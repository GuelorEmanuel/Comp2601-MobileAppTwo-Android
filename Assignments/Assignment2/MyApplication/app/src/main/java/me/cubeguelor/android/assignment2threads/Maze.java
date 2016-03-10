package me.cubeguelor.android.assignment2threads;

/**
 * Created by Guelor on 16-02-20.
 */
class Maze extends Thread {

    private boolean startCell;
    private boolean destinationCell;
    private int indexStartCol;
    private int indexStartRow;
    private boolean isolved;
    private boolean isNotsolved;

    private int NUMROW;
    private int NUMCOLUMN;
    private int PATH = 1;
    private int STARTCELL = 2;
    private int ENDCELL   = 3;
    private int CELLVISITED = 4;

    private int THREADSLEEPDURATION = 400; // 4 sec


    private static Maze instance = null; //globally accessible instance
    private MainActivity mainActivityInstance;
    public static Maze getInstance(){return instance;}

    private  int[][] grid;

    //Constructor
    Maze(MainActivity mainActivityInstance, int row, int column) {

        destinationCell    = false;
        startCell          = false;
        isolved            = false;
        isNotsolved        = false;

        NUMROW    = row;
        NUMCOLUMN = column;

        grid = new int[NUMROW][NUMCOLUMN];
        this.mainActivityInstance = mainActivityInstance;

        for (int rowCounter = 0; rowCounter < NUMROW; rowCounter++)
            for (int columnCounter = 0; columnCounter < NUMCOLUMN; columnCounter++) {
                int brickOrGrass = (int)(2 * Math.random());  // add either 0 or 1
                grid[rowCounter][columnCounter] = brickOrGrass;
            }
    }

    public void run() {

        if (solve(indexStartRow, indexStartCol)) {
            isolved = true;
        }
        else {
            isNotsolved = true;
        }

    }

    //method fo Printing the maze
    public void print_maze () {

        System.out.println();

        for (int row=0; row < grid.length; row++) {
            for (int column=0; column < grid[row].length; column++)
                System.out.print (grid[row][column]);
            System.out.println();
        }
        System.out.println();
    }

    /*
     * Method : solve
     *   Attemots to recursively traverse the maze. It inserts special
     *   characters indicating location that have been tried and that
     *   eventually become part of the solution
     * @param int, int
     * @return bool
     */
    public boolean solve (int row, int column) {
        boolean done = false;

        if (valid(row, column)) {


            if (grid[row][column] == PATH)
                grid[row][column] = CELLVISITED;

            if (grid[row][column] == ENDCELL)
                    done = true;  // maze is solved
            else {
                try {
                    mainActivityInstance.getHandler().post(new Runnable() {
                        public void run() {
                            mainActivityInstance.setGrid(grid);
                        }
                    });
                    Thread.sleep(THREADSLEEPDURATION);
                }catch(InterruptedException e) {
                    e.printStackTrace();

                }
                done = solve(row + 1, column);  // down
                if (!done)
                    done = solve(row, column + 1);  // right

                if (!done)
                    done = solve(row - 1, column);  // up
                if (!done)
                    done = solve(row, column - 1);  // left
                }
                if (done)  // part of the final path
                    if (grid[row][column] != ENDCELL)
                        grid[row][column] = CELLVISITED;
        }
        return done;

    }

    /*
     * Method : valid
     *   Determines if a specific location is valid
     * @param int, int
     * @return bool
     */
    private boolean valid (int row, int column) {
        boolean result = false;

        // check if cell is in the bounds of the matrix
        if (row >= 0 && row < grid.length &&
                column >= 0 && column < grid[0].length) {

            //  check if cell is not blocked and not previously tried
            if (grid[row][column] == PATH)
                result = true;
            if (grid[row][column] == ENDCELL || grid[row][column] == STARTCELL)
                result = true;
        }
        return result;
    }


    // Set and get Methods
    public int[][] getGrid() {return grid;}
    public void setCell(int row, int column, int num) { grid[row][column] = num;}
    public void setStartCell (boolean sCell) {startCell = sCell;}
    public void setDestinationCell (boolean dCell) {destinationCell = dCell;}
    public void setIndexStartCol(int indexCol) { indexStartCol = indexCol;}
    public void setIndexStartRow(int indexRow) { indexStartRow = indexRow;}


    public boolean isStartCell() {return startCell;}
    public boolean isDestinationCell() {return destinationCell;}
    public boolean isIsolved() {return isolved;}
    public boolean isNotsolved() {return isNotsolved;}


}
