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
package uk.co.inetria.appstart.backend.di;

import uk.co.inetria.appstart.common.services.TodoService;
import uk.co.inetria.appstart.common.services.TodoServiceImpl;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.googlecode.objectify.ObjectifyFilter;


/**
 * Bind which classes will be initialized when Guice encounters the 
 * @Inject annotation
 * @author Omer Dawelbeit
 *
 */
public class BusinessLogicModule extends AbstractModule {
	
	/* (non-Javadoc)
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {
		
		bind(ObjectifyFilter.class).in(Singleton.class);
		bind(TodoService.class).to(TodoServiceImpl.class).in(Scopes.SINGLETON);
		
	}

}
