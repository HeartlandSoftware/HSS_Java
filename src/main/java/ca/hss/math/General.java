/**
 * general.java
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

package ca.hss.math;

import ca.hss.annotations.Source;

/**
 * General math routines like angle conversions.
 */
@Source(project="Math", sourceFile="angles.h")
public class General {
	public static final double HALF_PI = 1.57079632679489661923;
	public static final double PI = Math.PI;
	public static final double TWO_PI = 2.0 * Math.PI;

	public static double CARTESIAN_TO_COMPASS_RADIAN(double x) {
		return NORMALIZE_ANGLE_RADIAN((Math.PI*2.5 - x));
	}
	
	public static double CARTESIAN_TO_COMPASS_DEGREE(double x) {
		return NORMALIZE_ANGLE_DEGREE((450.0 - x));
	}
	
	public static double COMPASS_TO_CARTESIAN_DEGREE(double x) {
		return CARTESIAN_TO_COMPASS_DEGREE(x);
	}
	
	public static double COMPASS_TO_CARTESIAN_RADIAN(double x) {
		return CARTESIAN_TO_COMPASS_RADIAN(x);
	}
	
	public static double NORMALIZE_ANGLE_DEGREE(double x) {
		return ANGLE_CALCULATE_BASE(360.0, x);
	}
	
	public static double NORMALIZE_ANGLE_RADIAN(double x) {
		return ANGLE_CALCULATE_BASE(Math.PI*2, x);
	}
	
	public static double ANGLE_CALCULATE_BASE(double base, double number) {
		if (number >= 0.0) {
			if (number < base)
				return number;
			return (number % base);
		}
		return (number % base) + base;
	}
	
	public static double DEGREE_TO_RADIAN(double x) {
		return ((x/180.0)*Math.PI);
	}
	
	public static double RADIAN_TO_DEGREE(double x) {
		return ((x*180.0)*(1.0/Math.PI));
	}	
}
