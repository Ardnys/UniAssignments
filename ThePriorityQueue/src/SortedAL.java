import java.util.ArrayList;

public class SortedAL implements PriorityQueue {
    private ArrayList<Integer> arr;
    private int size = 0;

    public SortedAL() {
        arr = new ArrayList<>(100000);
    }

    @Override
    public int peek() {
        return arr.get(0);
    }

    @Override
    public void insert(int item) {
        for (int i = 0; i < size; i++) {
            if (arr.get(i) > item) {
                arr.add(i, item);
                size++;
                return;
            }
        }
        arr.add(size,item);
        size++;
    }

    @Override
    public int deleteMin() {
        size--;
        return arr.remove(0);
    }

    @Override
    public int size() {
        return size;
    }
}
