import java.io.*;

public class WordCounter {
    private BSStringTree[] alpha = new BSStringTree[26];

    public WordCounter() {}

    private void readFile() throws IOException {
        File file = new File("input.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String str;
        while ((str = br.readLine()) != null) {
            str = str.replaceAll("[^a-zA-Z]", " ").toLowerCase();
            processLine(str);
        }
    }

    private void processLine(String str) {
        String[] arr = str.split(" ");
        for (String word : arr) {
            if (!word.isEmpty()) {
                int idx = word.charAt(0) - 'a';
                if (alpha[idx] == null) {
                    alpha[idx] = new BSStringTree();
                }
                alpha[idx].insertWord(word);
            }
        }
    }

    private void writeWordAndFreq() throws IOException {
        BSStringTree.initialiseWriter("out.txt");
        for (BSStringTree bsst : alpha) {
            bsst.writeWords();
        }
        BSStringTree.closeWriter();
    }

    public static void main(String[] args) throws IOException {
        WordCounter w = new WordCounter();
        long start = System.currentTimeMillis();
        w.readFile();
        System.out.println("it took " + (System.currentTimeMillis() - start) + "ms to process input.txt");
        start = System.currentTimeMillis();
        w.writeWordAndFreq();
        System.out.println("it took " + (System.currentTimeMillis() - start) + "ms to write to out.txt");
    }
}
