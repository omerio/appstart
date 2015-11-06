/**
 * 
 */
package uk.co.inetria.appstart.common.services;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;

import uk.co.inetria.appstart.common.entities.Todo;

/**
 * @author dawelbeito
 *
 */
public class TodoServiceImpl implements TodoService {
	
	// Java built-in logging is a lot faster on App Engine
	private static final Logger log = Logger.getLogger(TodoServiceImpl.class.getName());
	
	@Override
	public Todo create(Todo todo) {
		log.info("Creating new todo: " + todo);
		Validate.notNull(todo);
		Validate.isTrue(todo.valid());
		todo.setId(null);
		todo.setArchived(false);
		todo.setDateAdded(null);
		todo.setDateArchived(null);
		todo.cleanTitle();
		todo.save();
		return todo;
	}

	@Override
	public Todo update(Todo todo, Long id) {
		log.info("Updating todo with id: " + id + ", entity: " + todo);
		Validate.notNull(id);
		Validate.notNull(todo);
		Validate.isTrue(todo.valid());
		todo.cleanTitle();
		
		Todo dbEntity = Todo.findById(id);
		Validate.notNull(dbEntity);
		dbEntity.setTitle(todo.getTitle());
		dbEntity.setCompleted(todo.isCompleted());
		dbEntity.save();
		return dbEntity;
	}
	
	@Override
	public Todo delete(Long id) {
		log.info("Deleting todo with id: " + id);
		Validate.notNull(id);
		Todo todo = Todo.findById(id);
		
		if(todo != null) {
			todo.remove();
		}
		return todo;
	}
	
	@Override
	public List<Todo> archive() {
		log.info("Archiving old todos");
		// archive any todos old than 24 hours
		Date today = new Date();
		Date date = DateUtils.addHours(today, -24);
		List<Todo> todos = Todo.findAllToArchive(date);
		
		if(!todos.isEmpty()) {
			for(Todo todo: todos) {
				todo.setArchived(true);
				todo.setDateArchived(today);
			}
			
			Todo.saveAll(todos);
		}
		return todos;
	}
}
