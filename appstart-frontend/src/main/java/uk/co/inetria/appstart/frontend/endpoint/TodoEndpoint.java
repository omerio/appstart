package uk.co.inetria.appstart.frontend.endpoint;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Named;

import uk.co.inetria.appstart.common.entities.Todo;
import uk.co.inetria.appstart.common.services.TodoService;
import uk.co.inetria.appstart.frontend.Constants;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.appengine.api.users.User;
import com.google.inject.Inject;

/**
 * Defines v1 of a appstart todo API, which provides simple CRUD methods.
 */
@Api(
		name = "appstart",
		version = "v1",
		scopes = {Constants.EMAIL_SCOPE},
		clientIds = {Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID, Constants.IOS_CLIENT_ID},
		audiences = {Constants.ANDROID_AUDIENCE}
)
public class TodoEndpoint {

	private static final Logger log = Logger.getLogger(TodoEndpoint.class.getName());
	
	@Inject
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	private TodoService service;
	
	@ApiMethod(name = "todos.list")
	public List<Todo> list(User user) {
		this.authenticate(user);
		return Todo.findAllByArchived(false);
	}
	
	@ApiMethod(name = "todos.get")
	public Todo get(@Named("id") Long id, User user) {
		this.authenticate(user);
		Todo todo = Todo.findById(id);
		if(todo == null) {
			throw new RuntimeException("Todo with id: " + id + ", is not found");
		}
		return todo;
	}
	
	@ApiMethod(name = "todos.create", httpMethod = "post")
	public Todo create(Todo dodo, User user) {
		this.authenticate(user);
		try {
			service.create(dodo);
			
		} catch(Exception e) {
			log.log(Level.SEVERE, "Something went wrong", e);
			throw e;
		}
		
		return dodo;
	}
	
	@ApiMethod(
		name = "todos.update",
		path = "todos/{id}"
	)
	public Todo update(@Named("id") Long id,
							   Todo todo, User user) {
		this.authenticate(user);
		try {
			service.update(todo, id);
			
		} catch(Exception e) {
			log.log(Level.SEVERE, "Something went wrong", e);
			throw e;
		}
		
		return todo;
	}
	
	@ApiMethod(
		name = "todos.delete",
		path = "todos/{id}"
	)
	public Todo delete(@Named("id") Long id, User user) {
		this.authenticate(user);
		Todo todo = null;
		try {
			todo = service.delete(id);
			
		} catch(Exception e) {
			log.log(Level.SEVERE, "Something went wrong", e);
			throw e;
		}
		return todo;
	}
	
	private void authenticate(User user) {
		if(user == null) {
			log.warning("User is not authenticated");
			//throw new RuntimeException("Authentication required!");
		} else {
			// further validation such as domain checking, etc...
			log.info(user.getEmail());
		}
	}

	/*
	 * Although this has the ignore annotation it's still getting exposed
	 * 
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	@Inject
	public void setService(AnnouncementService service) {
		this.service = service;
	}
	*/
	 
}
