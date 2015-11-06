/**
 * 
 */
package uk.co.inetria.appstart.common.services;

import java.util.List;

import uk.co.inetria.appstart.common.entities.Todo;

/**
 * You services will usually expose an interface and the implementation is automatically injected
 * using Dependency Injection. This makes it easy to swap one implementation for the other in the future
 * and makes your code loosely coupled.
 * 
 * The service is a reusable chunk of business functionality that can injected wherever is needed  
 * 
 * @author dawelbeito
 *
 */
public interface TodoService {

	public Todo create(Todo todo);

	public Todo update(Todo todo, Long id);

	public Todo delete(Long id);

	public List<Todo> archive();

}
