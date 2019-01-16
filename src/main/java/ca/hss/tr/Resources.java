/**
 * Resources.java
 *
 * Copyright 2015-2019 Heartland Software Solutions Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the license at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the LIcense is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ca.hss.tr;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class Resources {
	
	private final LinkedList<ResourcePair> resources = new LinkedList<>();
	
	Resources() { }
	
	Resources(ResourceBundle base, ResourceBundle bundle) {
		addResources(base, bundle);
	}
	
	void addResources(ResourceBundle base, ResourceBundle bundle) {
		resources.add(new ResourcePair(bundle, base));
	}
	
	/**
	 * Get a translated string from the resource bundle.
	 * @param key The resource values key.
	 * @return A translated string or the key if it is not found.
	 */
	public String getString(final String key) {
		for (ResourcePair pair : resources) {
			String retval = pair.getString(key);
			if (retval != null)
				return retval;
		}
		return key;
	}
	
	/**
	 * Get a translated and formatted string from the resource bundle.
	 * @param key The resource values key.
	 * @param vars Format parameters to substitute into the string.
	 * @return A translated string or the key if it is not found.
	 */
	public String getString(final String key, Object...vars) {
		try {
			MessageFormat format = null;
			String str = null;
			for (ResourcePair pair : resources) {
				String retval = pair.getString(key);
				if (retval != null) {
					str = retval;
					break;
				}
			}
			if (str == null)
				return key;
			else
				format = new MessageFormat(str);
			return format.format(vars);
		}
		catch (Exception e) {
			return key;
		}
	}
	
	/**
	 * A pair of related resource files.
	 * @author Travis Redpath
	 */
	private class ResourcePair {

		private final ResourceBundle bundle;
		private final ResourceBundle base;
		
		private ResourcePair(ResourceBundle bundle, ResourceBundle base) {
			this.bundle = bundle;
			this.base = base;
		}
		
		/**
		 * Get a translated string from the resource bundle.
		 * @param key The resource values key.
		 * @return A translated string or the key if it is not found.
		 */
		public String getString(final String key) {
			if (bundle.containsKey(key))
				return bundle.getString(key);
			else if (base.containsKey(key))
				return base.getString(key);
			return null;
		}
	}
}
