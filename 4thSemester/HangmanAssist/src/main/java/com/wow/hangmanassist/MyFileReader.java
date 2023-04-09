package com.wow.hangmanassist;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyFileReader {
    private BufferedReader reader;

    public MyFileReader(URI path) {
        System.out.println(path);
        File file = new File(path);
        try {
            reader = new BufferedReader(new FileReader(file));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("error while initializing the file reader");
        }
    }
    public List<String> read() {
        List<String> list = new ArrayList<>();
        String s;
        try {
            while ((s = reader.readLine()) != null) {
                list.add(s);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error while reading the file");
        }
        return list;
    }

//    public static void main(String[] args) {
//        MyFileReader mfr;
//        List<String> list = null;
//        try {
//            mfr = new MyFileReader(Objects.requireNonNull(MyFileReader.class.getResource("wordList.txt")).toURI());
//            list = mfr.read();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//            System.out.println("error while uri");
//        }
//        assert list != null;
//        Trie trie = new Trie();
//        for (String s : list) {
//            trie.insert(s);
//        }
//        // System.out.println(trie.search("foster"));
////        String pattern = "c      ";
////        trie.suggest(pattern);
////        System.out.println(trie.map);
//    }
}
