import java.io.FileWriter;
import java.io.IOException;

public class BSStringTree {
    private BTNode<String> root = null;
    private static FileWriter fw;
    public BSStringTree() {}

    public void insertWord(String word) {
        if (root == null) {
            root = new BTNode<>(word);
            return;
        }
        BTNode<String> w = root;
        while (w != null) {
            if (w.data.compareTo(word) < 0) {
                // word is larger so go to right branch
                if (w.rchild == null) {
                    w.rchild = new BTNode<>(word);
                    break;
                }
                w = w.rchild;
            }
            else if (w.data.equals(word)) {
                // word is equal. increment freq
                w.freq++;
                break;
            }
            else {
                // word is smol
                if (w.lchild == null) {
                    w.lchild = new BTNode<>(word);
                    break;
                }
                w = w.lchild;
            }
        }
    }

    public void inOrder() {
        System.out.print("in order: ");
        inOrder(root);
    }

    private void inOrder(BTNode<String> b) {
        if (b == null) return;
        inOrder(b.lchild);
        System.out.println(b.data + " " + b.freq);
        inOrder(b.rchild);
    }

    public static void initialiseWriter(String path) throws IOException {
        fw = new FileWriter(path);
    }

    public static void closeWriter() throws IOException {
        fw.close();
    }
    public void writeWords() throws IOException {
        fileWriteInOrder(root);
    }
    private void fileWriteInOrder(BTNode<String> r) throws IOException {
        if (r == null) return;
        fileWriteInOrder(r.lchild);
        fw.write(r.data + " " + r.freq + "\n");
        fileWriteInOrder(r.rchild);
    }

}
