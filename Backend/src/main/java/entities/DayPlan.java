package entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author Andreas Vikke
 */
@Entity
public class DayPlan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String recipeId;
    private int dayOfWeek;
    
    @ManyToOne
    @JoinColumn( name="menuPlan_id" )
    private MenuPlan menuPlan;

    public DayPlan() {
    }

    public DayPlan(String recipeId, int dayOfWeek, MenuPlan menuPlan) {
        this.recipeId = recipeId;
        this.dayOfWeek = dayOfWeek;
        this.menuPlan = menuPlan;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public MenuPlan getMenuPlan() {
        return menuPlan;
    }

    public void setMenuPlan(MenuPlan menuPlan) {
        this.menuPlan = menuPlan;
    }
}
