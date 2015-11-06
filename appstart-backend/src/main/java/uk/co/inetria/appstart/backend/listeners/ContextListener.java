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
package uk.co.inetria.appstart.backend.listeners;

import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;

import uk.co.inetria.appstart.backend.di.AppServletModule;
import uk.co.inetria.appstart.backend.di.BusinessLogicModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * Servlet Context Listener for when the application is started, we initialize Guice here
 * @author Omer Dawelbeit
 *
 */
public class ContextListener extends GuiceServletContextListener {

	private static final Logger log = Logger.getLogger(ContextListener.class.getName());
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		super.contextDestroyed(event);
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		// Register you objectify entities here

		log.info("Context created");

	}

	@Override
	protected Injector getInjector() {
		// create an injector for the application servlets and business logic services
		return Guice.createInjector(
				new BusinessLogicModule(),
				new AppServletModule());
		
	}
	
	


}
