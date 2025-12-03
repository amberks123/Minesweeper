import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Class to create the game board, including each hex cell and the entire grid.
 * Handles left and right clicks appropriately, detects game win/loss, and
 * creates a panel to be added to the overall frame.
 * @author Amber Smith
 */
public class GameBoardGUI {

    /** holds the number of rows and columns and size of a hex board */
    private final int numRows;
    private final int numCols;
    private final int hexSize;

    /** an array of HexCell objects to represent the game board */
    private HexCell[][] hexCells;

    /** final game board JPanel to be used for the game board GUI */
    JPanel gameBoard;

    /** color for the background of the panel */
    private final Color background = new Color(87, 130, 31);
    private final Color uncovered = new Color(208, 232, 176);
    private static final Color covered = new Color(144, 189, 51);

    /** boolean to represent whether a button has been clicked, game started */
    private boolean timerStarted = false;

    /** images to represent different board states */
    ImageIcon flag = new ImageIcon
            (GameBoardGUI.class.getResource("/TruffulaTree.png"));
    ImageIcon mine = new ImageIcon
            (GameBoardGUI.class.getResource("/LoraxMine.png"));
    ImageIcon one = new ImageIcon
            (GameBoardGUI.class.getResource("/one.PNG"));
    ImageIcon two = new ImageIcon
            (GameBoardGUI.class.getResource("/two.PNG"));
    ImageIcon three = new ImageIcon
            (GameBoardGUI.class.getResource("/three.PNG"));
    ImageIcon four = new ImageIcon
            (GameBoardGUI.class.getResource("/four.PNG"));
    ImageIcon five = new ImageIcon
            (GameBoardGUI.class.getResource("/five.PNG"));
    ImageIcon six = new ImageIcon
            (GameBoardGUI.class.getResource("/six.PNG"));

    /** HexMineManager object to manage the game logic */
    private static HexMineManager mineManager;

    /**
     * HexGrid class constructor, calls makeCells and makePanel
     */
    public GameBoardGUI(int numRows, int numCols, int size, int numMines) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.hexSize = size;
        mineManager = new HexMineManager(numMines, numRows, numCols);

        makeCells();
        makePanel();
    }

    /**
     * Initializes all the cells for the hex grid by correctly sizing them and
     * assigning a color.
     */
    private void makeCells() {
        hexCells = new HexCell[numRows][numCols];

        // find the width and height of each hex cell
        int width = hexSize * 2;
        int height = (int) (Math.sqrt(3) * hexSize);

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                int xBound = col * (int) (width * 0.75);
                int yBound = row * height + (col % 2) * (height / 2);
                Polygon polygon = makeHex(xBound + hexSize,
                        yBound + hexSize, hexSize);
                hexCells[row][col] = new HexCell(polygon);
            }
        }
    }

    /**
     * A helper method to makeCells that creates the individual hexagon
     * @param xCenter x coordinate of the center
     * @param yCenter y coordinate of the center
     * @param size size of the hexagon
     * @return hexagon to be used in a cell
     */
    private Polygon makeHex(int xCenter, int yCenter, int size) {
        Polygon polygon = new Polygon();
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(60 * i);
            polygon.addPoint(xCenter + (int) (size * Math.cos(angle)),
                    yCenter + (int) (size * Math.sin(angle)));
        }
        return polygon;
    }

    /**
     * Create the JPanel for the game board GUI
     */
    private void makePanel() {
        gameBoard = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGrid(g);
            }
        };

        int width = (int) (numCols * hexSize * 1.5);
        int height = (int) (numRows * (Math.sqrt(3) * hexSize));
        gameBoard.setPreferredSize(new Dimension(width, height));
        gameBoard.setBackground(background);

        gameBoard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e);
                if (!timerStarted) {
                    timerStarted = true;
                    HexMines.timerObject.start();
                }
            }
        });
    }

    /**
     * Handle a click on a hex cell. Determine which hex cell was clicked,
     * if it was a right or left click, and take the appropriate action.
     * @param e event of the click
     */
    private void handleClick(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        HexCell clickedCell = null;
        int clickedRow = 0;
        int clickedCol = 0;

        // iterate through the hex cells to determine which was clicked
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                if (hexCells[row][col].hex.contains(x, y)) {
                    clickedCell = hexCells[row][col];
                    clickedRow = row;
                    clickedCol = col;
                }
            }
        }

        if (clickedCell == null) {
            return;
        }

        if (SwingUtilities.isLeftMouseButton(e)) {
            leftClick(clickedRow, clickedCol);
        } else if (SwingUtilities.isRightMouseButton(e)) {
            rightClick(clickedRow, clickedCol);
        }
    }

    /**
     * handle the left click by checking if the cell is already uncovered
     * @param row row of the clicked cell
     * @param col column of the clicked cell
     */
    private void leftClick(int row, int col) {
        if (hexCells[row][col].hasNoImage()) {
            drawCellImage(mineManager.uncover(row,col),row,col);
            if (drawCellImage(mineManager.uncover(row,col),row,col)) {
                gameBoard.repaint();
                gameEnd(false);
            } else if (mineManager.checkForWin()) {
                gameBoard.repaint();
                gameEnd(true);
            }
        }
        gameBoard.repaint();
    }

    /**
     * Draw the appropriate image for an uncovered cell
     * @param cellState the character representing the cell state
     * @param row row of the uncovered cell
     * @param col column of the uncovered cell
     * @return true if game is over, false otherwise
     */
    public boolean drawCellImage(char cellState, int row, int col) {
        boolean gameOver = false;

        if (cellState == '.') {
            for (int r = 0; r < numRows; r++) {
                for (int c = 0; c < numCols; c++) {
                    char thisState = mineManager.grid[r][c];

                    if (thisState != 'c' & thisState != 'F') {
                        redrawSingleCell(thisState, r, c);
                    }
                }
            }
        } else {
            redrawSingleCell(cellState,row,col);
            if (cellState == 'M') {
                uncoverUnMinedCells();
                gameOver = true;
            }
        }
        return gameOver;
    }

    /**
     * Redraw a single cell on the hex grid, helper method to drawCellImage
     * @param cellState character representing the cell's state
     * @param row row of the cell
     * @param col column of the cell
     */
    private void redrawSingleCell(char cellState, int row, int col) {
        hexCells[row][col].color = uncovered;

        switch (cellState) {
            case '1':
                hexCells[row][col].setImage(one.getImage());
                break;
            case '2':
                hexCells[row][col].setImage(two.getImage());
                break;
            case '3':
                hexCells[row][col].setImage(three.getImage());
                break;
            case '4':
                hexCells[row][col].setImage(four.getImage());
                break;
            case '5':
                hexCells[row][col].setImage(five.getImage());
                break;
            case '6':
                hexCells[row][col].setImage(six.getImage());
                break;
            case 'M':
                hexCells[row][col].setImage(mine.getImage());
                break;
        }
    }

    /**
     * helper method used to uncover all cells without a mine if a mine is hit
     */
    public void uncoverUnMinedCells() {
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                if (!mineManager.hasMine[r][c]) {
                    drawCellImage(mineManager.grid[r][c],r,c);
                }
            }
        }
    }

    /**
     * handle right clicks by placing or removing flags
     * @param row row of the cell clicked
     * @param col column of the cell clicked
     */
    private void rightClick(int row, int col) {
        if (hexCells[row][col].color == covered &&
                hexCells[row][col].getImage() == null) {
            hexCells[row][col].setImage(flag.getImage());
            mineManager.toggleFlag(row, col);
            HexMines.numFlagsObject.editFlags(true);
        } else if (hexCells[row][col].getImage() == flag.getImage()) {
            hexCells[row][col].setImage(null);
            mineManager.toggleFlag(row, col);
            HexMines.numFlagsObject.editFlags(false);
        }

        gameBoard.repaint();
    }

    /**
     * Set the color and image of each hex cell and give a border color.
     * @param g graphics object used to paint
     */
    private void drawGrid(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                HexCell cell = hexCells[row][col];

                g2.setColor(cell.color);
                g2.fillPolygon(cell.hex);

                // if the hex cell has an image draw it
                if (cell.image != null) {
                    g2.setClip(cell.hex);
                    Rectangle b = cell.hex.getBounds();
                    g2.drawImage(cell.image, b.x, b.y,
                            b.width, b.height, null);
                    g2.setClip(null);
                }

                g2.setColor(background);
                g2.drawPolygon(cell.hex);
            }
        }
    }

    /**
     * If game is over, display an option pane that lets you choose to play
     * again or close the game
     * @param gameWon whether the game was won
     */
    private static void gameEnd(boolean gameWon) {
        HexMines.timerObject.stop();
        int result = GameEndGUI.showGameEnd(gameWon);

        if (result == 0) {
            HexMines.frame.getContentPane().removeAll();
            HexMines.resetGame();
            HexMines.makeFrame();
            HexMines.frame.revalidate();
            HexMines.frame.repaint();
        } else if (result == 1) {
            System.exit(0);
        }
    }

    /**
     * Get the game board JPanel
     * @return game board JPanel
     */
    public JPanel getGameBoard() {
        return gameBoard;
    }

    /**
     * Nested class for an object to represent each hex cell.
     */
    private static class HexCell {
        // hex cell characteristics
        Polygon hex;
        Color color = covered;
        Image image = null;

        private HexCell(Polygon hex) {
            this.hex = hex;
        }

        private void setImage(Image image) {
            this.image = image;
        }

        private boolean hasNoImage() {
            return image == null;
        }

        private Image getImage() {
            return image;
        }
    }
}