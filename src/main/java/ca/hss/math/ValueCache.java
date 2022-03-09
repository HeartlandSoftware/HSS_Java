/**
 * ValueCache.java
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

public class ValueCache<T1,T2> {
	int		m_numEntries;
	long	m_call;

	static class Entry {
		long	m_call = -1;
		Object	m_key;
		Object	m_value;
	};

	Entry [] entries = null;

	public ValueCache(int numEntries) {
		m_numEntries = numEntries;
		entries = new Entry[m_numEntries];
		for (int i = 0; i < m_numEntries; i++)
			entries[i] = new Entry();
	}

	public void clear() {
		for (int i = 0; i < m_numEntries; i++)
			entries[i].m_call = -1;
	}

	public void put(T1 key, T2 value) {
		if (m_numEntries == 0)
			return;
		m_call++;
		if (m_call < 0) {
			clear();
			m_call++;
		}

		int i, oldest = 0;
		for (i = 0; i < m_numEntries; i++) {
			if (entries[i].m_call == -1)
				break;
			if (entries[i].m_call < entries[oldest].m_call)
				oldest = i;
			if (entries[i].m_key.equals(key)) {
				entries[i].m_call = m_call;
				return;
			}
		}
		if (i == m_numEntries)
			i = oldest;

		entries[i].m_call = m_call;
		entries[i].m_key = key;
		entries[i].m_value = value;
	}

	@SuppressWarnings("unchecked")
	public T2 get(T1 key) {
		if (m_numEntries == 0)
			return null;

		for (int i = 0; i < m_numEntries; i++) {
			if (entries[i].m_call == -1)
				return null;
			if (entries[i].m_key.equals(key)) {
				m_call++;
				if (m_call < 0) {
					clear();
					m_call++;
				}
				entries[i].m_call = m_call;
				return (T2)entries[i].m_value;
			}
		}
		return null;
	}
}
