public class MinHeap implements PriorityQueue {

    private int[] arr;
    private static final int MAX_SIZE = 1000000;
    private int size;

    public MinHeap() {
        arr = new int[MAX_SIZE];
        size = 0;
    }


    @Override
    public int peek() {
        return arr[0];
    }

    @Override
    public void insert(int item) {
       if (size < MAX_SIZE) {
           arr[size] = item;
           heapUp(size);
           size++;
       }
    }

    @Override
    public int deleteMin() {
        int min = arr[0];
        arr[0] = arr[size-1];
        arr[size-1] = 0;
        size--;
        heapDown(0);
        return min;
    }

    private static int _par(int n) {
        return (n-1)/2;
    }

    private static int _left(int n) {
        return n == 0 ? 1 : 2 * n;
    }

    private static int _right(int n) {
        return n == 0 ? 2 : 2 * n + 1;
    }

    private void swap(int a, int b) {
        arr[a] ^= arr[b];
        arr[b] ^= arr[a];
        arr[a] ^= arr[b];
    }

    private void heapDown(int pos) {
        int swapPos;
        boolean flag = false;
        if (pos > size) return;
        if (_right(pos) < size) {
            swapPos = arr[_left(pos)] > arr[_right(pos)] ? _right(pos) : _left(pos);
            flag = true;
        }
        else {
            swapPos = _left(pos);
            flag = true;
        }
        if (_left(pos) > size)
            return;
        if ((arr[pos] > arr[_left(pos)] || arr[pos] > arr[_right(pos)]) && flag) { //
            if (arr[swapPos] == 0)
                return;
            swap(pos,swapPos);
            heapDown(swapPos);
        }
    }

    @Override
    public int size() {
        return size;
    }

    private void heapUp(int pos) {
        while (pos > 0) {
            if (arr[pos] < arr[(pos-1)/2]) {
                int hold = arr[pos];
                arr[pos] = arr[(pos-1)/2];
                arr[(pos-1)/2] = hold;
                pos = (pos-1)/2;
            }
            else {
                break;
            }
        }
    }


}
