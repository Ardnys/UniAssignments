package algorithms;

import java.util.*;

public class Huffman implements CompressionAlgorithm {
    PriorityQueue<HuffmanNode<Character>> queue; // queue for sorted list

    PriorityQueue<HuffmanNode<Character>> firstQueue;
    ArrayList<HuffmanNode<Character>> freqArray = new ArrayList<>(); // list for char freqs
    
    public Huffman() {
        
    }

    @Override
    public void compress() {

    }

    @Override
    public void decompress() {

    }

    public void getCharFreqArr(String s) {
        for (char c: s.toCharArray()) {
            HuffmanNode<Character> temp = new HuffmanNode<>(c ,1);
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
            HuffmanNode<Character> left = firstQueue.poll();
            HuffmanNode<Character> right = firstQueue.poll();

            assert right != null;

            // combine the frequency as a parent node of the left and right
            HuffmanNode<Character> parent = new HuffmanNode<>(null,left.getFrequency() + right.getFrequency(), left, right);

            firstQueue.add(parent);
        }
    }

    public static void main(String[] args) {
        String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut vitae felis in justo feugiat porta. " +
                "Etiam justo ligula, bibendum quis blandit sit amet, aliquet et felis. Nulla consectetur ex mauris, vitae " +
                "dictum nulla aliquet sit amet. Morbi facilisis augue sed est placerat gravida. Quisque placerat placerat " +
                "justo tempor faucibus. Fusce scelerisque at mi sit amet laoreet. Pellentesque metus nulla, hendrerit id quam " +
                "sit amet, condimentum congue nulla. Fusce congue vitae tortor tincidunt dictum. Pellentesque orci sem," +
                " sodales feugiat vulputate non, aliquet et justo. Curabitur at sagittis quam. Sed quis arcu et massa posuere mattis.";

        Huffman h = new Huffman();
        h.getCharFreqArr(text);

        while (!h.firstQueue.isEmpty()) {
            System.out.println(h.firstQueue.poll());
        }
    }

}

class FreqComparator implements Comparator<HuffmanNode<?>> {

    @Override
    public int compare(HuffmanNode<?> o1, HuffmanNode<?> o2) {
        return o1.getFrequency() - o2.getFrequency();
    }
}

class HuffmanNode<T> {

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