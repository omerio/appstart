/**
 * 
 */
package uk.co.inetria.appstart.common.utils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Some RESTful API utilities
 * @author dawelbeito
 *
 */
public class RestUtils {
	
	/**
	 * Rest exceptions
	 * @param message
	 * @param status
	 * @return
	 */
	public static Response buildRestException(String message, Status status) {
		return Response.status(status).entity(message)
				.type(MediaType.TEXT_PLAIN).build();
	}

	/**
	 * Rest responses
	 * @param data
	 * @return
	 */
	public static Response buildRestResponse(Object data) {
		return Response.status(Response.Status.OK).entity(data).build();
	}

	/**
	 * Throws a rest webapplication exception
	 * @param message
	 * @param status
	 */
	public static void throwRestException(String message, Status status) {
		throw new WebApplicationException(buildRestException(message, status));
	}

}
