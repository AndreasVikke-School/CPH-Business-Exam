package facades;

import entities.DayPlan;
import utils.EMF_Creator;
import entities.MenuPlan;
import entities.Role;
import entities.User;
import entities.dto.DayPlanDTO;
import entities.dto.MenuPlanDTO;
import entities.dto.RecipeDTO;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

public class MenuPlanFacadeTest {

    private static EntityManagerFactory emf;
    private static MenuPlanFacade facade;

    public MenuPlanFacadeTest() {
    }
    
    @BeforeAll
    public static void setUpClassV2() {
       emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST,Strategy.DROP_AND_CREATE);
       facade = MenuPlanFacade.getFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }
    
    User user;
    MenuPlan menuPlan;

    // Setup the DataBase in a known state BEFORE EACH TEST
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Role.deleteAllRows").executeUpdate();
            em.createNamedQuery("DayPlan.deleteAllRows").executeUpdate();
            em.createNamedQuery("MenuPlan.deleteAllRows").executeUpdate();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            
            user = new User("user", "test1");
            Role userRole = new Role("user");
            user.addRole(userRole);
            
            List<DayPlan> dayPlans = new ArrayList();
            menuPlan = new MenuPlan(user, 1, dayPlans);

            dayPlans.add(new DayPlan("slow cooker beef stew", 1, menuPlan));
            dayPlans.add(new DayPlan("Smoked paprika goulash for the slow cooker", 2, menuPlan));
            dayPlans.add(new DayPlan("Moist garlic roasted chicken", 3, menuPlan));
            dayPlans.add(new DayPlan("Polly's eccles cakes", 4, menuPlan));
            dayPlans.add(new DayPlan("Braised beef in red wine", 5, menuPlan));
            dayPlans.add(new DayPlan("Tofu vindaloo", 6, menuPlan));
            dayPlans.add(new DayPlan("Pistachio chicken with pomegranate sauce", 7, menuPlan));

            em.persist(userRole);
            em.persist(user);
            em.persist(menuPlan);
            for(DayPlan dayPlan : dayPlans)
                em.persist(dayPlan);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    @Test
    public void getSingleMenuPlanByIdTest() {
        MenuPlanDTO result = facade.getSingleMenuPlanById(menuPlan.getId());
        
        List<DayPlan> dayPlans = new ArrayList();
        for(DayPlanDTO dayPlanDTO : result.getDayPlans()){
            DayPlan dp = new DayPlan(dayPlanDTO.getRecipeDTO().getId(), dayPlanDTO.getDayOfWeek(), menuPlan);
            dp.setId(dayPlanDTO.getId());
            dayPlans.add(dp);
        }
            
        MenuPlan menuPlanResult = new MenuPlan(new User(result.getUser().getUserName(), null), result.getWeek(), dayPlans);
        menuPlanResult.setId(result.getId());
        assertEquals(menuPlanResult, menuPlan);
    }
    
    @Test
    public void getSingleMenuPlanByWeekTest() {
        MenuPlanDTO result = facade.getSingleMenuPlanByWeek(menuPlan.getWeek(), user.getUserName());
        
        List<DayPlan> dayPlans = new ArrayList();
        for(DayPlanDTO dayPlanDTO : result.getDayPlans()){
            DayPlan dp = new DayPlan(dayPlanDTO.getRecipeDTO().getId(), dayPlanDTO.getDayOfWeek(), menuPlan);
            dp.setId(dayPlanDTO.getId());
            dayPlans.add(dp);
        }
            
        MenuPlan menuPlanResult = new MenuPlan(new User(result.getUser().getUserName(), null), result.getWeek(), dayPlans);
        menuPlanResult.setId(result.getId());
        assertEquals(menuPlanResult, menuPlan);
    }
    
    @Test
    public void getAllMenuPlansByUserTest() {
        List<MenuPlanDTO> result = facade.getAllMenuPlansByUser(user.getUserName());
        List<MenuPlan> result2 = new ArrayList();
        
        for(MenuPlanDTO mpdto : result) {
            List<DayPlan> dayPlans = new ArrayList();
            for(DayPlanDTO dayPlanDTO : mpdto.getDayPlans()){
                DayPlan dp = new DayPlan(dayPlanDTO.getRecipeDTO().getId(), dayPlanDTO.getDayOfWeek(), menuPlan);
                dp.setId(dayPlanDTO.getId());
                dayPlans.add(dp);
            }
            MenuPlan menuPlanResult = new MenuPlan(new User(mpdto.getUser().getUserName(), null), mpdto.getWeek(), dayPlans);
            menuPlanResult.setId(mpdto.getId());
            result2.add(menuPlanResult);
        }
        
        assertEquals(result2.size(), 1);
    }
    
    @Test
    public void createMenuPlanTest() {
        menuPlan.setWeek(2);
        facade.createMenuPlan(new MenuPlanDTO(menuPlan, RecipeFacade.getFacade().fetch(menuPlan.getDayPlans())));
        List<MenuPlanDTO> result = facade.getAllMenuPlansByUser(user.getUserName());
        
        assertEquals(result.size(), 2);
    }
    
    @Test
    public void editMenuPlanTest() {
        menuPlan.setWeek(1);
        MenuPlanDTO edit = new MenuPlanDTO(menuPlan, RecipeFacade.getFacade().fetch(menuPlan.getDayPlans()));
        edit.getDayPlans().get(0).setRecipeDTO(new RecipeDTO("Cheese and bacon stuffed pasta shells", null, null, null, null));
        facade.editMenuPlan(edit);
        MenuPlanDTO result = facade.getSingleMenuPlanById(menuPlan.getId());
        
        System.out.println(result.getDayPlans().get(0).getRecipeDTO().getId());
        assertTrue(result.getDayPlans().get(0).getRecipeDTO().getId().startsWith("Cheese"));
    }
    
    @Test
    public void deleteMenuPlanTest() {
        facade.deleteMenuPlan(menuPlan.getId());
        List<MenuPlanDTO> result = facade.getAllMenuPlansByUser(user.getUserName());
        
        assertEquals(result.size(), 0);
    }
    
    
}
