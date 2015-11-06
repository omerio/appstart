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

import java.util.HashMap;

import uk.co.inetria.appstart.frontend.servlets.HomeServlet;

import com.google.inject.servlet.ServletModule;
import com.googlecode.objectify.ObjectifyFilter;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

/**
 * Register all the servlets & filters here so Guice can manage their dependencies
 * and parse the @Inject annotation
 * @author Omer Dawelbeit
 *
 */
public class AppServletModule extends ServletModule {
	
	
	
	/* (non-Javadoc)
	 * @see com.google.inject.servlet.ServletModule#configureServlets()
	 */
	@Override
	protected void configureServlets() {
		
		// filters
		// Objectify filter
		filter("/*").through(ObjectifyFilter.class);

		// servlets
		serve("/home").with(HomeServlet.class);
		
		// Jersey restful servlet
	    HashMap<String, String> params = new HashMap<String, String>();
        params.put(PackagesResourceConfig.PROPERTY_PACKAGES, "uk.co.inetria.appstart.frontend.rest");
        params.put(JSONConfiguration.FEATURE_POJO_MAPPING, "true");
        // speed up jersey startup under appengine
        params.put(ResourceConfig.FEATURE_DISABLE_WADL, "true");
        
        serve("/api/*").with(GuiceContainer.class, params);
		
		
	}

}
