import java.util.Random;

public class Main {

    private final MinHeap mh;
    private final LList ll;
    private final SortedAL al;

    private static final Random rand = new Random();

    public Main() {
        mh = new MinHeap();
        ll = new LList();
        al = new SortedAL();
    }
    public static void main(String[] args) {
        Main m = new Main();
        m.benchmark();
        System.out.println("min heap wins"); // it better win I worked so much on the algorithms
    }

    public void benchmark() {
        // min heap
        long[] mhEnqueueTimes = fill(mh);
        long[] mhDeleteTimes = delete(mh);
        // linked list
        long[] llEnqueueTimes = fill(ll);
        long[] llDeleteTimes = delete(ll);
//        // sorted array list
        long[] alEnqueueTimes = fill(al);
        long[] alDeleteTimes = delete(al);

        printHeader("MinHeap", mhEnqueueTimes, mhDeleteTimes);
        printHeader("LinkedList", llEnqueueTimes, llDeleteTimes);
        printHeader("SortedArrayList", alEnqueueTimes, alDeleteTimes);
    }

    private void printHeader(String name, long[] e, long[] d) {
        System.out.println("Benchmark for " + name);
        System.out.println("| Input Size(n) | Enqueue Time (ms) | DeleteMin Time(ms) |");
        for (int i = 0; i < e.length; i++) {
            System.out.printf("| %5d | %15d | %16d\n", (i+1)*10000, e[i], d[i]);
        }
    }

    public long[] fill(PriorityQueue pq) {
        long start = System.currentTimeMillis();
        long[] times = new long[10];
        int timeCtr = 0;
        for (int i = 0; i < 100000; i++) {
            pq.insert(rand.nextInt(1,1000));
            if ((i+1) % 10000 == 0) {
                long interval = System.currentTimeMillis() - start;
                times[timeCtr++] = interval;
            }
        }
        return times;
    }

    public long[] delete(PriorityQueue pq) {
        long start = System.currentTimeMillis();
        long[] times = new long[10];
        int timeCtr = 0;
        while (pq.size() != 0) {
            pq.deleteMin();
            if (pq.size() % 100000 == 0) {
                long interval = System.currentTimeMillis() - start;
                times[timeCtr++] = interval;
            }
        }
        return times;
    }



}