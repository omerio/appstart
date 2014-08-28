/**
 * 
 */
package uk.co.inetria.appstart.common.services;

import java.util.List;

import uk.co.inetria.appstart.common.entities.Announcement;

/**
 * @author dawelbeito
 *
 */
public interface AnnouncementService {

	public Announcement create(Announcement announcement);

	public Announcement update(Announcement announcement, Long id);

	public Announcement delete(Long id);

	public List<Announcement> archive();

}
