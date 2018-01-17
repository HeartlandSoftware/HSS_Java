/**
 * LinkedListNode.java
 *
 * Copyright 2015-2018 Heartland Software Solutions Inc.
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

/**
 * An abstract class for use with {@link ca.hss.math.LinkedList}.
 *
 */
public abstract class LinkedListNode {
	LinkedListNode ln_Succ = null;
	LinkedListNode ln_Pred = null;
	
	/**
	 * Get the next item in the linked list.
	 * 
	 * @return the next item in the list.
	 */
	public LinkedListNode getNext() { return ln_Succ; }
	
	/**
	 * Get the previous item in the linked list.
	 * 
	 * @return the previous item in the list.
	 */
	public LinkedListNode getPrevious() { return ln_Pred; }
}
