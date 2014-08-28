/**
 * 
 */
package uk.co.inetria.appstart.common.services;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;

import uk.co.inetria.appstart.common.entities.Announcement;

/**
 * @author dawelbeito
 *
 */
public class AnnouncementServiceImpl implements AnnouncementService {
	
	private static final Logger log = Logger.getLogger(AnnouncementServiceImpl.class.getName());
	
	@Override
	public Announcement create(Announcement announcement) {
		log.info("Creating new announcement: " + announcement);
		Validate.notNull(announcement);
		Validate.isTrue(announcement.valid());
		announcement.setId(null);
		announcement.setArchived(false);
		announcement.setDateAdded(null);
		announcement.setDateArchived(null);
		announcement.cleanTitle();
		announcement.save();
		return announcement;
	}

	@Override
	public Announcement update(Announcement announcement, Long id) {
		log.info("Updating announcement with id: " + id + ", entity: " + announcement);
		Validate.notNull(id);
		Validate.notNull(announcement);
		Validate.isTrue(announcement.valid());
		announcement.cleanTitle();
		
		Announcement dbEntity = Announcement.findById(id);
		Validate.notNull(dbEntity);
		dbEntity.setTitle(announcement.getTitle());
		dbEntity.save();
		return dbEntity;
	}
	
	@Override
	public Announcement delete(Long id) {
		log.info("Deleting announcement with id: " + id);
		Validate.notNull(id);
		Announcement announcement = Announcement.findById(id);
		
		if(announcement != null) {
			announcement.remove();
		}
		return announcement;
	}
	
	@Override
	public List<Announcement> archive() {
		log.info("Archiving old announcements");
		// archive any announcements old than 24 hours
		Date today = new Date();
		Date date = DateUtils.addHours(today, -24);
		List<Announcement> announcements = Announcement.findAllToArchive(date);
		
		if(!announcements.isEmpty()) {
			for(Announcement announcement: announcements) {
				announcement.setArchived(true);
				announcement.setDateArchived(today);
			}
			
			Announcement.saveAll(announcements);
		}
		return announcements;
	}
}
