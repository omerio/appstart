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

import lombok.Data;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.appengine.api.datastore.KeyFactory;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnSave;

/**
 * Todo entity. 
 * The CRUD pattern used here is similar to the Active Entity pattern
 * 
 * @author omerio
 *
 */
@Entity
@Data
public class Todo {

	@Id
	private Long id;

	private String title;

	@Index
	private boolean completed;

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

	public static List<Todo> findAll()	{
		return ofy().load().type(Todo.class).list();
	}

	public static List<Todo> findAllByArchived(boolean archived)	{
		return ofy().load().type(Todo.class).filter("archived", archived).list();
	}
	
	public static List<Todo> findAllByCompleted(boolean completed)	{
		return ofy().load().type(Todo.class).filter("completed", completed).list();
	}

	/**
	 * Find all todos which are completed, not archived and are older than the provided
	 * date.
	 * @param date
	 * @return
	 */
	public static List<Todo> findAllToArchive(Date date)	{
		return ofy().load().type(Todo.class)
				.filter("archived", false)
				.filter("completed", true)
				.filter("dateAdded <=", date).list();
	}



	public static Todo findByKey(String key) {
		return findByKey(KeyFactory.stringToKey(key));
	}

	public static Todo findByKey(com.google.appengine.api.datastore.Key key) {
		return (Todo) ofy().load().value(key).now();
	}

	public static Todo findById(Long id) {
		return ofy().load().type(Todo.class).id(id).now();
	}

	public Key<Todo> save()	{
		return ofy().save().entity(this).now();
	}

	public static Map<Key<Todo>, Todo> saveAll(List<Todo> todos)	{
		return ofy().save().entities(todos).now();
	}

	public void remove()	{
		ofy().delete().entity(this).now();
	}

	public static void removeAll(List<Todo> todos) {
		ofy().delete().entities(todos).now();
	}

}
