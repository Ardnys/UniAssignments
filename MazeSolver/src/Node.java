public class Node<T> {
    public Node<T> next;
    public T data;

    public Node() {
        data = null;
        next = null;
    }
    public Node(T data) {
        this.data = data;
        next = null;
    }
}
