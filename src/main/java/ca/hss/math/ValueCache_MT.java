/**
 * ValueCache_MT.java
 *
 * Copyright 2022 Heartland Software Solutions Inc.
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

public class ValueCache_MT<T1,T2> extends ValueCache<T1,T2> {
	public ValueCache_MT(int numEntries) {
		super(numEntries);
	}

	@Override
	public synchronized void clear() {
		super.clear();
	}

	@Override
	public synchronized void put(T1 key, T2 value) {
		super.put(key, value);
	}

	@Override
	public synchronized T2 get(T1 key) {
		return super.get(key);
	}
}
