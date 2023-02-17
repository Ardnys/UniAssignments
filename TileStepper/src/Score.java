public class Score {
    private long time;
    private int move;
    private int floorID;
    private Coord bugStart;

    public Score(long time, int move, int floorID, Coord bugStart) {
        this.time = time;
        this.move = move;
        this.floorID = floorID;
        this.bugStart = bugStart;
    }

    public long getTime() {
        return time;
    }

    public int getMove() {
        return move;
    }

    public int getFloorID() {
        return floorID;
    }

    public Coord getBugStart() {
        return bugStart;
    }
}
