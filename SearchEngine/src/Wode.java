import java.util.LinkedList;

public class Wode {
    public String word;
    public LinkedList<String> listOfFiles;
    public Wode left;
    public Wode right;
    public int height;
    public int freq;

    public Wode(String word) {
        left = right = null;
        this.word = word;
        height = 0;
        listOfFiles = new LinkedList<>();
        freq = 1;
    }

    public void addToList(String fileNum) {
        if (listOfFiles.contains(fileNum))
            return;
        listOfFiles.add(fileNum);
    }

}
