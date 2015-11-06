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
package uk.co.inetria.appstart.frontend.di;

import java.util.HashSet;
import java.util.Set;

import uk.co.inetria.appstart.frontend.endpoint.TodoEndpoint;

import com.google.api.server.spi.guice.GuiceSystemServiceServletModule;

/**
 * 
 * 
 * @author Omer Dawelbeit
 *
 */
public class CloudEndpointModule extends GuiceSystemServiceServletModule {
	
	@Override
	  protected void configureServlets() {
	    super.configureServlets();

	    Set<Class<?>> serviceClasses = new HashSet<Class<?>>();
	    serviceClasses.add(TodoEndpoint.class);
	    
	    this.serveGuiceSystemServiceServlet("/_ah/spi/*", serviceClasses);
	  }

}
