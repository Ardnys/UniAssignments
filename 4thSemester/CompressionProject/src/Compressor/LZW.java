package Compressor;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LZW implements CompressionAlgorithm {

    Map<String, Integer> codingMap = new HashMap<>();
    Map<Integer, String> decodingMap = new HashMap<>();
    String encodedString;
    String decodedString;

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
        initEncodingStream();
        // initialise the map with one length values
        initEncodingMap();


        // these will be on another class later
        initDecodingStream();
        initDecodingMap();

        //printMap();
    }


    @Override
    public byte[] compress(byte[] bytes) {
        String s = byteToString(bytes);
//        s = s.replace("\r", ""); // i hate this lol
//        s = s.replace("’", "'");
//        s = s.replace("—", "-");
//        s = s.replace("‘", "'");
//        s = s.replace("“", "\"");
//        s = s.replace("”", "\"");
//        s = s.replace("œ", "oe"); // œ

        // String str = "This is a string with [some] special + characters \\ that need to be escaped.";
//        String escaped = escapeString(s);
//        System.out.println(escaped);
        System.out.println(s);

        // System.out.println(escapeString(s));
        System.out.println("length of bytes: "+bytes.length);

        ArrayList<Integer> list = lzwEncode(s);

        short[] shortArr = listToShort(list);

        writeShorts(shortArr);
         encodedString = list.toString();

//        printMap();
//         writeToFile(encodedString.getBytes(StandardCharsets.UTF_8));


        return new byte[0];
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        ArrayList<Integer> list = bytesToList(bytes);
        lzwDecode(list);
        return new byte[0];
    }

    @Override
    public String getFileFormat() {
        return null;
    }

    private void initEncodingStream() {
        try {
            outputStream = new FileOutputStream(compressedFile);
            objOutStream = new ObjectOutputStream(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error while initialising the compression output streams");
        }

        try {
            inputStream = new FileInputStream(sourceFile);
            // System.out.println("initial available bits " + inputStream.available());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error while initialising the compression input streams");
        }
    }

    private void initEncodingMap() {
        for (int i = 0; i < 256; i++) {
            codingMap.put(String.valueOf((char)i), i);
        }
    }

    private void initDecodingStream() {
        try {
            decompressionOutputStream = new FileOutputStream(decompressedFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error while initialising the decompression output stream");
        }
        try {
            decompressionInputStream = new FileInputStream(compressedFile);
            objInStream = new ObjectInputStream(decompressionInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error while initialising the decompression input streams");
        }
    }

    private void initDecodingMap() {
        for (int i = 0; i < 256; i++) {
            decodingMap.put(i, String.valueOf((char)i));
        }
    }

    private void printMap() {
        for (Map.Entry<String, Integer> entry : codingMap.entrySet()) {
            System.out.println("key: " + entry.getKey() + " value: " + entry.getValue());
        }
    }

    private byte[] readFile(String path) { // this emulates the thing that sends bytes to compress method
        try {
            return Files.readAllBytes(Paths.get(path));

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
        ArrayList<Integer> outList = new ArrayList<>(150);
        int code = 256;
        StringBuilder currentString = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if ((int)(s.charAt(i)) > 256) {
                continue;
            }
            currentString.append(s.charAt(i));
            String realString = currentString.toString();
            if (!codingMap.containsKey(realString)) {
                codingMap.put(realString, code++); // add it to map
                outList.add(codingMap.get(realString.substring(0, realString.length()-1))); // output the code
//                if (outList.size() > 1063 && outList.size() < 2000)
//                    System.out.println("added index " + outList.size() +  " string: " + realString.substring(0, realString.length()-1)
//                            + " code: " + codingMap.get(realString.substring(0, realString.length()-1)) +
//                            "add returns: " + uh);
                //System.out.println(realString);
                currentString.delete(0, currentString.length()-1); // set current string
            }
        }
        outList.add(codingMap.get(currentString.toString()));
//        System.out.println("uncompressed: " + s);
//        System.out.println("======================\ncompressed");
//        for (Integer i : outList) {
//            System.out.println(i);
//        }
        return outList;
    }

    private short[] listToShort(ArrayList<Integer> arr) {
        int ctr = 0;
        short[] sharr = new short[arr.size()];
        try {
            for (int i : arr) {
                if (ctr == 105) {
                    System.out.println("hold on");
                }
                short s = (short) i;
                sharr[ctr++] = s;
            }
            return sharr;
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("exception at " + ctr);
        }
        return sharr;
    }

//    private void writeToFile(byte[] bytes) {
//        try {
//            outputStream.write(bytes);
//            outputStream.flush();
//            outputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("Something went wrong while writing compressed fiel");
//        }
//    }

    private void writeShorts(short[] nope) {
        try (DataOutputStream dos = new DataOutputStream(outputStream)) {
            for (short code: nope) {
                System.out.println(code);
                dos.writeShort(code);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("problem while writing shorts");
        }
    }

    /*
    ----------------------------------------------------------------------
    |                                                                    |
    |                                                                    |
    |             D  E  C  O  M  P  R  E  S  S  I  O  N                  |
    |                                                                    |
    |                                                                    |
    ----------------------------------------------------------------------
     */

    private ArrayList<Integer> bytesToList(byte[] bytes) {
        ArrayList<Integer> list = new ArrayList<>();
        byte[] pls = new byte[bytes.length-4];
        int ctr = 0;
        for (int i = 4; i < bytes.length; i++) {
            pls[ctr++] = bytes[i];
        }
        ByteBuffer bb = ByteBuffer.wrap(pls);
        bb.order(ByteOrder.BIG_ENDIAN); // set the byte order to little-endian
        while (bb.remaining() >= 2) {
            short value = bb.getShort();
//            if (value >= 0 && value < 256)
                list.add((int) value);
        }
        return list;
    }


    private void lzwDecode(ArrayList<Integer> list) {
        int old = list.get(0); // TODO fix this hopefully understand too
        int sym = 0;
        int next = 256;
        StringBuilder sb = new StringBuilder();
        sb.append(decodingMap.get(old));
        String s = "";
        s = s+decodingMap.get(old);
        StringBuilder prev = new StringBuilder();
        prev.append(s.charAt(0));
        //System.out.print(s);
        for (int i = 0; i < list.size()-1; i++) {
            sym = list.get(i+1);
            if (!decodingMap.containsKey(sym)) {
                sb.delete(0, sb.length());
                sb.append(decodingMap.get(old));
                sb.append(prev);

            } else {
                sb.append(decodingMap.get(sym));
            }
            System.out.print(sb);
            prev.delete(0, prev.length());
            prev.append(sb.charAt(0));
            String uh = decodingMap.get(old) + prev;
            decodingMap.put(next, uh);
            next++;
            old = sym;
        }

    }

    public static void main(String[] args) {
        LZW l = new LZW("readthis/lzw.txt");

        l.compress(l.readFile(l.sourcePath));

        l.decompress(l.readFile(l.compressPath));



        System.out.println("\nEND OF PROGRAM");
    }

//    public String escapeString(String s) {
//        // define the characters that need to be escaped
//        String specialChars = "\\[]*+-?.,|^$&";
//
//        // replace each special character with its escape sequence
//        StringBuilder sb = new StringBuilder();
//        Pattern p = Pattern.compile("([" + specialChars + "\"])");
//        Matcher m = p.matcher(s);
//        while (m.find()) {
//            String matched = m.group(1);
//            if (matched.equals("\\")) {
//                m.appendReplacement(sb, "\\\\\\\\");
//            } else {
//                m.appendReplacement(sb, "\\\\" + matched);
//            }
//        }
//        m.appendTail(sb);
//
//        // escape the single and double quotes
//        sb = new StringBuilder(sb.toString().replace("'", "\\'").replace("\"", "\\\""));
//
//        return sb.toString();
//    }


}
