/**
 * The contents of this file may be used under the terms of the Apache License, Version 2.0
 * in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 *
 * Copyright 2014, dawelbeit.info
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.inetria.appstart.common.entities;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.appengine.api.datastore.KeyFactory;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;

/**
 * @author omerio
 *
 */
@Entity
public class Announcement {

	@Id
	private Long id;

	private String title;

	//@Index
	//private String addedBy;

	@Index
	private boolean archived;

	@Index
	private Date dateAdded;

	private Date dateArchived;

	/**
	 * On save update the date added
	 */
	@OnSave
	void onSave() {
		if(this.dateAdded == null) {
			this.dateAdded = new Date();
		}

		/*if(addedBy != null) {
			addedBy = addedBy.toLowerCase();
		}*/
	}

	public boolean valid() {
		return StringUtils.isNoneBlank(this.title);
	}

	/**
	 * Escape any HTML and Javascript
	 */
	public void cleanTitle() {
		this.title = StringEscapeUtils.escapeHtml4(StringEscapeUtils.escapeEcmaScript(this.title));
	}

	//------- CRUD

	public static List<Announcement> findAll()	{
		return ofy().load().type(Announcement.class).list();
	}

	public static List<Announcement> findAll(boolean archived)	{
		return ofy().load().type(Announcement.class).filter("archived", archived).list();
	}

	/**
	 * Find all announcements which are not archived and are older than the provided
	 * date.
	 * @param date
	 * @return
	 */
	public static List<Announcement> findAllToArchive(Date date)	{
		return ofy().load().type(Announcement.class)
				.filter("archived", false)
				.filter("dateAdded <=", date).list();
	}



	public static Announcement findByKey(String key) {
		return findByKey(KeyFactory.stringToKey(key));
	}

	public static Announcement findByKey(com.google.appengine.api.datastore.Key key) {
		return (Announcement) ofy().load().value(key).now();
	}

	/*public static List<Announcement> findByAddedBy(String email) {
		if(email != null) {
			email = email.toLowerCase();
		}
		return ofy().load().type(Announcement.class).filter("addedBy", email).list();
	}*/

	public static Announcement findById(Long id) {
		return ofy().load().type(Announcement.class).id(id).now();
	}

	public Key<Announcement> save()	{
		return ofy().save().entity(this).now();
	}

	public static Map<Key<Announcement>, Announcement> saveAll(List<Announcement> announcements)	{
		return ofy().save().entities(announcements).now();
	}

	public void remove()	{
		ofy().delete().entity(this).now();
	}

	public static void removeAll(List<Announcement> announcement) {
		ofy().delete().entities(announcement).now();
	}

	//------------- Getters and Setters

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isArchived() {
		return archived;
	}


	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public Date getDateAdded() {
		return dateAdded;
	}


	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}


	public Date getDateArchived() {
		return dateArchived;
	}


	public void setDateArchived(Date dateArchived) {
		this.dateArchived = dateArchived;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
