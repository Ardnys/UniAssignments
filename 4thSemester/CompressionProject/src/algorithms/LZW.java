package algorithms;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LZW implements CompressionAlgorithm {

    Map<String, Integer> codingMap = new HashMap<>();
    String encodedString;

    String sourcePath = "readthis/lzwtest.txt";
//    String compressPath = sourcePath + ".hf";
    String compressPath = "readthis/lzwcomp.txt";
    String decompressPath = "readthis/lzwdecomp.txt";
    File sourceFile = new File(sourcePath);
    File compressedFile = new File(compressPath);
    File decompressedFile = new File(decompressPath);
    FileOutputStream outputStream;
    FileOutputStream decompressionOutputStream;
    ObjectOutputStream objOutStream;
    ObjectInputStream objInStream;
    FileInputStream inputStream;
    FileInputStream decompressionInputStream;

    public LZW(String sourcePath) {
//        this.sourcePath = sourcePath;
        initStream();
        // initialise the map with one length values
        initMap();

        //printMap();
    }


    @Override
    public byte[] compress(byte[] bytes) {
        String s = byteToString(bytes);

        System.out.println(s);

        ArrayList<Integer> list = lzwEncode(s);

        short[] shortArr = listToShort(list);

//        printMap();
        writeToFile(encodedString.getBytes(StandardCharsets.UTF_8));


        return new byte[0];
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        return new byte[0];
    }

    @Override
    public String getFileFormat() {
        return null;
    }

    private void initStream() {
        try {
            outputStream = new FileOutputStream(compressedFile);
            objOutStream = new ObjectOutputStream(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error while initialising the output streams");
        }

        try {
            inputStream = new FileInputStream(sourceFile);
            // System.out.println("initial available bits " + inputStream.available());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error while initialising the input streams");
        }
    }

    private void initMap() {
        for (int i = 0; i < 256; i++) {
            codingMap.put(String.valueOf((char)i), i);
        }
    }

    private void printMap() {
        for (Map.Entry<String, Integer> entry : codingMap.entrySet()) {
            System.out.println("key: " + entry.getKey() + " value: " + entry.getValue());
        }
    }

    private byte[] readFile() { // this emulates the thing that sends bytes to compress method
        try {
            return Files.readAllBytes(Paths.get(sourcePath));

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Problem while reading the file");
        }
        return new byte[0]; // that's so sketchy
    }

    private String byteToString(byte[] bytes) {
        return new String(bytes);
    }

    private void hotsaucee() {
        try {
            FileReader fr = new FileReader(sourceFile);
            BufferedReader br = new BufferedReader(fr);
            int i = 0;
            while ((i = br.read()) != -1 ) {
                char c = (char) i;
                // send the c to other methods and build the map
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("my hot sauce spilled");
        }
    }

    private ArrayList<Integer> lzwEncode(String s) {
        ArrayList<Integer> outList = new ArrayList<>();
        int code = 256;
        StringBuilder currentString = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            currentString.append(s.charAt(i));
            String realString = currentString.toString();
            if (!codingMap.containsKey(realString)) {
                codingMap.put(realString, code++); // add it to map
                outList.add(codingMap.get(realString.substring(0, realString.length()-1))); // output the code
                currentString.delete(0, currentString.length()-1); // set current string
            }
        }
        outList.add(codingMap.get(currentString.toString()));
        System.out.println("uncompressed: " + s);
        System.out.println("======================\ncompressed");
        for (Integer i : outList) {
            System.out.println(i);
        }
        return outList;
    }

    private short[] listToShort(ArrayList<Integer> arr) {
        int ctr = 0;
        short[] sharr = new short[arr.size()];
        for (int i : arr) {
            short s = (short) i;
            sharr[ctr++] = s;
        }
        return sharr;
    }

    private void writeToFile(byte[] bytes) {
        try {
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Something went wrong while writing compressed fiel");
        }
    }

    public static void main(String[] args) {
        LZW l = new LZW("readthis/lzw.txt");

        l.compress(l.readFile());
        System.out.println("END OF PROGRAM");
    }

}
