public class BTNode <T>{
    public T data;
    public int freq;
    public BTNode<T> lchild;
    public BTNode <T>rchild;
    public 	BTNode() {
        lchild=rchild=null;
        freq = 1;
    }
    public BTNode(T e) {
        lchild=rchild=null;
        data=e;
        freq = 1;
    }

}