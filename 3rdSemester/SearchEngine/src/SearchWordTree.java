public class SearchWordTree {

    public Wode root = null;

    public SearchWordTree() {}

    private int height(Wode w) {
        return w == null ? -1 : w.height;
    }

    public void insert(String str, String file) {
        root = insert(str, root, file);
    }

    private Wode insert(String str, Wode wode, String file) {
        // insertion
        if (wode == null) {
            wode = new Wode(str);
            wode.addToList(file);
            return wode;
        } else if (str.compareTo(wode.word) < 0) {
            wode.left = insert(str, wode.left, file);
        } else if (str.compareTo(wode.word) > 0) {
            wode.right = insert(str, wode.right, file);
        } else {
            // same word, increase freq, add file to list
            wode.freq++;
            wode.addToList(file);
        }
        // balancing
        wode.height = max(height(wode.left), height(wode.right)) + 1;

        int bal = balanceNum(wode);
        if (bal > 1) {
            if (str.compareTo(wode.left.word) < 0) {
                return rotateRight(wode);
            } else if (str.compareTo(wode.left.word) > 0) {
                 wode.left = rotateLeft(wode.left);
                 return rotateRight(wode);
            }
        }
        if (bal < -1) {
            if (str.compareTo(wode.right.word) > 0) {
                return rotateLeft(wode);
            } else if (str.compareTo(wode.right.word) < 0) {
                wode.right = rotateRight(wode.right);
                return rotateLeft(wode);
            }
        }
        return wode;
    }

    private Wode rotateRight(Wode wode) {
        Wode a = wode.left;
        Wode b = a.right;
        a.right = wode;
        wode.left = b;
        wode.height = max(height(wode.left), height(wode.right)) + 1;
        a.height = max(height(a.left), height(a.right)) + 1;
        return a;
    }

    private Wode rotateLeft(Wode wode) {
        Wode a = wode.right;
        Wode b = a.left;
        a.left = wode;
        wode.right = b;
        wode.height = max(height(wode.right), height(wode.left)) + 1;
        a.height = max(height(a.right), height(a.left)) + 1;
        return a;
    }

    private int balanceNum(Wode wode) {
        if (wode == null)
            return 0;
        return height(wode.left) - height(wode.right);
    }


    private int max(int lhs, int rhs) {
        return Math.max(lhs, rhs);
    }

    public void inOrder() {
        inOrder(root);
    }

    private void inOrder(Wode node) {
        if (node == null) return;
        inOrder(node.left);
        System.out.print(node.word + " " + node.freq + " " + node.listOfFiles + " \n");
        inOrder(node.right);

    }

    public Wode search(String key) {
        return search(root, key);
    }

    private Wode search(Wode wode, String key) {
        if (wode == null || wode.word.equals(key))
            return wode;
        if (key.compareTo(wode.word) < 0)
            return search(wode.left, key);
        return search(wode.right, key);
    }
}
