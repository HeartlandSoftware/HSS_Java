/**
 * CollectionExtensions.java
 *
 * Copyright 2016 Heartland Software Solutions Inc.
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

package ca.hss.general;

/**
 * Helper methods for searching arrays.
 * 
 * @author Travis
 */
public abstract class CollectionExtensions {
	public static abstract class Selector<T> {
		public abstract boolean testEqual(T value);
	}
	
	/**
	 * Find an element in an array.
	 * @param array
	 * @param compare
	 * @return
	 */
	public static <T> T Find(T[] array, Selector<T> compare) {
		for (T val : array) {
			if (compare.testEqual(val))
				return val;
		}
		return null;
	}
}
