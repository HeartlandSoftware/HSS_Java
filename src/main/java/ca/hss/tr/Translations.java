/**
 * Translations.java
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

import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Translations {
	
	private static Locale baseLocale = Locale.getDefault();
	private static HashMap<String, Resources> resourceMap = new HashMap<>();

	/**
	 * Set the locale that resources are being loaded in.
	 */
	public static void setLocale(Locale locale) {
		baseLocale = locale;
		resourceMap.clear();
	}
	
	/**
	 * The locale that resources are being loaded in.
	 */
	public static Locale getLocale() {
		return baseLocale;
	}
	
	/**
	 * Get the resources for a specific class.
	 * @param contextClass The class to get the resources for.
	 */
	public static Resources getResources(final Class<?> contextClass) {
		return resourceMap.computeIfAbsent(contextClass.getSimpleName(), n -> {
			Resources retval = new Resources();
			boolean shouldContinue = true;
			boolean first = true;
			Class<?> cls = contextClass;
			do {
				ResourceBundle base = null;
				ResourceBundle bundle = null;
				try {
					base = ResourceBundle.getBundle(cls.getSimpleName(), Locale.ENGLISH);
					if (baseLocale.getLanguage().equals("en"))
						bundle = base;
					else
						bundle = ResourceBundle.getBundle(cls.getSimpleName(), baseLocale);
				}
				catch (MissingResourceException e) { }
				if ((base == null || bundle == null) && !first)
					shouldContinue = false;
				else {
					if (base != null && bundle != null)
						retval.addResources(base, bundle);
					cls = cls.getSuperclass();
					if (cls == null)
						shouldContinue = false;
				}
				first = false;
			} while (shouldContinue);
			return retval;
		});
	}
	
	/**
	 * Get a named resource bundle.
	 * @param name The name of the resource bundle to load.
	 */
	public static Resources getNamedResources(String name) {
		return resourceMap.computeIfAbsent(name, n -> {
			ResourceBundle base = ResourceBundle.getBundle(n, Locale.ENGLISH);
			ResourceBundle bundle;
			if (baseLocale.getLanguage().equals("en"))
				bundle = base;
			else
				bundle = ResourceBundle.getBundle(n, baseLocale);
			return new Resources(base, bundle);
		});
	}
}
