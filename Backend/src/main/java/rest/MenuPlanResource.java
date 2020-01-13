package rest;

import entities.dto.MenuPlanDTO;
import facades.MenuPlanFacade;
import java.util.List;
import utils.EMF_Creator;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

//Todo Remove or change relevant parts before ACTUAL use
@Path("menuplan")
public class MenuPlanResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    private static final MenuPlanFacade FACADE =  MenuPlanFacade.getFacade(EMF);
    
    @Path("{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public MenuPlanDTO getSingleMenuPlanById(@PathParam("id") long id) {
        try {
            return FACADE.getSingleMenuPlanById(id);
        } catch(NoResultException ex) {
            throw new WebApplicationException("No MenuPlan with id: " + id + " was found.", Status.NOT_FOUND);
        }
    }
    
    @Path("week/{week}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public MenuPlanDTO getSingleMenuPlanByWeek(@PathParam("week") int week) {
        try {
        return FACADE.getSingleMenuPlanByWeek(week);
        } catch(NoResultException ex) {
            throw new WebApplicationException("No MenuPlan with week: " + week + " was found.", Status.NOT_FOUND);
        }
    }

    @Path("all/{username}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<MenuPlanDTO> getSingleMenuPlanById(@PathParam("username") String username) {
        return FACADE.getAllMenuPlansByUser(username);
    }
}
