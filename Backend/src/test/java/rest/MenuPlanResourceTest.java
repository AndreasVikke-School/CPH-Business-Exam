/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import entities.DayPlan;
import entities.MenuPlan;
import entities.Role;
import entities.User;
import entities.dto.MenuPlanDTO;
import facades.RecipeFacade;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

/**
 *
 * @author APC
 */
public class MenuPlanResourceTest {

    private static EntityManagerFactory emf;
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.DROP_AND_CREATE);
        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
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
            for (DayPlan dayPlan : dayPlans) {
                em.persist(dayPlan);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterAll
    public static void closeTestServer() {
        //Don't forget this, if you called its counterpart in @BeforeAll
        httpServer.shutdownNow();
    }

    private static String securityToken;

    //Utility method to login and set the returned securityToken
    private static void login(String role, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");
        System.out.println("TOKEN ---> " + securityToken);
    }

    private void logOut() {
        securityToken = null;
    }

    /**
     * Test of getJson method, of class SwapiResource.
     */
    @Test
    public void getSingleMenuPlanByIdTest() {
        login("user", "test1");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .get("/menuplan/" + menuPlan.getId()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("week", equalTo(menuPlan.getWeek()));
    }

    @Test
    public void getSingleMenuPlanByIdTest404() {
        login("user", "test1");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .get("/menuplan/0").then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode());
    }
    
    @Test
    public void getSingleMenuPlanByWeekTest() {
        login("user", "test1");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .get("/menuplan/week/" + menuPlan.getWeek() + "?username=" + user.getUserName()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("week", equalTo(menuPlan.getWeek()));
    }
    
    @Test
    public void getSingleMenuPlanByWeekTest404() {
        login("user", "test1");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .get("/menuplan/week/" + menuPlan.getWeek() + "?usename=" + user.getUserName()).then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode());
    }
    
    @Test
    public void getAllMenuPlansTest() {
        login("user", "test1");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .get("/menuplan/all/" + user.getUserName()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("size()", equalTo(1));
    }
    
    @Test
    public void createMenuPlanTest() {
        login("user", "test1");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .body(new MenuPlanDTO(menuPlan, RecipeFacade.getFacade().fetch(menuPlan.getDayPlans())))
                .post("/menuplan/add/").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("week", equalTo(1));
    }
    
    @Test
    public void deleteMenuPlanTest() {
        login("user", "test1");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .delete("/menuplan/delete/" + menuPlan.getId()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode());
    }
    
    @Test
    public void editMenuPlanTest() {
        login("user", "test1");
        given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .body(new MenuPlanDTO(menuPlan, RecipeFacade.getFacade().fetch(menuPlan.getDayPlans())))
                .post("/menuplan/edit").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode());
    }
}
