import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class FreqString {
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_BRIGHT_GREEN  = "\u001B[92m";
    public static final String ANSI_RESET = "\u001B[0m";
    String str;
    int freq;

    public FreqString(String str) {
        this.str = str;
        freq = 1;
    }

    @Override
    public boolean equals(Object f) {
        if (!(f instanceof FreqString))
            return false;
        else return str.equals(((FreqString) f).str);
    }

    @Override
    public String toString() {
        return ANSI_BRIGHT_GREEN + str + ANSI_RESET+ ": "+ ANSI_PURPLE + freq + ANSI_RESET;
    }

}

class SortByFreq implements Comparator<FreqString> {

    @Override
    public int compare(FreqString o1, FreqString o2) {
        return o1.freq - o2.freq;
    }
}

public class readTextInfo {

    // colours
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_BRIGHT_GREEN  = "\u001B[92m";
    public static final String ANSI_RESET = "\u001B[0m";

    File[] files;
    private final SearchWordTree tree;

    List<FreqString> freqList = new ArrayList<>();


    public readTextInfo(String path) {
        DirReader dr = new DirReader(path);
        files = dr.getTextFiles();
        tree = new SearchWordTree();
    }

    public void readAllFiles() throws IOException {
        int i;
        for (File file : files) {
            FileReader reader = new FileReader(file);
            StringBuilder line = new StringBuilder();
            while ((i = reader.read()) != -1) {
                if (i >= 'A' && i <= 'Z')
                    i += 'a'-'A';
                if (i >= 'a' && i <= 'z') {
                    line.append((char)i);
                } else if ((char)i == ' ') {
                    line.append((char)i);
                }
            }
            addWords(line.toString().split(" "), file.getName().replace(".txt", ""));
            reader.close();
        }
    }

    public void addWords(String[] words, String fileNum) {
        for (String word : words) {
            if (!word.isEmpty()) {
                tree.insert(word, fileNum);
                FreqString f = new FreqString(word);
                if (!freqList.contains(f)) {
                    freqList.add(f);
                }
                else {
                    int idx = freqList.indexOf(f);
                    freqList.get(idx).freq++; // remove, update, insert
                }
            }
        }
        if (Integer.parseInt(fileNum) % 1500 == 0) System.out.println("processed " + fileNum);
    }

    public void printFrequencies(int n, boolean most) {
        long start = System.currentTimeMillis();
        freqList.sort(new SortByFreq());
        System.out.println("sort took " + ANSI_PURPLE +  (System.currentTimeMillis()-start) + ANSI_RESET + " ms");
        if (most) {
            for (int i = freqList.size()-1; i > freqList.size()-n; i--) {
                System.out.println(freqList.get(i));
            }
        }
        else {
            for (int i = 0; i < n; i++) {
                System.out.println(freqList.get(i));
            }
        }
        System.out.println("-------------------------------------");
    }

    public void searchListOfFiles(String key) {
        Wode elem = tree.search(key);
        if (elem == null) {
            System.out.println("Sorry, " + key + " does not exist.");
        }
        else {
            System.out.println(elem.listOfFiles);
        }
    }

    public static void main(String[] args) throws IOException {
        readTextInfo r = new readTextInfo("AllDocs");
        long start = System.currentTimeMillis();
        r.readAllFiles();
        System.out.println(ANSI_PURPLE + (System.currentTimeMillis() - start) + ANSI_RESET + "ms elapsed");

        Scanner scan = new Scanner(System.in);
        r.ui();
        System.out.print(ANSI_BRIGHT_GREEN + "Enter: ");
        int choice = scan.nextInt();
        do {
            if (choice == 1) {
                System.out.print("Enter keyword: ");
                String key = scan.next();
                r.searchListOfFiles(key);
            } else if (choice == 2) {
                r.printFrequencies(10, true);
            } else if (choice == 3) {
                r.printFrequencies(10, false);
            }
            System.out.print(ANSI_BRIGHT_GREEN + "Enter: ");
            choice = scan.nextInt();
        } while (choice != 4);

    }

    public void ui() {
        System.out.println("1. Enter a single keyword to list the document(s)(file names)");
        System.out.println("2. Print the top 10 words that appeared most frequently");
        System.out.println("3. Print the top 10 words that appeared the least frequently");
        System.out.println("4. Exit");
    }
}
