package com.wow.hangmanassist;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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
}
