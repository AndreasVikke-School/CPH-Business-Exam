package entities.dto;

import entities.DayPlan;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andreas Vikke
 */
public class DayPlanDTO {
    private Long id;
    private RecipeDTO recipeDTO;
    private int dayOfWeek;

    public DayPlanDTO(Long id, RecipeDTO recipeDTO, int dayOfWeek) {
        this.id = id;
        this.recipeDTO = recipeDTO;
        this.dayOfWeek = dayOfWeek;
    }
    
    public DayPlanDTO(DayPlan dayPlan, RecipeDTO recipeDTO) {
        this.id = dayPlan.getId();
        this.recipeDTO = recipeDTO;
        this.dayOfWeek = dayPlan.getDayOfWeek();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RecipeDTO getRecipeDTO() {
        return recipeDTO;
    }

    public void setRecipeDTO(RecipeDTO recipeDTO) {
        this.recipeDTO = recipeDTO;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
