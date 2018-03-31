//
// ******************PUBLIC OPERATIONS*********************
// void insert( x ) --> Insert x
// Comparable deleteMin( )--> Return and remove smallest item
// Comparable findMin( ) --> Return smallest item
// boolean isEmpty( ) --> Return true if empty; else false
// ******************ERRORS********************************
// Throws RuntimeException for findMin and deleteMin when empty

package org.insa.algo.utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Implements a binary heap. Note that all "matching" is based on the compareTo
 * method.
 * 
 * @author Mark Allen Weiss
 * @author DLB
 */
public class BinaryHeap<E extends Comparable<E>> implements PriorityQueue<E> {

   // Number of elements in heap.
   private int currentSize;

   // The heap array.
   private final ArrayList<E> array;

   // A map to allow getting any element index in O(1) time
   private HashMap<E, Integer> indexesMap;
   /**
    * Construct a new empty binary heap.
    */
   public BinaryHeap() {
      this.currentSize = 0;
      this.array = new ArrayList<E>();
      this.indexesMap = new HashMap<E, Integer>();
   }

   /**
    * Construct a copy of the given heap.
    * 
    * @param heap
    *           Binary heap to copy.
    */
   public BinaryHeap(BinaryHeap<E> heap) {
      this.currentSize = heap.currentSize;
      this.array = new ArrayList<E>(heap.array);
      this.indexesMap = new HashMap<E, Integer>(heap.indexesMap);
   }

   /**
    * Set an element at the given index.
    * 
    * @param index
    *           Index at which the element should be set.
    * @param value
    *           Element to set.
    */
   private void arraySet(int index, E value) {
      if (index == this.array.size()) {
         this.array.add(value);
      } else {
         this.array.set(index, value);
      }
      indexesMap.put(value, index);
   }

   /**
    * @return Index of the parent of the given index.
    */
   private int index_parent(int index) {
      return (index - 1) / 2;
   }

   /**
    * @return Index of the left child of the given index.
    */
   private int index_left(int index) {
      return index * 2 + 1;
   }

   /**
    * Internal method to percolate up in the heap.
    * 
    * @param index
    *           Index at which the percolate begins.
    */
   private void percolateUp(int index) {
      E x = this.array.get(index);

      for (; index > 0 && x.compareTo(
            this.array.get(index_parent(index))) < 0; index = index_parent(
                  index)) {
         E moving_val = this.array.get(index_parent(index));
         this.arraySet(index, moving_val);
      }

      this.arraySet(index, x);
   }

   /**
    * Internal method to percolate down in the heap.
    * 
    * @param index
    *           Index at which the percolate begins.
    */
   private void percolateDown(int index) {
      int ileft = index_left(index);
      int iright = ileft + 1;

      if (ileft < this.currentSize) {
         E current = this.array.get(index);
         E left = this.array.get(ileft);
         boolean hasRight = iright < this.currentSize;
         E right = (hasRight) ? this.array.get(iright) : null;

         if (!hasRight || left.compareTo(right) < 0) {
            // Left is smaller
            if (left.compareTo(current) < 0) {
               this.arraySet(index, left);
               this.arraySet(ileft, current);
               this.percolateDown(ileft);
            }
         } else {
            // Right is smaller
            if (right.compareTo(current) < 0) {
               this.arraySet(index, right);
               this.arraySet(iright, current);
               this.percolateDown(iright);
            }
         }
      }
   }

   @Override
   public boolean isEmpty() {
      return this.currentSize == 0;
   }

   @Override
   public int size() {
      return this.currentSize;
   }

   @Override
   public void insert(E x) {
      int index = this.currentSize++;
      this.arraySet(index, x);
      this.percolateUp(index);
   }

   /*
    * private void heapify_down(int index) { int rightIndex = 2*index + 2; int
    * leftIndex = 2*index + 1; int minIndex = index; if(leftIndex < currentSize
    * - 1 && (array.get(leftIndex).compareTo(array.get(index))) < 0) { minIndex
    * = leftIndex; } if(rightIndex < currentSize - 1 &&
    * (array.get(rightIndex).compareTo(array.get(index))) < 0) { minIndex =
    * rightIndex; } if(minIndex != index) { //we have to swap some elements
    * 
    * } }
    */

   @Override
   public void remove(E x) throws ElementNotFoundException {
      // we first find the element x
      // (we can actually directly search the array)
     // System.out.println("want to remove : " + x);
     // System.out.println("before :");
     // printSorted();
      if (currentSize == 0) {
         throw new ElementNotFoundException(x);
      }
      /*boolean found = false;
      int currentIndex = 0;
      int testEqu;
      while (currentIndex < currentSize && !found) {
         testEqu = array.get(currentIndex).compareTo(x);
         if (testEqu == 0) {
            found = true;
            //System.out.println(("found" + x));
         }
         else {
            currentIndex++;
         }
      }*/
      Integer currentIndex = indexesMap.get(x);
      if (currentIndex == null) {
         throw new ElementNotFoundException(x);
      }
      // System.out.println("current size = " + currentSize + "(array size = " + array.size() + "), found index is :" + currentIndex );
      if (currentIndex == currentSize - 1) {
         currentSize--;
         array.remove(currentSize);
         this.indexesMap.remove(x);
      } else {
         //System.out.println("current size = " + currentSize + ", found index is :" + currentIndex );
         // now we "remove" x from the heap and replace it with the rightmost
         // leaf
         currentSize--;
         array.set(currentIndex, array.get(currentSize));
         this.indexesMap.remove(x);
         this.indexesMap.remove(array.get(currentSize));
         this.indexesMap.put(array.get(currentSize), currentIndex);
         array.remove(currentSize);
         percolateUp(currentIndex);
         percolateDown(currentIndex);
      }
     // System.out.println("after :");
     // printSorted();

   }

   @Override
   public E findMin() throws EmptyPriorityQueueException {
      if (isEmpty())
         throw new EmptyPriorityQueueException();
      return this.array.get(0);
   }

   @Override
   public E deleteMin() throws EmptyPriorityQueueException {
      E minItem = findMin();
      E lastItem = this.array.get(--this.currentSize);
      this.arraySet(0, lastItem);
      this.percolateDown(0);
      this.indexesMap.remove(minItem);
      return minItem;
   }

   /**
    * Prints the heap
    */
   public void print() {
      System.out.println();
      System.out.println(
            "========  HEAP  (size = " + this.currentSize + ")  ========");
      System.out.println();

      for (int i = 0; i < this.currentSize; i++) {
         System.out.println(this.array.get(i).toString());
      }

      System.out.println();
      System.out.println("--------  End of heap  --------");
      System.out.println();
   }

   /**
    * Prints the elements of the heap according to their respective order.
    */
   public void printSorted() {

      BinaryHeap<E> copy = new BinaryHeap<E>(this);

      System.out.println();
      System.out.println("========  Sorted HEAP  (size = " + this.currentSize
            + ")  ========");
      System.out.println();

      while (!copy.isEmpty()) {
         System.out.println(copy.deleteMin());
      }

      System.out.println();
      System.out.println("--------  End of heap  --------");
      System.out.println();
   }

}
