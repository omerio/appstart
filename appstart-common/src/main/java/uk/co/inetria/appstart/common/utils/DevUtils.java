/**
 * 
 */
package uk.co.inetria.appstart.common.utils;

import com.google.appengine.api.utils.SystemProperty;

/**
 * App Engine Development Utilities
 * @author omerio
 *
 */
public class DevUtils {
	
	/**
	 * Check if we are running on the Dev environment
	 * @return
	 */
	public static boolean isDevelopment() {
		return (SystemProperty.environment.value() == SystemProperty.Environment.Value.Development);
	}

}
