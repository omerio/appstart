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

import uk.co.inetria.appstart.common.entities.Todo;
import uk.co.inetria.appstart.common.services.TodoService;
import uk.co.inetria.appstart.common.utils.RestUtils;

import com.google.inject.Inject;

/**
 * JAX-WS Endpoint
 * @author omerio
 *
 */
@Path("/todo")
@Produces(MediaType.APPLICATION_JSON)
public class TodoAction {
	
	private static final Logger log = Logger.getLogger(TodoAction.class.getName());
	
	private TodoService service;
	
	@GET
	public List<Todo> list() {
		return Todo.findAllByArchived(false);
	}
	
	@GET
	@Path("/{id}")
	public Todo get(@PathParam("id") Long id) {
		Todo todo = Todo.findById(id);
		if(todo == null) {
			RestUtils.throwRestException("Todo with id: " + id + ", is not found", 
					Response.Status.NOT_FOUND);
		}
		return todo;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Todo create(Todo todo) {
		
		try {
			service.create(todo);
			
		} catch(Exception e) {
			log.log(Level.SEVERE, "Something went wrong", e);
			RestUtils.throwRestException(e.getMessage(), Response.Status.BAD_REQUEST);
		}
		
		return todo;
	}
	
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Todo update(@PathParam("id") Long id,
							   Todo todo) {
		try {
			service.update(todo, id);
			
		} catch(Exception e) {
			log.log(Level.SEVERE, "Something went wrong", e);
			RestUtils.throwRestException(e.getMessage(), Response.Status.BAD_REQUEST);
		}
		
		return todo;
	}
	
	@DELETE
	@Path("/{id}")
	public Todo delete(@PathParam("id") Long id) {
		
		Todo todo = null;
		try {
			todo = service.delete(id);
			
		} catch(Exception e) {
			log.log(Level.SEVERE, "Something went wrong", e);
			RestUtils.throwRestException(e.getMessage(), Response.Status.BAD_REQUEST);
		}
		return todo;
	}

	@Inject
	public void setService(TodoService service) {
		this.service = service;
	}
	
}
