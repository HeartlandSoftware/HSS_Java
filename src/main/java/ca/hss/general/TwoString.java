/**
 * TwoString.java
 *
 * Copyright 2015-2021 Heartland Software Solutions Inc.
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
 * A class to store two strings.
 * 
 * @author Travis
 *
 */
public class TwoString {
	private String secondary = null;
	private String text;
	
	/**
	 * Create a NumberString with a specific number and text.
	 * @param text
	 * @param value
	 */
	public TwoString(String text, String secondary) {
		this.text = text;
		this.secondary = secondary;
	}
	
	/**
	 * Set the secondary string.
	 * @param value
	 */
	public void setSecondary(String value) {
		this.secondary = value;
	}
	
	/**
	 * Get the secondary string.
	 * @return
	 */
	public String getSecondary() { return secondary; }
	
	/**
	 * Set the text.
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * Get the text.
	 * @return
	 */
	public String getText() { return text; }
	
	@Override
	public String toString() { return text; }
}
