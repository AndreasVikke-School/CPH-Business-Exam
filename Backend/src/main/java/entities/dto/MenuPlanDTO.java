package entities.dto;

import entities.DayPlan;
import entities.MenuPlan;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Andreas Vikke
 */
public class MenuPlanDTO {
    private Long id;
    private UserDTO user;
    private int week;
    private List<DayPlanDTO> dayPlans;
    private List<String> shoppingList;

    public MenuPlanDTO(Long id, UserDTO user, int week, List<DayPlanDTO> dayPlans) {
        this.id = id;
        this.user = user;
        this.week = week;
        this.dayPlans = dayPlans;
    }
    
    public MenuPlanDTO(MenuPlan menuPlan, Map<Long, RecipeDTO> recepies) {
        this.id = menuPlan.getId();
        this.user = new UserDTO(menuPlan.getUser());
        this.week = menuPlan.getWeek();
        this.dayPlans = new ArrayList();
        this.shoppingList = new ArrayList();
        
        for(DayPlan dayPlan : menuPlan.getDayPlans())
            dayPlans.add(new DayPlanDTO(dayPlan, recepies.get(dayPlan.getId())));
        for(DayPlanDTO dayPlanDTO : this.dayPlans)
            this.shoppingList.addAll(dayPlanDTO.getRecipeDTO().getIngredients());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public List<DayPlanDTO> getDayPlans() {
        return dayPlans;
    }

    public void setDayPlans(List<DayPlanDTO> dayPlans) {
        this.dayPlans = dayPlans;
    }

    public List<String> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(List<String> shoppingList) {
        this.shoppingList = shoppingList;
    }
}
