public class LList implements PriorityQueue {
    private Node head = null;
    private int size = 0;
    private static final int MAX_SIZE = 100000;

    public LList() {

    }

    @Override
    public int peek() {
        return head.data;
    }

    @Override
    public void insert(int item) {
        if (size >= MAX_SIZE) return;
        size++;
        if (head == null) {
            head = new Node(item);
        }
        Node n = new Node(item);
        Node walk = head, prev = null;
        while (walk != null) {
            if (walk.data >= item) {
                if (prev == null) {
                    prev = n;
                    prev.next = head;
                    head = prev;
                    return;
                }
                prev.next = n;
                n.next = walk;
                return;
            } else if (walk.next == null) {
                walk.next = n;
                return;
            }
            prev = walk;
            walk = walk.next;
        }
    }


    @Override
    public int deleteMin() {
        int val = head.data;
        head = head.next;
        size--;
        return val;
    }

    @Override
    public int size() {
        return size;
    }

}
