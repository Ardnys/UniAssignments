package algorithms;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Huffman implements CompressionAlgorithm {
    PriorityQueue<HuffmanNode<Byte>> queue; // queue for sorted list

    PriorityQueue<HuffmanNode<Byte>> firstQueue;
    ArrayList<HuffmanNode<Byte>> freqArray = new ArrayList<>(); // list for char freqs
    Map<String, Byte> codingMap = new TreeMap<>();


    File compressedFile;
    
    public Huffman() {
        
    }

    @Override
    public byte[] compress(byte[] bytes) {
        
        getByteArr(bytes);
        
        codingTable();
        
        createHeader();
        String body = createEncodedBody(bytes);

        BitSet bits = strToBit(body);

//        writeCompressedFile(bits, "readthis/comp.txt");
        
        return bits.toByteArray();
    }
    
    @Override
    public byte[] decompress(byte[] bytes) {
        return new byte[0];
    }

    @Override
    public String getFileFormat() {
        return null;
    }

    public void getByteArr(byte[] bytes) {
        for (byte b: bytes) {
            HuffmanNode<Byte> temp = new HuffmanNode<>(b ,1);
            if (!freqArray.contains(temp)) {
                freqArray.add(temp);
            }
            else {
                freqArray.get(freqArray.indexOf(temp)).incrementFrequency();
            }
        }
        queue = new PriorityQueue<>(freqArray.size()*2, new FreqComparator());
        firstQueue = new PriorityQueue<>(freqArray.size()*2, new FreqComparator());
        firstQueue.addAll(freqArray);
        construction();
    }

    public void construction() {
        // take the first two nodes and join them
        while (firstQueue.size() > 1) {
            HuffmanNode<Byte> left = firstQueue.poll();
            HuffmanNode<Byte> right = firstQueue.poll();

            assert right != null;

            // combine the frequency as a parent node of the left and right
            HuffmanNode<Byte> parent = new HuffmanNode<>(null,left.getFrequency() + right.getFrequency(), left, right);

            firstQueue.add(parent);
        }
    }

    public void codingTable() {
        /*
        let's make it recursive
        if node.data == null: // meaning parent node
            fun(node.left, str.append('0')
            fun(node.right, str.append('1')
        else: // we hit a char
            map.put(node.data, str)
            return
         */
        traverse(firstQueue.peek(), "");
    }

    private void traverse(HuffmanNode<Byte> node, String code) {
        if (node.getData() == null) {
            traverse(node.getLeft(), code.concat("0"));
            traverse(node.getRight(), code.concat("1"));
        }
        else {
            // encode the string as bits
            // System.out.println("String code: " + code);

//            BitSet bit = strToBit(code);
//
//            System.out.println("Bit code: " + bitString(bit));
            codingMap.put(code, node.getData());
        }
    }

//    public ArrayList<HuffmanNode<Byte>> huffmanTreePreorder(HuffmanNode<Byte> node) {
//        ArrayList<HuffmanNode<Byte>> array = new ArrayList<>(firstQueue.size());
//        return preorder(node, array);
//    }
//
//    private ArrayList<HuffmanNode<Byte>> preorder(HuffmanNode<Byte> node, ArrayList<HuffmanNode<Byte>> huffmanArray) {
//        if (node == null) return huffmanArray;
//        huffmanArray.add(node);
//        huffmanArray = preorder(node.getLeft(), huffmanArray);
//        huffmanArray = preorder(node.getRight(), huffmanArray);
//        return huffmanArray;
//    }
//
//    public ArrayList<HuffmanNode<Byte>> huffmanTreeInorder(HuffmanNode<Byte> node) {
//        ArrayList<HuffmanNode<Byte>> array = new ArrayList<>(firstQueue.size());
//        return inorder(node, array);
//    }
//
//    private ArrayList<HuffmanNode<Byte>> inorder(HuffmanNode<Byte> node, ArrayList<HuffmanNode<Byte>> huffmanArray) {
//        if (node == null) return huffmanArray;
//        huffmanArray = inorder(node.getLeft(), huffmanArray);
//        huffmanArray.add(node);
//        huffmanArray = inorder(node.getRight(), huffmanArray);
//        return huffmanArray;
//    }


    public String createEncodedBody(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (Byte b: bytes) {
            sb.append(getKeyByValue(b));
        }

        // return sb.toString().getBytes(StandardCharsets.UTF_8);
        return sb.toString();
    }

    public void createHeader() {
        // store the huffman tree here
        // StringBuilder sb = new StringBuilder();

        try {
            FileOutputStream fos = new FileOutputStream("serialtest.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.write(firstQueue.size()); // first write the number of elements in the priority queue

            while (!firstQueue.isEmpty()) {
                oos.writeObject(firstQueue.poll()); // write the element
                oos.writeChar(','); // comma separate them
            }

        } catch (IOException e) {
            System.out.println("Problem while writing header");
            e.printStackTrace();
        }
    }

    public String getKeyByValue(Byte value) {
        for (Map.Entry<String, Byte> entry : codingMap.entrySet() ) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public BitSet strToBit(String str) {
        // str += "1"; // bits demand a sacrifice
        int strlen = str.length();
        System.out.println(strlen);
        BitSet bits = new BitSet(strlen);
        int ctr = 0;

        for (int i = strlen-1; i >= 0 ; i--) {
            char c = str.charAt(i);
            if (c == '0') {
                bits.set(ctr++, false);
            }
            else if (c == '1') {
                bits.set(ctr++, true);
            }
        }
        return bits;
    }
    // TODO if the performance sucks return here
//    private void traverse(HuffmanNode<Byte> node, BitSet code, int bitlen) {
//        if (node.getData() == null) {
//            code.set(bitlen, false);
//            traverse(node.getLeft(), code, bitlen);
//            code.clear(bitlen-1); // remove the last bit set by the left subtree
//
//            code.set(bitlen, true);
//            traverse(node.getRight(), code, bitlen);
//            code.clear(bitlen-1); // remove the last bit set by the right subtree
//        } else {
//            codingMap.put((BitSet) code.clone(), node.getData());
//            System.out.println("code: " + bitString(code) + " data: " + node.getData());
//        }
//    }

//    public String convertTo8bits(String binary) {
//        StringBuilder zeros = new StringBuilder();
//
//        for (int i = 0; i != (8 - binary.length()); i++)
//            zeros.append("0");
//        return zeros + binary;
//    }

     private String bitString(BitSet bits) {
        int nbits = bits.length();
        final StringBuilder buffer = new StringBuilder(nbits);
        IntStream.range(0, nbits).mapToObj(i -> bits.get(i) ? '1' : '0').forEach(buffer::append);
        return buffer.toString();
    }

    public void printTable() {
        for (Map.Entry<String, Byte> e : codingMap.entrySet()) {
            System.out.println("key: " + e.getKey() + " value: " + e.getValue());
        }
    }



    public void writeCompressedFile(byte[] bytes, String pathname) {
//        String s= new String(bytes, StandardCharsets.UTF_8);
//        System.out.println(s);
        File outputFile = new File(pathname);
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            outputStream.write(bytes);
        }
        catch (IOException e) {
            System.out.println("Error while writing to compressed file");
            e.printStackTrace();
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

    public String readHeader(byte[] bytes) {
        // read the header and call the thing to store the table
        // pass the rest of the array to method that read the body



        return null;
    }


    public static void main(String[] args) {
        String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut vitae felis in justo feugiat porta. " +
                "Etiam justo ligula, bibendum quis blandit sit amet, aliquet et felis. Nulla consectetur ex mauris, vitae " +
                "dictum nulla aliquet sit amet. Morbi facilisis augue sed est placerat gravida. Quisque placerat placerat " +
                "justo tempor faucibus. Fusce scelerisque at mi sit amet laoreet. Pellentesque metus nulla, hendrerit id quam " +
                "sit amet, condimentum congue nulla. Fusce congue vitae tortor tincidunt dictum. Pellentesque orci sem," +
                " sodales feugiat vulputate non, aliquet et justo. Curabitur at sagittis quam. Sed quis arcu et massa posuere mattis.";

        Huffman h = new Huffman();

        File file = new File("readthis/lorem.txt");
        byte[] bytes = new byte[(int) file.length()];

        try(FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);

            //System.out.println(new String(bytes, StandardCharsets.UTF_8));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] compressedBytes = h.compress(bytes);
        h.writeCompressedFile(compressedBytes, "readthis/comp.txt");

        File compfile = new File("readthis/comp.txt");
        //byte[] outbytes = new byte[(int) compfile.length()];

        try {
            byte [] fileBytes = Files.readAllBytes(compfile.toPath());
            char singleChar;
            for(byte b : fileBytes) {
                singleChar = (char) b;
                System.out.print(singleChar);
            }

        }
        catch (IOException e) {
            System.out.println("Error while reading file");
        }

//        try(FileInputStream fis = new FileInputStream(compfile)) {
//            fis.read(outbytes);
//
//            //System.out.println(new String(bytes, StandardCharsets.UTF_8));
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        h.writeCompressedFile(outbytes, "readthis/hmm.txt");



        // h.getByteArr(bytes);

//        while (!h.firstQueue.isEmpty()) {
//            System.out.println(h.firstQueue.poll());
//        }
//        h.codingTable();
//        h.printTable();
//        String header = h.createHeader();
//        //byte[] thembits = h.createEncodedBody(bytes);
//        String boi = header + h.createEncodedBody(bytes);
//        byte[] byteArray = boi.getBytes(StandardCharsets.UTF_8);
//        BitSet bs = h.strToBit(boi);

        // System.out.println(Arrays.toString(thembits);


        // create directory and put the compressed file in it
//        String compressedDirPath = file.getName()+"-compressed";
//        File compressedDir = new File(compressedDirPath);
//        boolean yes = compressedDir.mkdir();
//        if (yes) {
//            System.out.println("Folder created");
//        } else {
//            System.out.println("Error while creating a folder");
//        }
    }
}

class FreqComparator implements Comparator<HuffmanNode<?>> {

    @Override
    public int compare(HuffmanNode<?> o1, HuffmanNode<?> o2) {
        return o1.getFrequency() - o2.getFrequency();
    }
}

class HuffmanNode<T> implements Serializable {

    // data is null for internal nodes
    private T data;
    private int frequency;

    // these are only for internal nodes
    private HuffmanNode<T> left;
    private HuffmanNode<T> right;

    public HuffmanNode(T data, int frequency) {
        this.data = data;
        this.frequency = frequency;
    }

    public HuffmanNode(T data, int frequency, HuffmanNode<T> left, HuffmanNode<T> right) {
        this.data = data;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    // default constructor. don't use it
    public HuffmanNode() {
        this.data = null;
        this.frequency = 0;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HuffmanNode<?> that = (HuffmanNode<?>) o;
        return data.equals(that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    @Override
    public String toString() {
        return "data=" + data +
                ", frequency=" + frequency + ", left=" + left.data + ", right="+right.data;
    }

    public void incrementFrequency() {
        frequency++;
    }

    public void incrementFrequency(int num) {
        frequency += num;
    }
    public HuffmanNode<T> getLeft() {
        return left;
    }

    public void setLeft(HuffmanNode<T> left) {
        this.left = left;
    }

    public HuffmanNode<T> getRight() {
        return right;
    }

    public void setRight(HuffmanNode<T> right) {
        this.right = right;
    }

    public T getData() {
        return data;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}