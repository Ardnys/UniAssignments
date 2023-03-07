import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

public class MSolver {

    private final String path;
    private int[][] maze;
    private Stack<Coord> stack;

    public MSolver(String path) {
        this.path = path;
        stack = new Stack<>();
    }

    public static void main(String[] args) throws IOException {
        MSolver m = new MSolver("maze1.txt");
        m.readText();
        m.mazeLogic();

        System.out.println("END OF PROGRAM");
    }

    private void readText() throws IOException {
        File file = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(file));
        int cols, rows;
        String firstLine;
        firstLine = br.readLine();
        cols = firstLine.length(); // gets the number of columns in the maze
        rows = getNLines(); // gets the number of lines in the maze (to initialise the matrix)
        maze = new int[rows+2][cols+2]; // for the one frame
        oneFrame(maze);
        for (int i = 1; i < cols+1; i++) {
            if (firstLine.charAt(i-1) == '1' || firstLine.charAt(i-1) == '0')
                maze[1][i] = firstLine.charAt(i-1)-48;
        }
        int i = 2;
        String st;
        while ((st = br.readLine()) != null) {
            for (int j = 1; j < cols+1; j++) {
                if (st.charAt(j-1) == '1' || st.charAt(j-1) == '0')
                    maze[i][j] = st.charAt(j-1)-48;
            }
            i++;
        }
        printMat(maze);
//        return maze;
    }

    private int getNLines() throws IOException {
        List<String> fileStream = Files.readAllLines(Paths.get(path));
        return fileStream.size();
    }

    private void oneFrame(int[][] m) {
        // assume that it already has enough space for a frame of ones
        int rows = m.length, cols = m[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if ((j == 0 || j == cols-1) || (i==0 || i == rows-1))
                    m[i][j] = 1;
                else {
                    m[i][j] = 0;
                }
            }
        }
    }

    private static void printMat(int[][] arr) {
        for (int[] ints : arr) {
            for (int l = 0; l < arr[0].length; l++) {
                System.out.print(ints[l]);
            }
            System.out.println();
        }
    }

    private void mazeLogic() {
        // start index is 1,1
        // end index is m.length-1,m[0].length-1
        Coord start = new Coord(1,1);
        Coord endCoord = new Coord(maze.length-2, maze[0].length-2);
        int cnt = 0;
        Coord c = bugMove(start);
        while (true) {
            if (c.equals(endCoord)) {
                // ladies and gentlemen
                // we got em
                cnt++;
                System.out.println("ladies and gentlemen we found em" + cnt);
                cheers();
            }
            else if (c.equals(start)) {
                // it's done traversin the digital matrix that is
                // the maze we trapped ourselves
                // it's over, solution is found
                // or maybe not
                // you can terminate now
                goodbye();
            }
            try {
                c=bugMove(c);
            } catch (EmptyStackException e) {
                goodbye();
                return;
            }

        }

    }

    private void cheers() {
        myStack<Coord> solutionPath = new myStack<>();
        myStack<Coord> saveStack = new myStack<>();
        while(!stack.isEmpty()) {
            Coord c = stack.pop();
            solutionPath.push(c);
            saveStack.push(c);
        }
        while (!saveStack.isEmpty()) {
            stack.push(saveStack.pop());
        }
        char[][] pathMatrix = solMat(maze.length-2, maze[0].length-2);
        pavePath(pathMatrix, solutionPath);
    }

    private static char[][] solMat(int x, int y) {
        char[][] s = new char[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                s[i][j] = '\u25a3';
            }
        }
        return s;
    }

    private void pavePath(char[][] m, myStack<Coord> solutionPath) {
        m[0][0] = '\u25a2';
        while (!solutionPath.isEmpty()) {
            Coord c = solutionPath.pop();
            m[c.getX()-1][c.getY()-1] = '\u25a2';
        }
        for (char[] chars : m) {
            for (int j = 0; j < m[0].length; j++) {
                char a = chars[j];
                System.out.print(a);
            }
            System.out.println();
        }
    }

    private boolean beenhere(Coord c) {
        myStack<Coord> tempStack = new myStack<>();
        boolean ere = false;
        Coord top = null;
        if (!stack.isEmpty())
            top = stack.pop();
        while (!stack.isEmpty()) {
            Coord n = stack.peek();
            if (c.equals(n)) {
                ere = true;
                break;
            }
            tempStack.push(stack.pop());
        }
        while (!tempStack.isEmpty()) {
            stack.push(tempStack.pop());
        }
        if (top != null)
            stack.push(top);
        return !ere;
    }

    private Coord bugMove(Coord c) throws EmptyStackException{
        int i = c.getX(), j = c.getY();
        if (maze[i][j+1] == 0 && c.getFlag(0) && beenhere(c)) {
            // east
            // cus it moved to east flag that coord so that
            // it can't move there again
            Coord n = new Coord(i,j+1);
            c.setFlag(0);
            n.setFlag(4);
            stack.push(n);
            return n;
        }
        if (maze[i+1][j+1] == 0 && c.getFlag(1) && beenhere(c)) {
            // southeast
            Coord n = new Coord(i+1,j+1);
            c.setFlag(1);
            n.setFlag(5);
            stack.push(n);
            return n;
        }
        if (maze[i+1][j] == 0 && c.getFlag(2) && beenhere(c)) {
            // south
            Coord n = new Coord(i+1,j);
            c.setFlag(2);
            n.setFlag(6);
            stack.push(n);
            return n;
        }
        if (maze[i+1][j-1] == 0 && c.getFlag(3) && beenhere(c)) {
            // southwest
            Coord n = new Coord(i+1,j-1);
            c.setFlag(3);
            n.setFlag(7);
            stack.push(n);
            return n;
        }
        if (maze[i][j-1] == 0 && c.getFlag(4) && beenhere(c)) {
            // west
            Coord n = new Coord(i,j-1);
            c.setFlag(4);
            n.setFlag(0);
            stack.push(n);
            return n;
        }
        if (maze[i-1][j-1] == 0 && c.getFlag(5) && beenhere(c)) {
            // northwest
            Coord n = new Coord(i-1,j-1);
            c.setFlag(5);
            n.setFlag(1);
            stack.push(n);
            return n;
        }
        if (maze[i-1][j] == 0 && c.getFlag(6) && beenhere(c)) {
            // north
            Coord n = new Coord(i-1,j);
            c.setFlag(6);
            n.setFlag(2);
            stack.push(n);
            return n;
        }
        if (maze[i-1][j+1] == 0 && c.getFlag(7) && beenhere(c)) {
            // northeast
            Coord n = new Coord(i-1,j+1);
            c.setFlag(7);
            n.setFlag(3);
            stack.push(n);
            return n;
        }
        // backtrack
        stack.pop();
        return stack.peek();
    }

    private void goodbye() {
        System.out.println("farewell");
    }

}
