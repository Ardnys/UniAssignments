package inputoutput;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

        String s = "0110100";
        System.out.println(bitString(stringToBit(s)));

        BitSet bitSet = stringToBit(s); // your original BitSet object
        int desiredLength = s.length(); // the desired length, including leading zeroes
        BitSet paddedBitSet = new BitSet(desiredLength);
        int startIndex = desiredLength - bitSet.length();
        for (int i = 0; i < bitSet.length(); i++) {
            if (bitSet.get(i)) {
                paddedBitSet.set(startIndex + i);
            }
        }
        System.out.println(bitString(paddedBitSet));


    }
}
