/**
 * Math.java
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

package ca.hss.math;

public class Math {
	public static boolean isDouble(String val) {
		try {
			Double.parseDouble(val);
		}
		catch(NumberFormatException ex) {
			return false;
		}
		return true;
	}
	
	public static boolean isInt(String val) {
		try {
			Integer.parseInt(val);
		}
		catch(NumberFormatException ex) {
			return false;
		}
		return true;
	}
	
	public static boolean isInt(char val) {
		try {
			Integer.parseInt(String.valueOf(val));
		}
		catch(NumberFormatException ex) {
			return false;
		}
		return true;
	}
	
	public static final double PI = java.lang.Math.PI;
}
