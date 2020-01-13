package facades;

import com.google.gson.Gson;
import entities.DayPlan;
import entities.dto.RecipeDTO;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author emilt
 */
public class RecipeFacade {
    private static final Gson gson = new Gson();

    //This fetch method returns a string with json format
    //based on a given url (using HTTP connection and a request method).
    public Map.Entry<Long, RecipeDTO> fetch(String urlStr, long id) {
        HttpURLConnection con = null;
        try {
            URL url = new URL(urlStr);
            System.out.println(url);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json;charset=UTF-8");
            con.addRequestProperty("User-Agent", "Mozilla/4.76;Chrome"); 
            String jsonStr = "";
            try ( Scanner scan = new Scanner(con.getInputStream())) {
                while (scan.hasNext()) {
                    jsonStr += scan.nextLine();
                }
            }
            return new SimpleEntry<Long, RecipeDTO>(id, gson.fromJson(jsonStr, RecipeDTO.class));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            con.disconnect();
        }
    }

    //This fetch method returns a string with json format
    //based on a given url and a specific* (using HTTP connection and a request method).
    //*a specific is abstract for a given identity to a variable on an endpoint
    //example would be a specific person ID on a person API.
    public RecipeDTO fetch(String urlStr, String specific) {
        HttpURLConnection con = null;
        try {
            URL url = new URL(urlStr + specific);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json;charset=UTF-8");
            con.addRequestProperty("User-Agent", "Mozilla/4.76;Chrome");
            String jsonStr = "";
            try ( Scanner scan = new Scanner(con.getInputStream())) {
                while (scan.hasNext()) {
                    jsonStr += scan.nextLine();
                }
            }
            return gson.fromJson(jsonStr, RecipeDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            con.disconnect();
        }
    }

    //This fetch method returns a list of strings with json format
    //based on a given url and a list of specifics* (using HTTP connection and a request method).
    //See the definition of a "specific" above.
    public Map<Long, RecipeDTO> fetch(List<DayPlan> specificList) {
        final ExecutorService executor = Executors.newCachedThreadPool();
        try {
            Queue<Future<Map.Entry<Long, RecipeDTO>>> queue = new ArrayBlockingQueue(specificList.size());
            Map<Long, RecipeDTO> res = new HashMap();
            for (DayPlan specifc : specificList) {
                Future<Map.Entry<Long, RecipeDTO>> future = executor.submit(() -> {
                    return fetch("http://46.101.217.16:4000/recipe/" + specifc.getRecipeId().replace(" ", "%20"), specifc.getId());
                });
                queue.add(future);
            }
            while (!queue.isEmpty()) {
                Future<Map.Entry<Long, RecipeDTO>> specific = queue.poll();
                if (specific.isDone()) {
                    res.put(specific.get().getKey(), specific.get().getValue());
                } else {
                    queue.add(specific);
                }
            }
            return res;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        } finally {
            executor.shutdown();
        }
    }
}
