package facades;

import entities.DayPlan;
import entities.MenuPlan;
import entities.User;
import entities.dto.DayPlanDTO;
import entities.dto.MenuPlanDTO;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class MenuPlanFacade {

    private static MenuPlanFacade instance;
    private static EntityManagerFactory emf;
    
    private MenuPlanFacade() {}
    
    public static MenuPlanFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new MenuPlanFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public MenuPlanDTO getSingleMenuPlanById(long id) throws NoResultException {
        EntityManager em = getEntityManager();
        try {
            MenuPlan menuPlan = em.createQuery("SELECT mp FROM MenuPlan mp WHERE mp.id = :id", MenuPlan.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return new MenuPlanDTO(menuPlan, RecipeFacade.getFacade().fetch(menuPlan.getDayPlans()));
        } finally {
            em.close();
        } 
    }
    
    public MenuPlanDTO getSingleMenuPlanByWeek(int week) throws NoResultException {
        EntityManager em = getEntityManager();
        try {
            MenuPlan menuPlan = em.createQuery("SELECT mp FROM MenuPlan mp WHERE mp.week = :week", MenuPlan.class)
                    .setParameter("week", week)
                    .getSingleResult();
            return new MenuPlanDTO(menuPlan, RecipeFacade.getFacade().fetch(menuPlan.getDayPlans()));
        } finally {
            em.close();
        } 
    }
    
    public List<MenuPlanDTO> getAllMenuPlansByUser(String username) {
        EntityManager em = getEntityManager();
        try {
            List<MenuPlan> menuPlans = em.createQuery("SELECT mp FROM MenuPlan mp WHERE mp.user.userName = :username", MenuPlan.class)
                    .setParameter("username", username)
                    .getResultList();
            List<MenuPlanDTO> menuPlansDTO = new ArrayList();
            for(MenuPlan menuPlan : menuPlans) 
                menuPlansDTO.add(new MenuPlanDTO(menuPlan, RecipeFacade.getFacade().fetch(menuPlan.getDayPlans())));
            return menuPlansDTO;
        } finally {
            em.close();
        } 
    }
    
    public MenuPlanDTO createMenuPlan(MenuPlanDTO menuPlanDTO) throws NoResultException {
        EntityManager em = getEntityManager();
        try {
            User user = em.find(User.class, menuPlanDTO.getUser().getUserName());
            if(user == null) 
                throw new NoResultException("User not found");
            
            List<DayPlan> dayPlans = new ArrayList();
            MenuPlan menuPlan = new MenuPlan(user, 0, dayPlans);
            
            for(DayPlanDTO dayPlanDTO : menuPlanDTO.getDayPlans())
                dayPlans.add(new DayPlan(dayPlanDTO.getRecipeDTO().getId(), dayPlanDTO.getDayOfWeek(), menuPlan));
            
            em.getTransaction().begin();
            em.persist(menuPlan);
            for(DayPlan dayPlan : dayPlans) {
                em.persist(dayPlan);
            }
            em.getTransaction().commit();
            return new MenuPlanDTO(menuPlan, RecipeFacade.getFacade().fetch(menuPlan.getDayPlans()));
        } finally {
            em.close();
        } 
    }
    
    public void deleteMenuPlan(long id) throws NoResultException {
        EntityManager em = getEntityManager();
        try {
            MenuPlan menuPlan = em.find(MenuPlan.class, id);
            if(menuPlan == null)
                throw new NoResultException("MenuPlan not found");
            
            em.getTransaction().begin();
            for(DayPlan dayPlan : menuPlan.getDayPlans()) {
                em.remove(dayPlan);
            }
            em.remove(menuPlan);
            em.getTransaction().commit();
        } finally {
            em.close();
        } 
    }
}
