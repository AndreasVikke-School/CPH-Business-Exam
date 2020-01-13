/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import entities.Role;
import entities.User;
import errorhandling.AuthenticationException;
import java.util.ArrayList;
import utils.EMF_Creator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;

/**
 *
 * @author APC
 */
public class UserFacadeTest {

    private static EntityManagerFactory emf;
    private static UserFacade facade;
    private List<User> users;

    public UserFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/startcode_test",
                "dev",
                "ax2",
                EMF_Creator.Strategy.CREATE);
        facade = UserFacade.getUserFacade(emf);
    }

    @BeforeAll
    public static void setUpClassV2() {
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.DROP_AND_CREATE);
        facade = UserFacade.getUserFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        users = new ArrayList();
        Role userRole = new Role("user");
        Role adminRole = new Role("admin");

        try {
            em.getTransaction().begin();
            em.createNamedQuery("Role.deleteAllRows").executeUpdate();
            em.createNamedQuery("DayPlan.deleteAllRows").executeUpdate();
            em.createNamedQuery("MenuPlan.deleteAllRows").executeUpdate();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();

            em.persist(userRole);
            em.persist(adminRole);

            User user1 = new User("Asger", "badpassword");
            user1.addRole(userRole);
            em.persist(user1);
            users.add(user1);

            User user2 = new User("Emil", "notgoodeither");
            user2.addRole(adminRole);
            em.persist(user2);
            users.add(user2);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void getVerifiedUserTest() throws AuthenticationException {
        String expected = users.get(0).getUserName();
        String pass = users.get(1).getUserPass();
        System.out.println(pass);
        assertEquals(expected, facade.getVeryfiedUser(expected, "badpassword").getUserName());
    }
}
