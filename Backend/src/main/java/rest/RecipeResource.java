package rest;

import entities.dto.RecipeDTO;
import facades.RecipeFacade;
import java.util.List;
import javax.persistence.NoResultException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

@Path("recipe")
public class RecipeResource {

    private static final RecipeFacade facade =  RecipeFacade.getFacade();
    
    @Path("all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<RecipeDTO> getAllRecipes(@PathParam("id") long id) {
        return facade.fetchAllRecipies();
    }
    
    @Path("findByName/{name}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public RecipeDTO getSingleRecipeByName(@PathParam("name") String name) {
        try {
            return facade.fetch(name);
        } catch(NoResultException ex) {
            throw new WebApplicationException("No MenuPlan with id: " + name + " was found.", Status.NOT_FOUND);
        }
    }
}
