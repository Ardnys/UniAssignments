package Compressor;

import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

public class Huffman implements CompressionAlgorithm {
    class HuffmanNode implements Serializable {

        // data is null for internal nodes
        private Byte data;
        private int frequency;

        // these are only for internal nodes
        private HuffmanNode left;
        private HuffmanNode right;

        public HuffmanNode(Byte data, int frequency) {
            this.data = data;
            this.frequency = frequency;
        }

        public HuffmanNode(Byte data, int frequency, HuffmanNode left, HuffmanNode right) {
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
            HuffmanNode that = (HuffmanNode) o;
            return data.equals(that.data);
        }

        @Override
        public int hashCode() {
            return Objects.hash(data);
        }

        @Override
        public String toString() {
            if (left == null || right == null) {
                return "data=" + data + ", frequency=" + frequency;
            }
            else {
                return "data=" + data +
                        ", frequency=" + frequency + ", left=" + left.data + ", right="+right.data;
            }
        }

        public void incrementFrequency() {
            frequency++;
        }

        public void incrementFrequency(int num) {
            frequency += num;
        }
        public HuffmanNode getLeft() {
            return left;
        }

        public void setLeft(HuffmanNode left) {
            this.left = left;
        }

        public HuffmanNode getRight() {
            return right;
        }

        public void setRight(HuffmanNode right) {
            this.right = right;
        }

        public Byte getData() {
            return data;
        }

        public int getFrequency() {
            return frequency;
        }

        public void setData(Byte data) {
            this.data = data;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }
    }

    class FreqComparator implements Comparator<HuffmanNode> {

        @Override
        public int compare(HuffmanNode o1, HuffmanNode o2) {
            return o1.getFrequency() - o2.getFrequency();
        }
    }
    PriorityQueue<HuffmanNode> firstQueue;
    PriorityQueue<HuffmanNode> secondQueue;
    PriorityQueue<HuffmanNode> decodingQueue;
    ArrayList<HuffmanNode> freqArray = new ArrayList<>(); // list for char freqs
    Map<String, Byte> codingMap = new HashMap<>();

    Map<String, Byte> decodingMap;

    int sz = 0;
    public String encodedString;
    int codeLength = 0;


    String sourcePath = "readthis/lorem.txt";
    String compressPath = sourcePath + ".hf";
    String decompressPath = "readthis/decomp.txt";
    File sourceFile = new File(sourcePath);
    File compressedFile = new File(compressPath);
    File decompressedFile = new File(decompressPath);
    FileOutputStream outputStream;
    FileOutputStream decompressionOutputStream;
    ObjectOutputStream objOutStream;
    ObjectInputStream objInStream;
    FileInputStream inputStream;
    FileInputStream decompressionInputStream;

    public Huffman() {
        // TODO get paths from constructor
        initStream();
    }

    @Override
    public byte[] compress(byte[] bytes) {
        
        getByteArr(bytes);
        
        codingTable();
        
//        createHeader();
        // header is created when writing to file
        String body = createEncodedBody(bytes);
        //System.out.println(" "+body);
        encodedString = body;
        BitSet bits = strToBit(body);

//        writeCompressedFile(bits, "readthis/comp.txt");
        
        return bits.toByteArray();
    }
    
    @Override
    public byte[] decompress(byte[] bytes) {
        // first read the header
        //int l = readHeader();

        // read bytes, convert it to BitSet, convert it to binary String and reverse it
        byte[] byteBody = readHeader();
        int l = byteBody.length;

        String thembits =  reverseString(bitsToBitsLOL(bytesToBits(byteBody)));  // lol
        //System.out.println(encodedString + "\nlen: " + encodedString.length());

        // substring it to remove padding
        thembits = thembits.substring(1, l);
//        System.out.println(thembits +"\nlen: " + (l));
        // thembits.substring(0, thembits.length()-40)

        // decompress and write it to file
        // byte[] decompressedBytes = actualDecompression(thembits);
        // writeDecodedBytes(actualDecompression(thembits));

        return actualDecompression(thembits);
    }

    @Override
    public String getFileFormat() {
        return ".hf";
    }

    public void getByteArr(byte[] bytes) {
        for (byte b: bytes) {
            HuffmanNode temp = new HuffmanNode(b ,1);
            if (!freqArray.contains(temp)) {
                freqArray.add(temp);
            }
            else {
                freqArray.get(freqArray.indexOf(temp)).incrementFrequency();
            }
        }
        System.out.println(bytes.length);

//        for (HuffmanNode node : freqArray) {
//            System.out.println(node);
//        }


        firstQueue = new PriorityQueue<>(freqArray.size()*2, new FreqComparator());
        firstQueue.addAll(freqArray);
        //System.out.println(firstQueue.size());
        sz = firstQueue.size();
        secondQueue = new PriorityQueue<>(freqArray.size()*2, new FreqComparator());
        secondQueue.addAll(freqArray);
        generateHuffmanTree(secondQueue);
        generateHuffmanTree(firstQueue);
    }

    public void generateHuffmanTree(PriorityQueue<HuffmanNode> queue) {
        // take the first two nodes and join them
        while (queue.size() > 1) {
            HuffmanNode left = queue.poll();
            HuffmanNode right = queue.poll();

            assert right != null;

            // combine the frequency as a parent node of the left and right
            HuffmanNode parent = new HuffmanNode(null,left.getFrequency() + right.getFrequency(), left, right);

            queue.add(parent);
        }
    }

    public void codingTable() {
        traverse(firstQueue.peek(), "");
    }

    private void traverse(HuffmanNode node, String code) {
        if (node.getData() == null) {
            traverse(node.getLeft(), code.concat("0"));
            traverse(node.getRight(), code.concat("1"));
        }
        else {
            codingMap.put(code, node.getData());
        }
    }
    public String createEncodedBody(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (Byte b: bytes) {
            sb.append(getKeyByValue(b));
        }

        // return sb.toString().getBytes(StandardCharsets.UTF_8);
        return sb.toString();
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
        str = "1" + str + "1"; // i hate this soo much
        codeLength = str.length();
        System.out.println(codeLength);
        // System.out.println(strlen);
        BitSet bits = new BitSet(codeLength);
        int ctr = 0;

        for (int i = codeLength-1; i >= 0 ; i--) {
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
    // TODO if the performance sucks return here. this makes BitSet
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

    public void printTable() {
        for (Map.Entry<String, Byte> e : codingMap.entrySet()) {
            System.out.println("key: " + e.getKey() + " value: " + e.getValue());
        }
    }

    public byte[] renamethis() {
        byte[] bytes = new byte[(int) sourceFile.length()];
        try {
            inputStream.read(bytes);
            //System.out.println("available bits after reading: " + inputStream.available());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error while reading the source file");
        }
        return bytes;
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
            System.out.println("initial available bits " + inputStream.available());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error while initialising the input streams");
        }
    }

    public void writeCompressedFile(byte[] bytes, String pathname) {
//        String s= new String(bytes, StandardCharsets.UTF_8);
//        System.out.println(s);
        firstQueue.clear();
        firstQueue.addAll(freqArray);
        try {
            //objOutStream.writeInt(sz);
            // System.out.println("sz is: " + sz);
//            while (!firstQueue.isEmpty()) {
//                objOutStream.writeObject(firstQueue.poll()); // write the element
//            }

//             write the length of the compressed bit string

            objOutStream.writeInt(codeLength);

            objOutStream.writeObject(codingMap);

            // writing the body for the compressed file
            objOutStream.write(bytes);
            objOutStream.flush();
            outputStream.flush();
            objOutStream.close();
            outputStream.close();
        }
        catch (IOException e) {
            System.out.println("Error while writing to compressed file");
            e.printStackTrace();
        }

    }

    public void inorder(PriorityQueue<HuffmanNode> qu) {
        orderin(qu.peek());
    }

    private void orderin(HuffmanNode node) {
        if (node == null) return;
        orderin(node.getLeft());
        System.out.print(node.getData() == null ? "" : node.getData() + " | ");
        orderin(node.getRight());
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

    public void initForDecompression() {
        try {
            decompressionInputStream = new FileInputStream(compressedFile);
            //System.out.println("available bits for decompression " + decompressionInputStream.available());
            objInStream = new ObjectInputStream(decompressionInputStream);
            decompressionOutputStream = new FileOutputStream(decompressedFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error while init decompression stream");
        }
    }


    public byte[] readHeader() {
        // first read an integer from the stream.
        int bitlen = 0;
        byte[] bytes = new byte[0];
        try {
            initForDecompression();

            bitlen = objInStream.readInt();
            bytes = new byte[bitlen];

            decodingMap = (Map<String, Byte>) objInStream.readObject();
            objInStream.read(bytes);

            //System.out.println("map is " + decodingMap);

             //int treeLength = objInStream.readInt();

//            System.out.println("tree length is: " + treeLength);

            // ArrayList<HuffmanNode> list = new ArrayList<>(treeLength);
            // Class<HuffmanNode> huffmanNodeClass = HuffmanNode.class;

            // read the object

//            for (int i = 0; i < treeLength; i++) {
//                Object o = objInStream.readObject();
//                HuffmanNode node = huffmanNodeClass.cast(o);
//                list.add(node);
//                // System.out.println(node);
//            }

//            decodingQueue = new PriorityQueue<>(new FreqComparator());
//            decodingQueue.addAll(list);
//            generateHuffmanTree(decodingQueue);
//            for (HuffmanNode node : list) {
//                System.out.println(node);
//            }
            // read the length of the encoded string
            // bitlen = objInStream.readInt();
//            System.out.println("-----------------------------------");
//            System.out.println("Table");
            //printTable();
//            System.out.println("list size: " + list.size());
//            System.out.println("queue size: " + decodingQueue.size());


        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error while writing to decompressed file");
        } catch (ClassNotFoundException ce) {
            ce.printStackTrace();
            System.out.println("what do you mean class not found");
        }
        return bytes;
    }

    public byte[] readBytes(int codeLength) {
        byte[] bytes = new byte[codeLength];
        try {
            decompressionInputStream.read(bytes);
            //System.out.println("available bits after reading the compressed file: " + decompressionInputStream.available());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error while reading the compressed file");
        }
        return bytes;
    }

    public BitSet bytesToBits(byte[] bytes) {
        // convert byte array to BitSet
        // i am just trying to undo what i did, hoping that it works lol

        return BitSet.valueOf(bytes);
    }
    
    public String bitsToBitsLOL(BitSet bits) {
        int nbits = bits.length();
        System.out.println("decoded bit len: " + nbits);
        final StringBuilder buffer = new StringBuilder(nbits);
        IntStream.range(0, nbits).mapToObj(i -> bits.get(i) ? '1' : '0').forEach(buffer::append);
        return buffer.toString();
    }

    public String reverseString(String str) {
        StringBuilder sb = new StringBuilder(str);
//        sb.deleteCharAt(0);
//        sb.deleteCharAt(str.length()-1);
        return sb.reverse().toString();
    }

    public byte[] actualDecompression(String encodedBinaryString) {
        // StringBuilder decodedString = new StringBuilder();
        byte[] bytes = new byte[encodedBinaryString.length()];
        // char[] encodedCharArray = encodedBinaryString.toCharArray();
         int ctr = 0;
        String code = "";
        for (int i = 0; i < encodedBinaryString.length(); i++) {
            if (decodingMap.containsKey(code)) {
                // write the value and reset the string
                //System.out.println("key: " + code + " value: " + decodingMap.get(code));
                bytes[ctr++] = decodingMap.get(code);
                code = "";
            }
            code = code + encodedBinaryString.charAt(i);
        }

//        HuffmanNode parent = decodingQueue.peek();
//        HuffmanNode node = decodingQueue.peek();
//        for (char c : encodedCharArray) {
//            assert node != null;
//            if (c == '0') {
//                // go to the left
//                node = node.getLeft();
//                code = code.concat("0");
//            } else if (c == '1') {
//                // go to the right
//                node = node.getRight();
//                code = code.concat("1");
//            }
//            //System.out.println(node);
//            if (node.getData() != null) {
//                // we are at leaf, get the data and add it to string
//                // System.out.println("Code: " + code + " value: " + codingMap.get(code));
//                System.out.println("----------------------------------------");
//                System.out.println("byte data: "+ node.getData() + " code: " + code);
//                bytes[i++] = node.getData();
//                node = parent;
//                code = "";
//            }
//        }
        System.out.println(ctr);
        byte[] bytesToWrite = new byte[ctr];
        for (int i = 0; i < ctr; i++) {
            bytesToWrite[i] = bytes[i];
        }
        return bytesToWrite;
    }

    public void writeDecodedBytes(byte[] bytes) {
        try {
            System.out.println("written length: " + bytes.length);
            decompressionOutputStream.write(bytes);
            decompressionOutputStream.flush();
            decompressionOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("problem writing the decompressed file");
        }
    }

    public static void main(String[] args) {
        Huffman h = new Huffman();
        byte[] compressedBytes = h.compress(h.renamethis());
        h.writeCompressedFile(compressedBytes, "readthis/comp.txt");
        System.out.println("hold on to your buns, i am gonna read the compressed file");
        h.writeDecodedBytes(h.decompress(compressedBytes));

//        System.out.println("encoding queue");
//        h.inorder(h.secondQueue);
//        System.out.println("decoding queue");
//        h.inorder(h.decodingQueue);

    }
}



