package disjointsets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;
    private Map<T, Integer> itemToIndex;
    private int openIndex;

    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */

    public UnionBySizeCompressingDisjointSets() {
        pointers = new ArrayList<>();
        itemToIndex = new HashMap<>();
        openIndex = 0;
    }

    @Override
    public void makeSet(T item) {
        if (itemToIndex.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        itemToIndex.put(item, openIndex);
        pointers.add(-1);
        openIndex++;
    }

    @Override
    public int findSet(T item) {
        if (!itemToIndex.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        int index = itemToIndex.get(item); // index of the current item in DisjointSet (pointers)
        return findSet(index);
        // this returns index of parent - I think that's what we want?
    }

    private int findSet(int index) {
        if (pointers.get(index) < 0) { // if we hit parent/root
            return index;
        }
        // otherwise compress and continue up the set
        int root = findSet(pointers.get(index));
        pointers.set(index, root);
        return root;
    }

    @Override
    public boolean union(T item1, T item2) {
        if (!itemToIndex.containsKey(item1) || !itemToIndex.containsKey(item2)) {
            throw new IllegalArgumentException();
        }
        int root1 = findSet(item1);
        int root2 = findSet(item2);

        if (root1 == root2) {
            return false; // they're in the same set
        }

        int size1 = pointers.get(root1); // these are negative
        int size2 = pointers.get(root2);

        // if size 1 has the bigger weight
        if (size1 <= size2) {
            pointers.set(root1, size1 + size2);
            pointers.set(root2, root1);
        } else {
            pointers.set(root2, size1 + size2);
            pointers.set(root1, root2);
        }
        return true;
    }
}
