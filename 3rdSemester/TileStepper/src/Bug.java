public class Bug {
    private Coord currentCoord;

    public Bug(int x, int y) {
        currentCoord = new Coord(x,y);
    }

    public void updateCoord(int x, int y) {
        currentCoord.setX(x);
        currentCoord.setY(y);
    }

    public void updateX(int x) {
        currentCoord.setX(x);
    }

    public void updateY(int y) {
        currentCoord.setY(y);
    }

    public Coord getCurrentCoord() {
        return currentCoord;
    }

    public int getBugX() {
        return currentCoord.getX();
    }

    public int getBugY() {
        return getCurrentCoord().getY();
    }
}
