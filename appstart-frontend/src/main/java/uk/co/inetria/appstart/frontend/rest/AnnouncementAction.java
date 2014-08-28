package uk.co.inetria.appstart.frontend.rest;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import uk.co.inetria.appstart.common.entities.Announcement;
import uk.co.inetria.appstart.common.services.AnnouncementService;
import uk.co.inetria.appstart.common.utils.Utils;

import com.google.inject.Inject;

/**
 * 
 * @author omerio
 *
 */
@Path("/announcement")
@Produces(MediaType.APPLICATION_JSON)
public class AnnouncementAction {
	
	private static final Logger log = Logger.getLogger(AnnouncementAction.class.getName());
	
	private AnnouncementService service;
	
	@GET
	public List<Announcement> list() {
		return Announcement.findAll(false);
	}
	
	@GET
	@Path("/{id}")
	public Announcement get(@PathParam("id") Long id) {
		Announcement announcement = Announcement.findById(id);
		if(announcement == null) {
			Utils.throwRestException("Announcement with id: " + id + ", is not found", 
					Response.Status.NOT_FOUND);
		}
		return announcement;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Announcement create(Announcement announcement) {
		
		try {
			service.create(announcement);
			
		} catch(Exception e) {
			log.log(Level.SEVERE, "Something went wrong", e);
			Utils.throwRestException(e.getMessage(), Response.Status.BAD_REQUEST);
		}
		
		return announcement;
	}
	
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Announcement update(@PathParam("id") Long id,
							   Announcement announcement) {
		try {
			service.update(announcement, id);
			
		} catch(Exception e) {
			log.log(Level.SEVERE, "Something went wrong", e);
			Utils.throwRestException(e.getMessage(), Response.Status.BAD_REQUEST);
		}
		
		return announcement;
	}
	
	@DELETE
	@Path("/{id}")
	public Announcement delete(@PathParam("id") Long id) {
		
		Announcement announcement = null;
		try {
			announcement = service.delete(id);
			
		} catch(Exception e) {
			log.log(Level.SEVERE, "Something went wrong", e);
			Utils.throwRestException(e.getMessage(), Response.Status.BAD_REQUEST);
		}
		return announcement;
	}

	@Inject
	public void setService(AnnouncementService service) {
		this.service = service;
	}
	
}
