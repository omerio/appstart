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

import com.google.appengine.api.datastore.KeyFactory;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Result;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;

/**
 * @author omerio
 *
 */
@Entity
public class Contact {
	
	@Id
	private Long id;
	
	private String name;
	
	@Index
	private String email;
	
	private String phone;
	
	private String blogUrl;
	
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
		
		if(email != null) {
			email = email.toLowerCase();
		}
	}
	
	
	//------- CRUD
	
	public static List<Contact> findAll()	{
		return ofy().load().type(Contact.class).list();
	}


	public static Contact findByKey(String key) {
		return findByKey(KeyFactory.stringToKey(key));
	}

	public static Contact findByKey(com.google.appengine.api.datastore.Key key) {
		return (Contact) ofy().load().value(key).now();
	}
	
	public static Contact findByEmail(String email) {
		if(email != null) {
			email = email.toLowerCase();
		}
		return ofy().load().type(Contact.class).id(email).now();
	}
	
	public Key<Contact> save()	{
		return ofy().save().entity(this).now();
	}

	public static Result<Map<Key<Contact>, Contact>> saveAll(List<Contact> contacts)	{
		return ofy().save().entities(contacts);
	}

	public void remove()	{
		ofy().delete().entity(this).now();
	}

	public static void removeAll(List<Contact> contact) {
		ofy().delete().entities(contact);
	}

	//------------- Getters and Setters
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}


	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}


	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}


	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}


	/**
	 * @return the blogUrl
	 */
	public String getBlogUrl() {
		return blogUrl;
	}


	/**
	 * @param blogUrl the blogUrl to set
	 */
	public void setBlogUrl(String blogUrl) {
		this.blogUrl = blogUrl;
	}


	/**
	 * @return the dateAdded
	 */
	public Date getDateAdded() {
		return dateAdded;
	}


	/**
	 * @param dateAdded the dateAdded to set
	 */
	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}


	/**
	 * @return the dateArchived
	 */
	public Date getDateArchived() {
		return dateArchived;
	}


	/**
	 * @param dateArchived the dateArchived to set
	 */
	public void setDateArchived(Date dateArchived) {
		this.dateArchived = dateArchived;
	}


}
