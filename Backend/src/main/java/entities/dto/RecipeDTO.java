package entities.dto;

import java.util.List;

/**
 *
 * @author Andreas Vikke
 */
public class RecipeDTO {
    private String id;
    private String description;
    private String prep_time;
    private List<String> preparaion_steps;
    private List<String> ingredients;

    public RecipeDTO(String id, String description, String prep_time, List<String> preparaion_steps, List<String> ingredients) {
        this.id = id;
        this.description = description;
        this.prep_time = prep_time;
        this.preparaion_steps = preparaion_steps;
        this.ingredients = ingredients;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrep_time() {
        return prep_time;
    }

    public void setPrep_time(String prep_time) {
        this.prep_time = prep_time;
    }

    public List<String> getPreparaion_steps() {
        return preparaion_steps;
    }

    public void setPreparaion_steps(List<String> preparaion_steps) {
        this.preparaion_steps = preparaion_steps;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
