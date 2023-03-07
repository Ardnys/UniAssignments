public class Coord {
    private int x;
    private int y;
    private boolean[] flags;
    /*
    idx: 0 1  2 3  4 5  6 7
    dir: E SE S SW W NW N NE
     */

    public Coord(int i, int j) {
        x = i;
        y = j;
        flags = new boolean[8];
    }
    public Coord(int i, int j, int flagIdx) {
        x = i;
        y = j;
        flags = new boolean[8];
        flags[flagIdx] = true;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public boolean getFlag(int flagIdx) {
        return !flags[flagIdx];
    }

    public void setFlag(int flagIdx) {
        flags[flagIdx] = true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Coord))
            return false;
        Coord c = (Coord) obj;
        return c.getX() == getX() && c.getY() == getY();
    }
}
