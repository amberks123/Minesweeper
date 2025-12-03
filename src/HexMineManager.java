/**
 * HexMineManager manages the game bookkeeping for Minesweeper on a hexagonal
 * grid that is roughly rectangular.
 * @author Amber Smith
 */
public class HexMineManager {

    /** Characters to represent the state of the cell on the board */
    private final char covered = 'c';
    private final char flagged = 'F';

    /** Number of mines, rows, and columns in the grid */
    public final int numMines;
    public final int rows;
    public final int cols;

    /** 2D array for character grid and grid of mine locations */
    public final char[][] grid;
    public final boolean[][] hasMine;

    /**
     * Constructor for a new hex board
     * @param numMines number of mines on the board
     * @param rows number of rows
     * @param cols number of columns
     */
    public HexMineManager(int numMines, int rows, int cols) {
        this.numMines = numMines;
        this.rows = rows;
        this.cols = cols;

        // initialize the covered grid
        grid = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = covered;
            }
        }

        // initialize a grid of mines, false if there is no mine
        hasMine = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                hasMine[i][j] = false;
            }
        }

        // add in specified number of mines at random location
        int n = 0;
        while (n < numMines) {
            int ranRow = (int)(Math.random() * rows);
            int ranCol = (int)(Math.random() * cols);
            if (!hasMine[ranRow][ranCol]) {
                hasMine[ranRow][ranCol] = true;
                n++;
            }
        }
    }

    /**
     * Create a roughly rectangular hexagonal grid string representation
     * @return the string representation
     */
    @Override
    public String toString() {
        StringBuilder board = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (j % 2 == 1) {
                    board.append(' ');
                }
                board.append(grid[i][j]).append(' ');
            }
            board.append("\n");
        }
        return board.toString();
    }

    /**
     * Flag covered cell and remove the flag from an already flagged cell
     * @param row cell row
     * @param col cell column
     */
    public void toggleFlag(int row, int col) {
        if (grid[row][col] == flagged) {
            grid[row][col] = covered;
        } else if (grid[row][col] == covered) {
            grid[row][col] = flagged;
        }
    }

    /**
     * Create 6 directional vectors to check the cells adjacent to the current
     * cell. Directions change based on whether the row is even or odd due to
     * the grid's hexagonal setup.
     * @param col the col of the current cell
     * @return the directional vectors to be added to the cell's row and col
     */
    private static int[][] neighbors(int col) {
        return (col % 2 == 0) ?
                new int[][]{{-1,-1},{-1,0},{-1,1},{0,-1},{1,0},{0,1}}:
                new int[][]{{0,-1},{-1,0},{0,1},{1,-1},{1,0},{1,1}};
    }

    /**
     * Uncover a cell. If the cell has a mine, uncover all un-mined cells. If
     * the cell doesn't have a mine, reveal number of mine neighbors. If there
     * are no mine neighbors, uncover neighbors until all have adjacent mines.
     * @param row cell row
     * @param col cell column
     * @return character that represents the state of the cell
     */
    public char uncover(int row, int col) {
        // don't uncover a cell if flagged or already uncovered
        char uncovered = '.';
        if (grid[row][col] == flagged || grid[row][col] == uncovered ||
                (grid[row][col] >= '1' && grid[row][col] <= '6')) {
            return grid[row][col];
        }

        // if the cell contains a mine, uncover all un-mined cells.
        if (hasMine[row][col]) {
            char mine = 'M';
            grid[row][col] = mine;
            revealCells();
            return grid[row][col];
        }

        // count how many mine neighbors the cell has
        int mineNeighbors = 0;
        int[][] directions = neighbors(col);
        for (int[] dir : directions) {
            int adjRow = row + dir[0];
            int adjCol = col + dir[1];

            if (adjRow >= 0 && adjRow < rows && adjCol >= 0 && adjCol < cols) {
                if (hasMine[adjRow][adjCol]) mineNeighbors++;
            }
        }

        // reveal the number of mine neighbors if there are any
        if (mineNeighbors > 0) {
            grid[row][col] = (char) ('0' + mineNeighbors);
            return grid[row][col];
        }

        // if there are no mine neighbors, uncover other neighbors
        grid[row][col] = uncovered;
        for (int[] dir : directions) {
            int adjRow = row + dir[0];
            int adjCol = col + dir[1];

            if (adjRow>=0 && adjRow<rows && adjCol>=0 && adjCol<cols) {
                uncover(adjRow,adjCol);
            }
        }
        return grid[row][col];
    }

    /**
     * Helper method to uncover, used to reveal all un-mined cells if a mine
     * is hit.
     */
    private void revealCells() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!hasMine[i][j]) uncover(i,j);
            }
        }
    }

    /**
     * Check for a win by determining if the cell is covered or flagged and if
     * the cell has a mine
     * @return false if the game has not been won, true otherwise
     */
    public boolean checkForWin() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if((grid[i][j] == 'c' || grid[i][j] == 'F') && !hasMine[i][j]){
                    return false;
                }
            }
        }
        return true;
    }
}