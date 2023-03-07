public class Floor {
    public int[][] tiles;
    private int height;
    private int width;


    public Floor(int n, int m) {
        tiles = new int[n][m];
        height = n;
        width = m;
    }

    public void printFloor() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(tiles[i][j] + " ");
            }
            System.out.println();
        }
    }

    public int numOfTiles() {
        return height*width;
    }

    public void reset() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tiles[i][j] = 0;
            }
        }
    }
}
