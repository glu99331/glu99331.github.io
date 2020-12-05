/******************************************************************************
 *  Compilation: javac IndexFibonacciMinPQ.java
 *  Execution:
 *  
 *  An index Fibonacci heap.
 *  
 ******************************************************************************/

import java.util.*;
import java.io.*;
import java.util.Map.*;

/*********************************************
 * ========================================= *
 * Author: Gordon Lu                         *
 * Created for: CS 1501 Advanced PQ Project  *
 * ========================================= *
 * 1) Implements PTEs, by frequency of       *
 * accesses for each page.                   *
 * 2) Upon each insertion, swaps minimum PTE *
 * with the index at the end.                *
 * 3) Implements resizing of each array:     *
 * pq, qp, and the pageEntries array are all *
 * resized when the maximum capacity is      *
 * reached.                                  *
 *********************************************/
 

/*
 *  The IndexFibonacciMinPQ class represents an indexed priority queue of generic keys.
 *  It supports the usual insert and delete-the-minimum operations,
 *  along with delete and change-the-key methods. 
 *  In order to let the client refer to keys on the priority queue,
 *  an integer between 0 and N-1 is associated with each key ; the client
 *  uses this integer to specify which key to delete or change.
 *  It also supports methods for peeking at the minimum key,
 *  testing if the priority queue is empty, and iterating through
 *  the keys.
 *  
 *  This implementation uses a Fibonacci heap along with an array to associate
 *  keys with integers in the given range.
 *  The insert, size, is-empty, contains, minimum-index, minimum-key
 *  and key-of take constant time.
 *  The decrease-key operation takes amortized constant time.
 *  The delete, increase-key, delete-the-minimum, change-key take amortized logarithmic time.
 *  Construction takes time proportional to the specified capacity
 *
 */
public class IndexFibonacciMinPQ<Key> implements Iterable<Integer> {
	private Node<Map.Entry<String,PTE>>[] nodes;			//Array of Nodes in the heap
	private Node<Map.Entry<String,PTE>> head;				//Head of the circular root list
	private Node<Map.Entry<String,PTE>> min;				//Minimum Node in the heap
	private int size;					//Number of keys in the heap
	private int n;						//Maximum number of elements in the heap
	private final Comparator<Map.Entry<String,PTE>> comp; //Comparator over the keys
	private HashMap<Integer, Node<Map.Entry<String,PTE>>> table = new HashMap<Integer, Node<Map.Entry<String,PTE>>>(); //Used for the consolidate operation
	
	//Represents a Node of a tree
	private class Node<Key> {
		Map.Entry<String,PTE> key;						//Map.Entry<String,PTE> of the Node
		int order;						//The order of the tree rooted by this Node
		int index;						//Index associated with the key
		Node<Map.Entry<String,PTE>> prev, next;			//siblings of the Node
		Node<Map.Entry<String,PTE>> parent, child;		//parent and child of this Node
		boolean mark;					//Indicates if this Node already lost a child
	}
	
    /**
     * Initializes an empty indexed priority queue with indices between {@code 0} and {@code N-1}
     * Worst case is O(n)
     * @param N number of keys in the priority queue, index from {@code 0} to {@code N-1}
     * @throws java.lang.IllegalArgumentException if {@code N < 0}
     */
	public IndexFibonacciMinPQ(int N) {
		if (N < 0) throw new IllegalArgumentException("Cannot create a priority queue of negative size");
		n = N;
		nodes = (Node<Map.Entry<String,PTE>>[]) new Node[n];
		comp = new MyComparator();
	}
	
    /**
     * Initializes an empty indexed priority queue with indices between {@code 0} and {@code N-1}
     * Worst case is O(n)
     * @param N number of keys in the priority queue, index from {@code 0} to {@code N-1}
     * @param C a Comparator over the keys
     * @throws java.lang.IllegalArgumentException if {@code N < 0}
     */
	public IndexFibonacciMinPQ(Comparator<Map.Entry<String,PTE>> C, int N) {
		if (N < 0) throw new IllegalArgumentException("Cannot create a priority queue of negative size");
		n = N;
		nodes = (Node<Map.Entry<String,PTE>>[]) new Node[n];
		comp = C;
	}

	/**
	 * Whether the priority queue is empty
	 * Worst case is O(1)
	 * @return true if the priority queue is empty, false if not
	 */
	
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Does the priority queue contains the index i ?
	 * Worst case is O(1)
	 * @param i an index
	 * @throws java.lang.IllegalArgumentException if the specified index is invalid
	 * @return true if i is on the priority queue, false if not
	 */
	
	public boolean contains(int i) {
		if (i < 0 || i >= n) throw new IllegalArgumentException();
		else 				 return nodes[i] != null;
	}

	/**
	 * Number of elements currently on the priority queue
	 * Worst case is O(1)
	 * @return the number of elements on the priority queue
	 */
	
	public int size() {
		return size;
	}

	/**
	 * Associates a key with an index
	 * Worst case is O(1)
	 * @param i an index
	 * @param key a Map.Entry<String,PTE> associated with i
	 * @throws java.lang.IllegalArgumentException if the specified index is invalid
	 * @throws java.lang.IllegalArgumentException if the index is already in the queue
	 */
	
	public void insert(int i, Map.Entry<String,PTE> key) {
		if (i < 0 || i >= n) throw new IllegalArgumentException();
		if (contains(i)) throw new IllegalArgumentException("Specified index is already in the queue");
		Node<Map.Entry<String,PTE>> x = new Node<Map.Entry<String,PTE>>();
		x.key = key;
		x.index = i;
		nodes[i] = x;
		size++;
		head = insert(x, head);
		if (min == null) min = head;
		else 			 min = (greater(min.key, key)) ? head : min;
	}

	/**
	 * Get the index associated with the minimum key
	 * Worst case is O(1)
	 * @throws java.util.NoSuchElementException if the priority queue is empty
	 * @return the index associated with the minimum key
	 */
	
	public int minIndex() {
		if (isEmpty()) throw new NoSuchElementException("Priority queue is empty");
		return min.index;
	}

	/**
	 * Get the minimum key currently in the queue
	 * Worst case is O(1)
	 * @throws java.util.NoSuchElementException if the priority queue is empty
	 * @return the minimum key currently in the priority queue
	 */
	
	public Map.Entry<String,PTE> minKey() {
		if (isEmpty()) throw new NoSuchElementException("Priority queue is empty");
		return min.key;
	}

	/**
	 * Delete the minimum key
	 * Worst case is O(log(n)) (amortized)
	 * @throws java.util.NoSuchElementException if the priority queue is empty
	 * @return the index associated with the minimum key
	 */
	
	public int delMin() {
		if (isEmpty()) throw new NoSuchElementException("Priority queue is empty");
		head = cut(min, head);
		Node<Map.Entry<String,PTE>> x = min.child;
		int index = min.index;
		min.key = null;					//For garbage collection
		if (x != null) {
			do {
				x.parent = null;
				x = x.next;
			} while (x != min.child);
			head = meld(head, x);
			min.child = null;			//For garbage collection
		}
		size--;
		if (!isEmpty()) consolidate();
		else 			min = null;
		nodes[index] = null;
		return index;
	}

	/**
	 * Get the key associated with index i
	 * Worst case is O(1)
	 * @param i an index
	 * @throws java.lang.IllegalArgumentException if the specified index is invalid
	 * @throws java.util.NoSuchElementException if the index is not in the queue
	 * @return the key associated with index i
	 */
	
	public Map.Entry<String,PTE> keyOf(int i) {
		if (i < 0 || i >= n) throw new IllegalArgumentException();
		if (!contains(i)) throw new NoSuchElementException("Specified index is not in the queue");
		return nodes[i].key;
	}

	/**
	 * Changes the key associated with index i to the given key
	 * If the given key is greater, Worst case is O(log(n))
	 * If the given key is lower, Worst case is O(1) (amortized)
	 * @param i an index
	 * @param key the key to associate with i
	 * @throws java.lang.IllegalArgumentException if the specified index is invalid
	 * @throws java.util.NoSuchElementException if the index has no key associated with
	 */
	
	public void changeKey(int i, Map.Entry<String,PTE> key) {
		if (i < 0 || i >= n) 		throw new IllegalArgumentException();
		if (!contains(i))			throw new NoSuchElementException("Specified index is not in the queue");
		if (greater(key, nodes[i].key))  increaseKey(i, key);
		else 							 decreaseKey(i, key);
	}

	/**
	 * Decreases the key associated with index i to the given key
	 * Worst case is O(1) (amortized).
	 * @param i an index
	 * @param key the key to associate with i
	 * @throws java.lang.IllegalArgumentException if the specified index is invalid
	 * @throws java.util.NoSuchElementException if the index has no key associated with
	 * @throws java.lang.IllegalArgumentException if the given key is greater than the current key
	 */
	
	public void decreaseKey(int i, Map.Entry<String,PTE> key) {
		if (i < 0 || i >= n) 		throw new IllegalArgumentException();
		if (!contains(i))			throw new NoSuchElementException("Specified index is not in the queue");
		if (greater(key, nodes[i].key))  throw new IllegalArgumentException("Calling with this argument would not decrease the key");
		Node<Map.Entry<String,PTE>> x = nodes[i];
		x.key = key;
		if (greater(min.key, key)) min = x;
		if (x.parent != null && greater(x.parent.key, key)) {
			cut(i);
		}
	}

	/**
	 * Increases the key associated with index i to the given key
	 * Worst case is O(log(n))
	 * @param i an index
	 * @param key the key to associate with i
	 * @throws java.lang.IllegalArgumentException if the specified index is invalid
	 * @throws java.util.NoSuchElementException if the index has no key associated with
	 * @throws java.lang.IllegalArgumentException if the given key is lower than the current key
	 */
	
	public void increaseKey(int i, Map.Entry<String,PTE> key) {
		if (i < 0 || i >= n) 		throw new IllegalArgumentException();
		if (!contains(i))			throw new NoSuchElementException("Specified index is not in the queue");
		if (greater(nodes[i].key, key))  throw new IllegalArgumentException("Calling with this argument would not increase the key");
		delete(i);
		insert(i, key);
	}

	/**
	 * Deletes the key associated the given index
	 * Worst case is O(log(n)) (amortized)
	 * @param i an index
	 * @throws java.lang.IllegalArgumentException if the specified index is invalid
	 * @throws java.util.NoSuchElementException if the given index has no key associated with
	 */
	
	public void delete(int i) {
		if (i < 0 || i >= n) 		throw new IllegalArgumentException();
		if (!contains(i))			throw new NoSuchElementException("Specified index is not in the queue");
		Node<Map.Entry<String,PTE>> x = nodes[i];
		x.key = null;				//For garbage collection
		if (x.parent != null) {
			cut(i);
		}
		head = cut(x, head);
		if (x.child != null) {
			Node<Map.Entry<String,PTE>> child = x.child;
			x.child = null;			//For garbage collection
			x = child;
			do {
				child.parent = null;
				child = child.next;
			} while (child != x);
			head = meld(head, child);
		}
		if (!isEmpty()) consolidate();
		else 			min = null;
		nodes[i] = null;
		size--;
	}
	
	/*************************************
	 * General helper functions
	 ************************************/
	
	//Compares two keys
	private boolean greater(Map.Entry<String,PTE> n, Map.Entry<String,PTE> m) {
		if (n == null) return false;
		if (m == null) return true;
		return comp.compare(n,  m) > 0;
	}
	
	//Assuming root1 holds a greater key than root2, root2 becomes the new root
	private void link(Node<Map.Entry<String,PTE>> root1, Node<Map.Entry<String,PTE>> root2) {
		root1.parent = root2;
		root2.child = insert(root1, root2.child);
		root2.order++;
	}
	
	/*************************************
	 * Function for decreasing a key
	 ************************************/
	
	//Removes a Node from its parent's child list and insert it in the root list
	//If the parent Node already lost a child, reshapes the heap accordingly
	private void cut(int i) {
		Node<Map.Entry<String,PTE>> x = nodes[i];
		Node<Map.Entry<String,PTE>> parent = x.parent;
		parent.child = cut(x, parent.child);
		x.parent = null;
		parent.order--;
		head = insert(x, head);
		parent.mark = !parent.mark;
		if (!parent.mark && parent.parent != null) {
			cut(parent.index);}
	}
	
	/*************************************
	 * Function for consolidating all trees in the root list
	 ************************************/
	
	//Coalesces the roots, thus reshapes the heap
	//Caching a HashMap improves greatly performances
	private void consolidate() {
		table.clear();
		Node<Map.Entry<String,PTE>> x = head;
		int maxOrder = 0;
		min = head;
		Node<Map.Entry<String,PTE>> y = null, z = null;
		do {
			y = x;
			x = x.next;
			z = table.get(y.order);
			while (z != null) {
				table.remove(y.order);
				if (greater(y.key, z.key)) {
					link(y, z);
					y = z;
				} else {
					link(z, y);
				}
				z = table.get(y.order);
			}
			table.put(y.order, y);
			if (y.order > maxOrder) maxOrder = y.order;
		} while (x != head);
		head = null;
		for (Node<Map.Entry<String,PTE>> n : table.values()) {
			min = greater(min.key, n.key) ? n : min;
			head = insert(n, head);
		}
	}
	
	/*************************************
	 * General helper functions for manipulating circular lists
	 ************************************/
	
	//Inserts a Node in a circular list containing head, returns a new head
	private Node<Map.Entry<String,PTE>> insert(Node<Map.Entry<String,PTE>> x, Node<Map.Entry<String,PTE>> head) {
		if (head == null) {
			x.prev = x;
			x.next = x;
		} else {
			head.prev.next = x;
			x.next = head;
			x.prev = head.prev;
			head.prev = x;
		}
		return x;
	}
	
	//Removes a tree from the list defined by the head pointer
	private Node<Map.Entry<String,PTE>> cut(Node<Map.Entry<String,PTE>> x, Node<Map.Entry<String,PTE>> head) {
		if (x.next == x) {
			x.next = null;
			x.prev = null;
			return null;
		} else {
			x.next.prev = x.prev;
			x.prev.next = x.next;
			Node<Map.Entry<String,PTE>> res = x.next;
			x.next = null;
			x.prev = null;
			if (head == x)  return res;
			else 			return head;
		}
	}
	
	//Merges two lists together.
	private Node<Map.Entry<String,PTE>> meld(Node<Map.Entry<String,PTE>> x, Node<Map.Entry<String,PTE>> y) {
		if (x == null) return y;
		if (y == null) return x;
		x.prev.next = y.next;
		y.next.prev = x.prev;
		x.prev = y;
		y.next = x;
		return x;
	}
	
	/*************************************
	 * Iterator
	 ************************************/
	
	/**
	 * Get an Iterator over the indexes in the priority queue in ascending order
	 * The Iterator does not implement the remove() method
	 * iterator() : Worst case is O(n)
	 * next() : 	Worst case is O(log(n)) (amortized)
	 * hasNext() : 	Worst case is O(1)
	 * @return an Iterator over the indexes in the priority queue in ascending order
	 */
	
	public Iterator<Integer> iterator() {
		return new MyIterator();
	}
	
	private class MyIterator implements Iterator<Integer> {
		private IndexFibonacciMinPQ<Map.Entry<String,PTE>> copy;
		
		
		//Constructor takes linear time
		public MyIterator() {
			copy = new IndexFibonacciMinPQ<Map.Entry<String,PTE>>(comp, n);
			for (Node<Map.Entry<String,PTE>> x : nodes) {
				if (x != null) copy.insert(x.index, x.key);
			}
		}
		
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
		public boolean hasNext() {
			return !copy.isEmpty();
		}
		
		//Takes amortized logarithmic time
		public Integer next() {
			if (!hasNext()) throw new NoSuchElementException();
			return copy.delMin();
		}
	}
	
	/***************************
	 * Comparator
	 **************************/
	
	//default Comparator
	private class MyComparator implements Comparator<Map.Entry<String,PTE>> {
		@Override
		public int compare(Map.Entry<String,PTE> key1, Map.Entry<String,PTE> key2) {
			return key1.getValue().compareTo(key2.getValue());
		}
	}
	
}

