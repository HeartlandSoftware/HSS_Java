/**
 * OutVariable.java
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

package ca.hss.general;

/**
 * Used to return values through method parameters.
 * @author Travis Redpath
 *
 * @param <T> The type of object to store.
 */
public class OutVariable<T> {
	public T value;
	
	/**
	 * Is the stored value null?
	 * @return
	 */
	public boolean isNull() {
		return value == null;
	}
	
	/**
	 * Returns "--NULL--" if the stored object is null or calls the default toString method if it isn't.
	 */
	@Override
	public String toString() {
		if (isNull()) {
			return "--NULL--";
		}
		return value.toString();
	}
}
