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
package uk.co.inetria.appstart.backend.servlets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import uk.co.inetria.appstart.common.services.TodoService;

/**
 * This servlet will be invoked daily by a GAE cron job to archive all completed
 * todos that are more than 24 hours old
 * 
 * @author Omer Dawelbeit
 *
 */
@Singleton
public class BackendServlet extends HttpServlet {

	private static final long serialVersionUID = -3573239534075174833L;
	
	private static final Logger log = Logger.getLogger(BackendServlet.class.getName());
	
	public static final String CONTENT_TYPE_HTML = "text/html";

	private TodoService service;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		log.info("Backend started, instance: " + this);
		
		// run your batch job directly or utilise background threads using ThreadManager.createBackgroundThread()
		
		try {
			
			this.service.archive();
			
			response.setStatus(HttpServletResponse.SC_OK);
			
		} catch(Exception e) {
			
			log.log(Level.SEVERE, "Failed to run backend job",  e);
			
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
		response.setContentType(CONTENT_TYPE_HTML);
	}

	@Inject
	public void setService(TodoService service) {
		this.service = service;
	}

}
