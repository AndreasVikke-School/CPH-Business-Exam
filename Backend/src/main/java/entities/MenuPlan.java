package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Andreas Vikke
 */
@Entity
public class MenuPlan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private User user;
    private int week;
    
    @OneToMany( mappedBy = "menuPlan" )
    private List<DayPlan> dayPlans;
    
    public MenuPlan() {
    }

    public MenuPlan(User user, int week, List<DayPlan> dayPlans) {
        this.user = user;
        this.week = week;
        this.dayPlans = dayPlans;
    }
        
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public List<DayPlan> getDayPlans() {
        return dayPlans;
    }

    public void setDayPlans(List<DayPlan> dayPlans) {
        this.dayPlans = dayPlans;
    }
    
    public List<String> getShoppingList() {
        List<String> shoppingList = new ArrayList();
        
        return shoppingList;
    }
}
