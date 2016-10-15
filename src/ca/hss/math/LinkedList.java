/**
 * LinkedList.java
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

package ca.hss.math;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

/**
 * Doubly-linked list. Doesn't directly implement {@link java.util.List} so that indices
 * into the list may be 64 bit integers instead of 32 bit.
 * 
 * The generic type that is stored in this linked list is required to extend the {@link ca.hss.math.LinkedListNode}. This
 * allows nodes within the list to iterate the list themselves. This differs from {@link java.util.LinkedList} where
 * nodes have no knowledge of one another.
 * 
 * <strong>Note that this implementation is not synchronized.</strong> If multiple threads access a linked list concurrently,
 * and at least one of the threads modifies the list structurally, it <em>must</em> by synchronized externally. (A structural
 * modification is any operation that adds or deletes one or more elements; merely setting the value of an element is not a
 * structural modification).
 * 
 * The iterators returned by this class's iterator and descendingIterator methods are <em>fail-fast</em>, if the list is
 * structurally modified at any time after the iterator is created, in any way except through the iterator's own <code>remove</code>
 * method, the iterator will throw a {@link java.util.ConcurrentModificationException}. Thus, in the face of concurrent
 * modification, the iterator fails quickly and cleanly, rather than risking arbitrary, non-deterministic behaviour at an
 * undetermined time in the future.
 * 
 * Note that the fail-fast behaviour of an iterator cannot be guaranteed as it is, generally speaking, impossible to make
 * any hard guarantees in the presence of unsynchronized concurrent modifications. Fail-fast iterators throw
 * <code>ConcurrentModificationException</code> on a best-effort basis. Therefore, it would be wrong to write a program
 * that depended on this exception for its correctness: <em>the fail-fast behaviour of iterators should be used only to
 * detect bugs.</em>
 * 
 * @param <Item> the type of the elements held in this collection. They must extend {@link ca.hss.math.LinkedListNode} and
 * must have a default constructor.
 */
public class LinkedList<Item extends LinkedListNode> implements Iterable<Item> {
	protected Item head;
	protected Item tail;
	protected long count;
	private Class<Item> clazz;
	private long modCount = 0;
	
	/**
	 * Create a new templated MinList. Due to restrictions in Java generics the class
	 * type must be specified here.
	 * 
	 * @param clazz A class object for <code>Item</code>.
	 * @throws IllegalArgumentException Thrown if <code>clazz</code> is null.
	 */
	public LinkedList(Class<Item> clazz) throws IllegalArgumentException {
		if (clazz == null)
			throw new IllegalArgumentException();
		head = null;
		tail = null;
		count = 0;
		this.clazz = clazz;
	}

	/**
	 * Returns <code>true</code> if this list contains no elements.
	 * 
	 * @return <code>true</code> if this list contains no elements.
	 */
    public boolean isEmpty() {
    	return (count == 0);
    }
    
    /**
     * Returns the number of elements in this list.
     * 
     * @return the number of elements in this list.
     */
    public long size() {
    	return count;
    }
    
    /**
     * Creates a new instance of the stored node type.
     * 
     * @return a new instance of type <code>Item</code>.
     */
    public Item newObject() {
		Item node = null;
		try {
			node = clazz.newInstance();
		}
		catch (Exception ex) { }
		return node;
	}

    /**
     * Returns the first element in this list. If the list is empty a new element is added to this list.
     * 
     * @return the first element in this list.
     */
    public Item getFirst() {
    	if (head == null) {
    		Item node = newObject();
    		node.ln_Pred = newObject();
    		node.ln_Pred.ln_Succ = node;
    		return node;
    	}
    	return head;
    }

    /**
     * Returns the last element in this list. If the list is empty a new element is added to this list.
     * 
     * @return the last element in this list.
     */
	public Item getLast() {
		if (tail == null) {
			Item node = newObject();
			node.ln_Succ = newObject();
			node.ln_Succ.ln_Pred = node;
			return node;
		}
		return tail;
	}

	/**
	 * Inserts the specified element at the beginning of this list.
	 * 
	 * @param Node the element to add.
	 */
	@SuppressWarnings("unchecked")
	public void addFirst(Item Node) {
		if (head == null)
			addAfter(null, Node);
		else
			addAfter((Item)head.ln_Pred, Node);
	}
	
	/**
	 * Appends the specified element to the end of this list.
	 * 
	 * This method is equivalent to {@link ca.hss.math.LinkedList#addLast(Item)}.
	 * 
	 * @param node element to be appended to this list.
	 */
	public void add(Item node) {
		addLast(node);
	}
	
	/**
	 * Inserts the specified element to the end of this list.
	 * 
	 * This method is equivalent to {@link ca.hss.math.LinkedList#add(Item)}.
	 * 
	 * @param Node the element to add.
	 */
	public void addLast(Item Node) {
		addAfter(tail, Node);
	}
	
	/**
	 * Returns the index of the first occurrence of the specified element in this list, or -1
	 * if this list does not contain the element. More formally, returns the lowest index <code>i</code>
	 * such that <code>(Node==null ? get(i)==null : Node.equals(get(i)))</code> or -1 if there
	 * is no such index.
	 * 
	 * @param Node element to search for.
	 * @return the index of the first occurrence of the specified element in this list, or -1 if this
	 * list does not contain the element.
	 */
	@SuppressWarnings("unchecked")
	public long indexOf(Item Node) {
		long cnt = 0;
		Item node = head;
		while (node.ln_Succ != null) {	
			if (node == Node)
				return cnt;
			node = (Item)node.ln_Succ;
			cnt++;
		}
		return -1L;
	}
	
	/**
	 * Returns the element at the specified position in this list.
	 * 
	 * @param index index of the element to return.
	 * @return the element at the specified position in this list.
	 */
	@SuppressWarnings("unchecked")
	public Item get(long index) {
		Item node;
		if (count == 0)
			return null;
		if (index == 0)
			return head;
		if (index <= (count >> 1)) {
			node = head;
			while (index != 0) {
				index--;
				node = (Item)node.ln_Succ;
			}
			return node;					
		}
		else {
			if (index >= count)
				return null;
			index = (count - index - 1);
			node = tail;
			while (index != 0) {
				index--;
				node = (Item)node.ln_Pred;
			}
			return node;
		}
	}
	
	/**
	 * Inserts the specified element at the specified position in this list. Shifts the element
	 * currently at that position (if any) and any subsequent elements to the right (adds one to
	 * their indices).
	 * 
	 * @param index index at which the specified element is to be inserted.
	 * @param New element to be inserted.
	 */
	@SuppressWarnings("unchecked")
	public void add(long index, Item New) {
		if (index >= size())
			addLast(New);
		else if (index == 0 || isEmpty())
			addFirst(New);
		else {
			Item mn = get(index);
			addAfter((Item)mn.getPrevious(), New);
		}
	}

	/**
	 * Adds the specified element after the other specified element. The element <code>after</code>
	 * is assumed to be in the list, if it is not undefined behaviour may occur. Shifts the element
	 * currently at that position (if any) and any subsequent element to the right (adds one to
	 * their indices).
	 * 
	 * @param after the element to add the new element after.
	 * @param item element to be inserted.
	 */
	public void addAfter(Item after, Item item) {
		if (head == null) {
			head = item;
			head.ln_Pred = newObject();
			head.ln_Pred.ln_Succ = head;
			head.ln_Succ = newObject();
			head.ln_Succ.ln_Pred = head;
			tail = head;
			count = 1;
		}
		else {
			after.ln_Succ.ln_Pred = item;
			item.ln_Succ = after.ln_Succ;
			item.ln_Pred = after;
			after.ln_Succ = item;
			if (item.ln_Succ.ln_Succ == null)
				tail = item;
			if (item.ln_Pred.ln_Pred == null)
				head = item;
			count++;
		}
		modCount++;
	}

	/**
	 * Removes and returns the first element from this list.
	 * 
	 * @return the first element from this list.
	 */
	@SuppressWarnings("unchecked")
	public Item removeFirst() {
		if (isEmpty()) 
			return null;
		if (count == 1) {
			Item node = head;
			head = tail = null;
			count = 0;
			return node;
		}
		Item node = head;
		node.ln_Succ.ln_Pred = node.ln_Pred;
		head = (Item)node.ln_Succ;
		count--;
		modCount++;
		return node;
	}
	
	/**
	 * Removes and returns the last element from this list.
	 * 
	 * @return the last element from this list.
	 */
	@SuppressWarnings("unchecked")
	public Item removeLast() {
		if (isEmpty())
			return null;
		if (count == 1) {
			Item node = head;
			head = tail = null;
			count = 0;
			return node;
		}
		Item node = tail;
		node.ln_Pred.ln_Succ = node.ln_Succ;
		tail = (Item)node.ln_Pred;
		count--;
		modCount++;
		return node;
	}
	
	/**
	 * Removes the specified element from this list. Shifts any subsequent elements
	 * to the left (subtracts one from their indices). Returns the element that was
	 * removed from the list.
	 * 
	 * @param item the index of the element to be removed.
	 */
	public void remove(Item item) {
		if (isEmpty())
			return;
		if (count == 1) {
			head = tail = null;
			count = 0;
			modCount++;
			return;
		}
		if (item.ln_Pred.ln_Pred == null)
			removeFirst();
		else if (item.ln_Succ.ln_Succ == null)
			removeLast();
		else {
			item.ln_Pred.ln_Succ = item.ln_Succ;
			item.ln_Succ.ln_Pred = item.ln_Pred;
			count--;
			modCount++;
		}
	}
	
	/**
	 * Replaces the specified element with another.
	 * 
	 * @param oldItem the element to be replaced
	 * @param newItem the new element.
	 */
	public void replace(Item oldItem, Item newItem) {
		if (count == 1) {
			head = newItem;
			tail = head;
		}
		else if (oldItem.ln_Pred == null) {
			newItem.ln_Succ = oldItem.ln_Succ;
			newItem.ln_Succ.ln_Pred = newItem;
			newItem.ln_Pred = null;
			head = newItem;
		}
		else if (oldItem.ln_Succ == null) {
			newItem.ln_Pred = oldItem.ln_Pred;
			newItem.ln_Pred.ln_Succ = newItem;
			newItem.ln_Succ = null;
			tail = newItem;
		}
		else {
			newItem.ln_Succ = oldItem.ln_Succ;
			newItem.ln_Succ.ln_Pred = newItem;
			newItem.ln_Pred = oldItem.ln_Pred;
			newItem.ln_Pred.ln_Succ = newItem;
		}
	}
	
	/**
	 * Removes all of the elements from this list. The list will be empty after this call returns.
	 */
	public void clear() {
		while (size() > 0) {
			removeFirst();
		}
		modCount++;
	}

	/**
	 * Returns an iterator of the elements in this list (in proper sequence).
	 * 
	 * The iterator is <em>fail-fast</em> if the list is structurally modified
	 * at any time after the iterator is created, in any way except through the
	 * iterator's own remove method, the iterator will throw a
	 * <code>ConcurrentModificationException</code>. Thus, in the face of
	 * concurrent modification, the iterator fails quickly and cleanly, rather
	 * than risking arbitrary, non-deterministic behaviour at an undetermined
	 * time in the future.
	 * 
	 * @return an Iterator of the elements in this list.
	 */
	@Override
	public Iterator<Item> iterator() {
		return new LinkedListIterator();
	}
	
	/**
	 * Returns an iterator over the elements in this list in reverse sequential order.
	 * The elements will be returned in order from last (tail) to first (head).
	 * 
	 * The iterator is <em>fail-fast</em> if the list is structurally modified
	 * at any time after the iterator is created, in any way except through the
	 * iterator's own remove method, the iterator will throw a
	 * <code>ConcurrentModificationException</code>. Thus, in the face of
	 * concurrent modification, the iterator fails quickly and cleanly, rather
	 * than risking arbitrary, non-deterministic behaviour at an undetermined
	 * time in the future.
	 * 
	 * @return an iterator over the elements in this list in reverse sequence.
	 */
	public Iterator<Item> descendingIterator() {
		return new ReverseLinkedListIterator();
	}
	
	private class LinkedListIterator implements Iterator<Item> {
		private Item current = null;
		private Item next = null;
		private long expectedModCount = modCount;
		
		public LinkedListIterator() {
			next = getFirst();
		}

		@Override
		public boolean hasNext() {
			return next != null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Item next() {
			checkForComodification();
			current = next;
			next = (Item)next.getNext();
			return current;
		}

		@Override
		public void remove() {
			checkForComodification();
			if (current == null)
				throw new IllegalStateException("next has not yet been called.");
			LinkedList.this.remove(current);
			current = null;
			expectedModCount++;
		}
		
		private void checkForComodification() {
			if (modCount != expectedModCount)
				throw new ConcurrentModificationException();
		}
	}
	
	private class ReverseLinkedListIterator implements Iterator<Item> {
		private Item current = null;
		private Item prev = null;
		private long expectedModCount = modCount;
		
		public ReverseLinkedListIterator() {
			prev = getLast();
		}

		@Override
		public boolean hasNext() {
			return prev != null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Item next() {
			checkForComodification();
			current = prev;
			prev = (Item)prev.getPrevious();
			return current;
		}

		@Override
		public void remove() {
			checkForComodification();
			if (current == null)
				throw new IllegalStateException("next has not yet been called.");
			LinkedList.this.remove(current);
			current = null;
		}
		
		private void checkForComodification() {
			if (modCount != expectedModCount)
				throw new ConcurrentModificationException();
		}
	}
}
