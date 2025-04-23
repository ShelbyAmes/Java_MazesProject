package priorityqueues;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.HashMap;
import java.util.Map;

/**
 * @see ExtrinsicMinPQ
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 0;
    List<PriorityNode<T>> items;
    int size;
    Map<T, Integer> itemIndex;

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        size = 0;
        itemIndex = new HashMap<>();
    }

    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.
    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        PriorityNode<T> old = items.get(a);
        items.set(a, items.get(b));
        items.set(b, old);

        itemIndex.put(items.get(a).getItem(), a);
        itemIndex.put(items.get(b).getItem(), b);
    }

    @Override
    public void add(T item, double priority) {

        if (contains(item) || item == null) {
            throw new IllegalArgumentException();
        }
        items.add(new PriorityNode<>(item, priority));

        int indexNew = size;
        int index = items.size() -1;
        itemIndex.put(item, index); //did index instead of indexNew
        size++;
        percolateUp(priority, indexNew);
    }

    public void percolateUp(double priority, int indexNew) {

        if (indexNew != 0 && priority < (items.get(((indexNew) - 1) / 2)).getPriority()) {
            swap(indexNew, (indexNew - 1) / 2);
            percolateUp(priority, (indexNew - 1) / 2);
        }
    }

    @Override
    public boolean contains(T item) {
        return itemIndex.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (size() == 0) {
            throw new NoSuchElementException("PQ is empty");
        }
        return items.get(0).getItem();
    }

    @Override
    public T removeMin() {
        if (size == 0) {
            throw new NoSuchElementException("PQ is empty");
        }

        T oldItem = items.get(0).getItem();
        itemIndex.remove(oldItem);


        if (size == 1) {
            items.remove(0);
            size--;
            return oldItem;
        }


        items.set(0, items.get(size - 1));
        items.remove(size - 1);
        size--;
        percolateDown(0);

        return oldItem;
    }

    public void percolateDown(int index) {

        if (size > 2 * index + 1 && items.get(2 * index + 1) != null) { // check if there is a left child
            int smallestChild = 2 * index + 1; // make the smallest the left child
            if (size > 2 * index + 2 && items.get(2 * index + 2) != null) {
                // if there's a right child, and it's smaller than left, it becomes the smallest
                if (items.get(2 * index + 2).getPriority() <= items.get(2 * index + 1).getPriority()) {
                    smallestChild = 2 * index + 2;
                }
            }
            // if the current is smaller than the child, swap them and continue percolating
            if (items.get(index).getPriority() >= items.get(smallestChild).getPriority()) {
                swap(smallestChild, index);
                percolateDown(smallestChild);
            }
        }
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!itemIndex.containsKey(item)) {
            throw new NoSuchElementException();
        }
        int index = itemIndex.get(item);
        double old = items.get(index).getPriority();
        items.get(index).setPriority(priority);
        if (priority < old) {
            percolateUp(priority, index);
        } else {
            percolateDown(index);
        }
    }

    @Override
    public int size() {
        return size;
    }
}
