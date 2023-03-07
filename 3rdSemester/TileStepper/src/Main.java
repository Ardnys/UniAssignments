import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

public class Main {

    private static Floor floor;
    private Bug roach;
    private static final Random r = new Random();
    private Stack<Coord> stack = new Stack<>();
    private final int numberOfTrials = 200;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_BRIGHT_GREEN  = "\u001B[92m";
    public static final String ANSI_CYAN   = "\u001B[36m";
    public static final String ANSI_RED    = "\u001B[31m";
    public static final String ANSI_BLACK  = "\u001B[30m";
    public static final String ANSI_BG_RED    = "\u001B[41m";

    public Main() {

    }

    private void fin() {
        floor.reset(); // resets the matrix
        while (!stack.isEmpty()) {
            stack.pop();
        }
    }
    private void initialiseFields() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter dimensions of the floor");
        System.out.print("Enter y dimension: ");
        int n = scan.nextInt();
        System.out.print("Enter x dimension: ");
        int m = scan.nextInt();
        // initialise floor
        floor = new Floor(n,m);

        System.out.println("Enter starting point the bug");
        System.out.print("Enter x coordinate: ");
        int x = scan.nextInt();
        System.out.print("Enter y coordinate: ");
        int y = scan.nextInt();
        // initialise bug
        roach = new Bug(x,y);
    }
    private void customTrial() {
        initialiseFields();
        int steps = 0;
        do {
            bugMove();
            steps++;
        } while (tileChecker());
        System.out.println("It took " + ANSI_RED + steps + " steps " + ANSI_RESET + "to touch every tile");
        floor.printFloor();
    }

    private boolean moveChecker(int x, int y) {
        return (x < floor.tiles.length && x >= 0 && y >= 0 && y < floor.tiles[0].length);
    }

    private boolean tileChecker() {
        if (floor.tiles[roach.getBugX()][roach.getBugY()] == 1) {
            // first visit, push to stack
            stack.push(roach.getCurrentCoord());
        }
        Stack<Coord> holder = new Stack<>();
        int ctr = 0;
        while (!stack.isEmpty()) {
            holder.push(stack.pop());
            ctr++;
        }
        if (ctr == floor.numOfTiles()) {
            // all tiles visited
            return false;
        }
        while (!holder.isEmpty()) {
            stack.push(holder.pop());
        }
        return true;
    }

    private void bugMove() {
        /*
         * 7 0 1
         * 6 X 2
         * 5 4 3
         */
        int direction = r.nextInt(8);
        Coord current = roach.getCurrentCoord();
        if (direction == 0) {
            // north
            // it doesn't matter as long as I don't move the bug in the floor
            if (moveChecker(current.getX(), current.getY()-1)) {
                roach.updateY(current.getY()-1);
            }
        } else if (direction == 1) {
            //NE
            if (moveChecker(current.getX()+1, current.getY()-1)) {
                roach.updateCoord(current.getX()+1, current.getY()-1);

            }
        } else if (direction == 2) {
            // E
            if (moveChecker(current.getX()+1, current.getY())) {
                roach.updateX(current.getX()+1);
            }
        } else if (direction == 3) {
            // SE
            if (moveChecker(current.getX()+1, current.getY()+1)) {
                roach.updateCoord(current.getX()+1, current.getY()+1);
            }
        } else if (direction == 4) {
            // S
            if (moveChecker(current.getX(), current.getY()+1)) {
                roach.updateY(current.getY()+1);
            }
        } else if (direction == 5) {
            // SW
            if (moveChecker(current.getX()-1, current.getY()+1)) {
                roach.updateCoord(current.getX()-1, current.getY()+1);
            }
        } else if (direction == 6) {
            // W
            if (moveChecker(current.getX()-1, current.getY())) {
                roach.updateX(current.getX()-1);
            }
        } else { // direction 7 that is
            // NW
            if (moveChecker(current.getX()-1, current.getY()-1)) {
                roach.updateCoord(current.getX()-1, current.getY()-1);
            }
        }
        floor.tiles[roach.getBugX()][roach.getBugY()] += 1;
    }

    private void resultsToFile(Score[] scores, int[] n, int[] m, int [] x) throws IOException {
        FileWriter fw = new FileWriter("simresults.txt");
        fw.write(n.length + " different floors\n" + x.length + " different bug positions\n" + numberOfTrials + " trials\n");
        for (Score s : scores) {
            String sb = "Floor: " +
                    n[s.getFloorID()] +
                    'x' +
                    m[s.getFloorID()] +
                    "\n" +
                    "Bug Starting Pos: " +
                    s.getBugStart() +
                    "\n" +
                    "Moves: " +
                    s.getMove() +
                    "\n" +
                    "Time in milliseconds: " +
                    s.getTime() +
                    "\n------------------------------------\n";
            fw.write(sb);
        }
        fw.close();
    }
    private void bugSim() throws IOException {
        int[] nDim = {3,4,5,12,12};
        int[] mDim = {3,5,9,12,8};
        int[] bugX = new int[4]; // 0, mid, 2 randoms
        int[] bugY = new int[4];
        Score[] simResults = new Score[nDim.length*bugX.length];
        int scoreIdx = 0;
        bugX[0] = 0;
        bugY[0] = 0;
        for (int i = 0; i < nDim.length; i++) { // different floors
            floor = new Floor(nDim[i],mDim[i]);
            bugX[1] = nDim[i]/2;
            bugY[1] = mDim[i]/2;
            bugX[2] = r.nextInt(nDim[i]);
            bugY[2] = r.nextInt(mDim[i]);
            bugX[3] = r.nextInt(nDim[i]);
            bugY[3] = r.nextInt(mDim[i]);
            System.out.println("Running floor " + i+1);
            for (int j = 0; j < bugY.length; j++) { // different starting points
                roach = new Bug(bugX[j], bugY[j]);
                Coord coord = roach.getCurrentCoord();
                int a = coord.getX();
                int b = coord.getY();
                System.out.println("Deploy bug number " + j+1);
                long start = System.currentTimeMillis();
                int steps = bugTrial();
                System.out.println(numberOfTrials + " trials are completed in " + (System.currentTimeMillis()-start));
                Score score = new Score((System.currentTimeMillis()-start), steps, i, new Coord(a,b));
                simResults[scoreIdx] = score;
                scoreIdx++;
            } // roaches ends
        } // floors end / sim finished
        resultsToFile(simResults, nDim, mDim, bugX);
    }
    private int bugTrial() {
        int[] bench = new int[numberOfTrials];
        for (int i = 0; i < bench.length; i++) {
            int steps = 0;
            do {
                steps++;
                bugMove();
            } while (tileChecker());
            fin();
            bench[i] = steps;
        }
        int sum = 0;
        for (int j : bench) {
            sum += j;
        }
        return sum/ bench.length;
    }

    public static void main(String[] args) {
        Main m = new Main();
        Scanner scan = new Scanner(System.in);
        System.out.println(ANSI_BRIGHT_GREEN + "Welcome to TileStepper, a game of probability" + ANSI_RESET);
        System.out.println("This game has 2 modes. A" + ANSI_PURPLE + " sophisticated simulation " + ANSI_RESET + "and a" + ANSI_CYAN + " custom trial " + ANSI_RESET);
        System.out.println("I encourage you to try" + ANSI_BRIGHT_GREEN + " both" + ANSI_RESET);
        int choice;
        do {
            System.out.print("Enter" + ANSI_PURPLE + " 1" + ANSI_RESET +" for" + ANSI_PURPLE +" simulation"+ ANSI_RESET +
                " ," + ANSI_CYAN +" 2"+ ANSI_RESET +" for" + ANSI_CYAN + " custom trial" + ANSI_RESET);
            System.out.println(", and" + ANSI_RED + " 0" + ANSI_RESET +" to" + ANSI_RED +" exit");
            System.out.print(ANSI_BG_RED + ANSI_BLACK + ">"+ ANSI_RESET);
            choice = scan.nextInt();
            if (choice == 1) {
                // run simulation
                System.out.println(ANSI_PURPLE + "Simulation it is!"+ ANSI_BLACK);
                try {
                    m.bugSim();
                } catch (IOException e) {
                    System.out.println(ANSI_RED + "Something went wrong with files" + ANSI_RESET);
                }
                System.out.println(ANSI_RESET + "Simulation finished. Please check " + ANSI_RED + "simresults.txt" + ANSI_RESET + " for results and statistics");
            } else if (choice == 2) {
                // run custom trial
                System.out.println(ANSI_CYAN + "Custom run! Warm up your fingers because you will be entering some values!" + ANSI_RESET);
                m.customTrial();
            } else if (choice == 0) {
                // exit
                break;
            } else {
                // invalid input, try again
                System.out.println(ANSI_BG_RED + ANSI_BLACK + "Invalid input. Please try again" + ANSI_RESET);
            }
        } while (true);
        System.out.println("Goodbye");

    }

}
