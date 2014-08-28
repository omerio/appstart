package uk.co.inetria.appstart.frontend.endpoint;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Named;
import javax.ws.rs.core.Response;

import uk.co.inetria.appstart.common.entities.Announcement;
import uk.co.inetria.appstart.common.services.AnnouncementService;
import uk.co.inetria.appstart.common.utils.Utils;
import uk.co.inetria.appstart.frontend.Constants;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.inject.Inject;

/**
 * Defines v1 of a helloworld API, which provides simple "greeting" methods.
 */
@Api(
		name = "appstart",
		version = "v1",
		scopes = {Constants.EMAIL_SCOPE},
		clientIds = {Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID, Constants.IOS_CLIENT_ID},
		audiences = {Constants.ANDROID_AUDIENCE}
)
public class AnnouncementEndpoint {

	private static final Logger log = Logger.getLogger(AnnouncementEndpoint.class.getName());
	
	@Inject
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	private AnnouncementService service;
	
	@ApiMethod(name = "announcements.list")
	public List<Announcement> list() {
		return Announcement.findAll(false);
	}
	
	@ApiMethod(name = "announcements.get")
	public Announcement get(@Named("id") Long id) {
		Announcement announcement = Announcement.findById(id);
		if(announcement == null) {
			Utils.throwRestException("Announcement with id: " + id + ", is not found", 
					Response.Status.NOT_FOUND);
		}
		return announcement;
	}
	
	@ApiMethod(name = "announcements.create", httpMethod = "post")
	public Announcement create(Announcement announcement) {
		
		try {
			service.create(announcement);
			
		} catch(Exception e) {
			log.log(Level.SEVERE, "Something went wrong", e);
			Utils.throwRestException(e.getMessage(), Response.Status.BAD_REQUEST);
		}
		
		return announcement;
	}
	
	@ApiMethod(
		name = "announcements.update",
		path = "announcements/{id}"
	)
	public Announcement update(@Named("id") Long id,
							   Announcement announcement) {
		try {
			service.update(announcement, id);
			
		} catch(Exception e) {
			log.log(Level.SEVERE, "Something went wrong", e);
			Utils.throwRestException(e.getMessage(), Response.Status.BAD_REQUEST);
		}
		
		return announcement;
	}
	
	@ApiMethod(
		name = "announcements.delete",
		path = "announcements/{id}"
	)
	public Announcement delete(@Named("id") Long id) {
		
		Announcement announcement = null;
		try {
			announcement = service.delete(id);
			
		} catch(Exception e) {
			log.log(Level.SEVERE, "Something went wrong", e);
			Utils.throwRestException(e.getMessage(), Response.Status.BAD_REQUEST);
		}
		return announcement;
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
	
	/*
	 * 
	 * public static ArrayList<HelloGreeting> greetings = new ArrayList<HelloGreeting>();

	static {
		greetings.add(new HelloGreeting("hello world!"));
		greetings.add(new HelloGreeting("goodbye world!"));
	}

	public HelloGreeting getGreeting(@Named("id") Integer id) throws NotFoundException {
		try {
			return greetings.get(id);
		} catch (IndexOutOfBoundsException e) {
			throw new NotFoundException("Greeting not found with an index: " + id);
		}
	}

	public ArrayList<HelloGreeting> listGreeting() {
		return greetings;
	}

	@ApiMethod(name = "greetings.multiply", httpMethod = "post")
	public HelloGreeting insertGreeting(@Named("times") Integer times, HelloGreeting greeting) {
		HelloGreeting response = new HelloGreeting();
		StringBuilder responseBuilder = new StringBuilder();
		for (int i = 0; i < times; i++) {
			responseBuilder.append(greeting.getMessage());
		}
		response.setMessage(responseBuilder.toString());
		return response;
	}

	@ApiMethod(name = "greetings.authed", path = "hellogreeting/authed")
	public HelloGreeting authedGreeting(User user) {
		HelloGreeting response = new HelloGreeting("hello " + user.getEmail());
		return response;
	}
	 */
	 
}
