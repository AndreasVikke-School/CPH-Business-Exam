package rest;

import entities.dto.MenuPlanDTO;
import facades.MenuPlanFacade;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import utils.EMF_Creator;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

@Path("menuplan")
public class MenuPlanResource {

    private static final EntityManagerFactory emf
            = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/exam",
                "dev",
                "ax2",
                EMF_Creator.Strategy.CREATE);
    private static final MenuPlanFacade facade =  MenuPlanFacade.getFacade(emf);
    
    @Path("{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed({"user", "admin"})
    public MenuPlanDTO getSingleMenuPlanById(@PathParam("id") long id) {
        try {
            return facade.getSingleMenuPlanById(id);
        } catch(NoResultException ex) {
            throw new WebApplicationException("No MenuPlan with id: " + id + " was found.", Status.NOT_FOUND);
        }
    }
    
    @Path("week/{week}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed({"user", "admin"})
    public MenuPlanDTO getSingleMenuPlanByWeek(@PathParam("week") int week, @QueryParam("username") String username) {
        try {
            return facade.getSingleMenuPlanByWeek(week, username);
        } catch(NoResultException ex) {
            throw new WebApplicationException("No MenuPlan with week: " + week + " was found.", Status.NOT_FOUND);
        }
    }

    @Path("all/{username}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed({"user", "admin"})
    public List<MenuPlanDTO> getAllMenuPlans(@PathParam("username") String username) {
        return facade.getAllMenuPlansByUser(username);
    }
    
    @Path("add")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed({"user", "admin"})
    public MenuPlanDTO createMenuPlan(MenuPlanDTO menuPlanDTO) throws Exception {
        try {
            return facade.createMenuPlan(menuPlanDTO);
        } catch(NoResultException ex) {
            throw new WebApplicationException(ex.getMessage(), Status.NOT_FOUND);
        }
    }
    
    @Path("delete/{id}")
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed({"user", "admin"})
    public String deleteMenuPlan(@PathParam("id") long id) {
        try {
            facade.deleteMenuPlan(id);
            return "{\"message\":\"MenuPlan with id: " + id + " has been deleted\"}";
        } catch(NoResultException ex) {
            throw new WebApplicationException("No MenuPlan with id: " + id + " was found.", Status.NOT_FOUND);
        }
    }
    
    @Path("edit")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed({"user", "admin"})
    public MenuPlanDTO editMenuPlan(MenuPlanDTO menuPlanDTO) {
        try {
            return facade.editMenuPlan(menuPlanDTO);
        } catch(NoResultException ex) {
            throw new WebApplicationException("No MenuPlan with week: " + " was found.", Status.NOT_FOUND);
        }
    }
}
