public class myStack<T> implements Stack<T> {
    private Node<T> top;

    public myStack() {
        top = null;
    }

    @Override
    public boolean isEmpty() {
        return top == null;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public void push(T item) {
        Node<T> n = new Node<>(item);
        n.next = top;
        top = n;
    }

    @Override
    public T pop() {
        if (isEmpty())
            throw new RuntimeException("Empty stack");
        T item = top.data;
        top = top.next;
        return item;
    }

    @Override
    public T peek() {
        if (isEmpty())
            throw new RuntimeException("Empty stack");
        return top.data;
    }
}
