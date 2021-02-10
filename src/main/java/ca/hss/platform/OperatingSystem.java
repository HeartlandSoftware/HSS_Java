/**
 * OperatingSystem.java
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

package ca.hss.platform;

import java.util.Locale;

public abstract class OperatingSystem {
	
	private static OperatingSystem _internal;
	
	public abstract Type getType();
	
	/**
	 * Get a process identifier for an executing process.
	 * @param process The process to retrieve the PID for.
	 * @return The PID of the requested process, negative if the result is the Windows handle, or null if nothing was found.
	 */
	public abstract Long getPid(Process process);
	
	public static synchronized OperatingSystem getOperatingSystem() {
		if (_internal == null) {
			String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
			if (os.contains("mac") || os.contains("darwin"))
				_internal = new Mac();
			else if (os.contains("win"))
				_internal = new Windows();
			else if (os.contains("nux"))
				_internal = new Linux();
			else
				_internal = new Unsupported();
		}
		
		return _internal;
	}
	
	public enum Type {
		Unknown,
		Windows,
		Mac,
		Linux
	}
}
