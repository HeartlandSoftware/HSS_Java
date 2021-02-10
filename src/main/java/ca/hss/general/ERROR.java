/**
 * ERROR.java
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
 * Error values.
 */
public abstract class ERROR {
	public static final long S_OK = 0;
	public static final long S_FALSE = 1;
	public static final long FILE_NOT_FOUND = 2;
	public static final long INVALID_DATA = 13;
	public static final long READ_FAULT = 30;
	public static final long BAD_FILE_TYPE = 222;
	public static final long INVALID_TIME = 1901;
	
	public static final long INITIAL_VALUES_ONLY = 10531;
	
	public static final long SEVERITY_WARNING = 0x80000000;
	public static final long INVALIDARG = 0x80070057L;
	
	public static final long ATTEMPT_PREPEND = 12800 | SEVERITY_WARNING;
	public static final long ATTEMPT_APPEND = 12801 | SEVERITY_WARNING;
	public static final long ATTEMPT_OVERWRITE = 12802 | SEVERITY_WARNING;
	
	/**
	 * There were missing hours in the imported data that were filled using interpolation.
	 */
	public static final long INTERPOLATE = 12803;
	
	/**
	 * Combined error, interpolated and invalid data.
	 */
	public static final long INTERPOLATE_BEFORE_INVALID_DATA = 12805;
}
