package inputoutput;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import java.util.stream.IntStream;

public class ReadingFiles {

    public void readFolder() {
        File folder = new File("/Users/you/folder/");
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                System.out.println(file.getName());
            }
        }
    }

    public void readFile() {
        File file = new File("src/inputoutput/test.txt");
        byte[] bytes = new byte[(int) file.length()];

        try(FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);

            System.out.println(new String(bytes, StandardCharsets.UTF_8));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static BitSet stringToBit(String str) {
        int strlen = str.length();
        BitSet bits = new BitSet(strlen);
        int ctr = 0;

        for (int i = 0; i < strlen; i++) {
            char c = str.charAt(i);
            if (c == '0') {
                bits.set(i, false);
            }
            else if (c == '1') {
                bits.set(i, true);
            }
        }
        return bits;
    }

    public static String bitString(BitSet bits) {
        int nbits = bits.length();
        final StringBuilder buffer = new StringBuilder(nbits);
        IntStream.range(0, nbits).mapToObj(i -> bits.get(i) ? '1' : '0').forEach(buffer::append);
        return buffer.toString();
    }

    public static void main(String[] args) {

        byte[] bytes = new byte[10];
        String s = "1010,123\n";
        bytes = s.getBytes(StandardCharsets.UTF_8);

        for (byte b: bytes) {
            System.out.println(b);
        }
        System.out.println(new String(bytes, StandardCharsets.UTF_8));














//        BitSet bits = new BitSet(7);
//        System.out.println(bitString(bits));
//        bits.set(0, false);
//        System.out.println(bitString(bits));
//        bits.set(1, false);
//        bits.set(2, false);
//        bits.set(3, true);
//        bits.set(4, true);
//        bits.set(5, false);
//        bits.set(6, false);
//        System.out.println(bitString(bits));
//        bits.clear(0);
//        System.out.println(bitString(bits));
//        bits.clear(1);
//        System.out.println(bitString(bits));
//        System.out.println(bitString(stringToBit(s)));
//
//        BitSet bitSet = stringToBit(s); // your original BitSet object
//        int desiredLength = s.length(); // the desired length, including leading zeroes
//        BitSet paddedBitSet = new BitSet(desiredLength);
//        int startIndex = desiredLength - bitSet.length();
//        for (int i = 0; i < bitSet.length(); i++) {
//            if (bitSet.get(i)) {
//                paddedBitSet.set(startIndex + i);
//            }
//        }
//        System.out.println(bitString(paddedBitSet));

//        String s = "01100100";
        // assert s.length() % 8 == 0;
//        byte[] bytes = new byte[s.length()/8];
//        bytes[0] = (byte) Integer.parseInt(s.substring(0, 8), 2);

//        FileOutputStream output;
//        try {
//            output = new FileOutputStream("inttobytetest.txt", true);
//            output.write(bytes);
//            output.close();
//        } catch (FileNotFoundException e) {
//            System.err.printf("Can't find file %s", bytes);
//        } catch (IOException e) {
//            System.err.println("Error with writing to output file");
//        }


    }
}
