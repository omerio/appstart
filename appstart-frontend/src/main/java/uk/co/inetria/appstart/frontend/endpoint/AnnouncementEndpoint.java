package uk.co.inetria.appstart.frontend.endpoint;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Named;

import uk.co.inetria.appstart.common.entities.Announcement;
import uk.co.inetria.appstart.common.services.AnnouncementService;
import uk.co.inetria.appstart.frontend.Constants;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.appengine.api.users.User;
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
	public List<Announcement> list(User user) {
		this.authenticate(user);
		return Announcement.findAll(false);
	}
	
	@ApiMethod(name = "announcements.get")
	public Announcement get(@Named("id") Long id, User user) {
		this.authenticate(user);
		Announcement announcement = Announcement.findById(id);
		if(announcement == null) {
			throw new RuntimeException("Announcement with id: " + id + ", is not found");
		}
		return announcement;
	}
	
	@ApiMethod(name = "announcements.create", httpMethod = "post")
	public Announcement create(Announcement announcement, User user) {
		this.authenticate(user);
		try {
			service.create(announcement);
			
		} catch(Exception e) {
			log.log(Level.SEVERE, "Something went wrong", e);
			throw e;
		}
		
		return announcement;
	}
	
	@ApiMethod(
		name = "announcements.update",
		path = "announcements/{id}"
	)
	public Announcement update(@Named("id") Long id,
							   Announcement announcement, User user) {
		this.authenticate(user);
		try {
			service.update(announcement, id);
			
		} catch(Exception e) {
			log.log(Level.SEVERE, "Something went wrong", e);
			throw e;
		}
		
		return announcement;
	}
	
	@ApiMethod(
		name = "announcements.delete",
		path = "announcements/{id}"
	)
	public Announcement delete(@Named("id") Long id, User user) {
		this.authenticate(user);
		Announcement announcement = null;
		try {
			announcement = service.delete(id);
			
		} catch(Exception e) {
			log.log(Level.SEVERE, "Something went wrong", e);
			throw e;
		}
		return announcement;
	}
	
	private void authenticate(User user) {
		if(user == null) {
			log.warning("User is not authenticated");
			throw new RuntimeException("Authentication required!");
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
