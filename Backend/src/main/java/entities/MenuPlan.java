package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author Andreas Vikke
 */
@Entity
@NamedQuery(name = "MenuPlan.deleteAllRows", query = "DELETE from MenuPlan")
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + Objects.hashCode(this.id);
        hash = 31 * hash + Objects.hashCode(this.user);
        hash = 31 * hash + this.week;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MenuPlan other = (MenuPlan) obj;
        if (this.week != other.week) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        return true;
    }
}
