package Compressor;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LZW implements CompressionAlgorithm {

    Map<String, Integer> codingMap = new HashMap<>();
    Map<Integer, String> decodingMap = new HashMap<>();
    String encodedString;
    String decodedString;

    public LZW() {
        initEncodingMap();
        initDecodingMap();
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
//        System.out.println(s);

        // System.out.println(escapeString(s));
//        System.out.println("length of bytes: "+bytes.length);

        ArrayList<Integer> list = lzwEncode(s);

        short[] shortArr = listToShort(list);

        byte[] bruh = shortsToBytes(shortArr);

        // short[] please = bytesToShorts(bruh);

        return bruh;
//        writeShorts(please);
//        encodedString = list.toString();
//
////        printMap();
////         writeToFile(encodedString.getBytes(StandardCharsets.UTF_8));
//
//
//        return encodedString.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        ArrayList<Integer> list = bytesToList(bytes);
        String s = lzwDecode(list);

        return s.getBytes(StandardCharsets.UTF_8);

    }

    @Override
    public String getFileFormat() {
        return ".lsw";
    }

    private void initEncodingMap() {
        for (int i = 0; i < 256; i++) {
            codingMap.put(String.valueOf((char)i), i);
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

    private String byteToString(byte[] bytes) {
        return new String(bytes);
    }

    private ArrayList<Integer> lzwEncode(String s) {
        ArrayList<Integer> outList = new ArrayList<>();
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
                String ihateyou = realString.substring(0, realString.length()-1);
                //System.out.println(ihateyou);
                outList.add(codingMap.get(ihateyou)); // output the code
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

    private byte[] shortsToBytes(short[] shortsA) {
        byte[] bytes2 = new byte[shortsA.length * 2];
        ByteBuffer.wrap(bytes2).order(ByteOrder.BIG_ENDIAN).asShortBuffer().put(shortsA);
        return bytes2;
    }

    private short[] bytesToShorts(byte[] bytes) {
        short[] shorts = new short[bytes.length/2];
        ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).asShortBuffer().get(shorts);
        return shorts;
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
//        byte[] pls = new byte[bytes.length];
//        int ctr = 0;
//        for (int i = 4; i < bytes.length; i++) {
//            pls[ctr++] = bytes[i];
//        }
        for (int i = 0; i < bytes.length-1; i+=2) {
            short val=(short)(((bytes[i] & 0xFF) << 8) | (bytes[i+1] & 0xFF));
            list.add((int)val);
        }

//        ByteBuffer bb = ByteBuffer.wrap(bytes);
//        bb.order(ByteOrder.BIG_ENDIAN);
//        while (bb.remaining() >= 2) {
//            short value = bb.getShort();
////            if (value >= 0 && value < 256)
//                list.add((int) value);
//        }
        return list;
    }


    private String lzwDecode(ArrayList<Integer> list) {
        /*
        Here's a step-by-step guide to decompressing LZW-encoded data:

        Initialize the dictionary with all possible single-character patterns.

        Read the first code from the compressed data.

        Look up the corresponding pattern in the dictionary and output it.

        While there are still codes left in the compressed data:

        a. Read the next code from the compressed data.

        b. If the code is not in the dictionary, create a new pattern by concatenating the last pattern output with the first character of the last pattern output. Output this new pattern.

        c. If the code is in the dictionary, look up the corresponding pattern and output it.

        d. Add a new dictionary entry for the last pattern output concatenated with the first character of the pattern just output. This new entry is assigned the next available code.

        The decompressed data is the concatenation of all the patterns output during the decompression process.
         */
        StringBuilder output = new StringBuilder();
        String prev = decodingMap.get(list.get(0));
        output.append(prev);
        String other = "";
        int code;
        int next = 256;

        for (int i = 1; i < list.size(); i++) {
            code = list.get(i);
            if (decodingMap.containsKey(code)) {
                other = decodingMap.get(code);
                output.append(other);

            }
            String s = prev + other.charAt(0);
            decodingMap.put(next++, s);
            prev = other;
        }
        decodedString = output.toString();
        System.out.println(decodedString);
        return output.toString();
    }

//    public static void main(String[] args) {
//        LZW l = new LZW();
//
//        l.compress(l.readFile(l.sourcePath));
//
//        l.decompress(l.readFile(l.compressPath));
//
//        System.out.println("\nEND OF PROGRAM");
//    }
}
