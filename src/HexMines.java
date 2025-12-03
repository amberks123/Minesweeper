import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * The main class, which uses panels from all other classes. Assembles the
 * panels into one frame, adds a button to change the difficulty, adds
 * background music, and resets the bookkeeping for a new game.
 * @author Amber Smith
 */
public class HexMines extends JFrame {

    /** difficulty setting of the game */
    private static String difficulty = "Easy";

    /** JPanel to represent the game board and the frame for GUI */
    private static JPanel gameBoard;
    public static JFrame frame;

    /** rows, columns, size, and number of mines for an easy and hard grid */
    private static final int[] easyGrid = new int[]{10, 12, 30, 15};
    private static final int[] hardGrid = new int[]{14, 16, 30, 30};

    /** width and height for frame, assuming starting in an easy game */
    private static int frameWidth = 570;
    private static int frameHeight = 630;

    /** color for panels in the top section of the GUI */
    private static final Color topColor = new Color(255,163,45);

    /** starting number of mines assuming an easy game */
    private static int numMines = easyGrid[3];

    /** Objects for the timer and number of flags panels */
    public static TimerGUI timerObject = new TimerGUI(frameWidth,topColor);
    public static NumFlagsGUI numFlagsObject =
            new NumFlagsGUI(topColor,frameWidth, numMines);

    /** a clip for background music in the game */
    private static Clip backgroundMusic;

    /**
     * Set up the board for a game, smaller for an easy game and bigger for
     * a hard game
     */
    private static void getGameBoard() {
        GameBoardGUI grid;
        if (difficulty.equals("Easy")) {
            grid = new GameBoardGUI
                    (easyGrid[0], easyGrid[1], easyGrid[2], easyGrid[3]);
        } else {
            grid = new GameBoardGUI
                    (hardGrid[0], hardGrid[1], hardGrid[2], hardGrid[3]);
        }
        gameBoard = grid.getGameBoard();
    }

    /**
     * Get a JPanel to display the scores of number of mines and flags placed
     * @return JPanel for scores
     */
    private static JPanel getScores() {
        JPanel scores = new JPanel(new BorderLayout());

        // set up the score for number of mines on the board
        JPanel numMines = new JPanel();
        numMines.setBackground(topColor);
        numMines.setPreferredSize(new Dimension(frameWidth/4,50));

        // set up the score to show how many flags are remaining to place
        JPanel numFlags = numFlagsObject.getPanel();

        // set up the text for the number of mines
        JLabel mineLabel = new JLabel(HexMines.numMines + " Mines");
        mineLabel.setFont(new Font("Arial", Font.BOLD, 24));
        numMines.add(mineLabel);

        scores.add(numMines, BorderLayout.EAST);
        scores.add(numFlags, BorderLayout.WEST);

        return scores;
    }

    /**
     * Get a JPanel to display the timer of how long it has been since the game
     * was started, when the first click is made
     */
    private static JPanel getInitialTimer() {
        return timerObject.getPanel();
    }

    /**
     * Get a button to allow toggling of the game difficulty that switches
     * when pressed.
     * @return Button to change difficulty
     */
    private static JButton getDifficulty() {
        JButton setDifficulty = new JButton("Difficulty: " + difficulty);
        setDifficulty.setPreferredSize
                ((new Dimension(frameWidth/4,50)));
        setDifficulty.setBackground(topColor);
        setDifficulty.setFont(new Font("Arial", Font.BOLD, 14));


        setDifficulty.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (difficulty.equals("Easy")) {
                    difficulty = "Hard";
                    frameWidth = 750;
                    frameHeight = 835;
                    numMines = hardGrid[3];
                } else {
                    difficulty = "Easy";
                    frameWidth = 570;
                    frameHeight = 630;
                    numMines = easyGrid[3];
                }
                setDifficulty.setText("Difficulty: " + difficulty);

                // reset the frame for new difficulty
                if (gameBoard != null) {
                    frame.getContentPane().removeAll();
                    timerObject = new TimerGUI(frameWidth, topColor);
                    numFlagsObject =
                            new NumFlagsGUI(topColor,frameWidth, numMines);
                }

                makeFrame();
                toggleMusic();
                frame.getContentPane().revalidate();
                frame.getContentPane().repaint();
            }
        });

        return setDifficulty;
    }

    /**
     * Start/stop background music, music is restarted when a new game begins
     */
    private static void toggleMusic() {
        try {
            // stop background music if it is already playing
            if (backgroundMusic != null && backgroundMusic.isRunning()) {
                backgroundMusic.stop();
                backgroundMusic.close();
            }

            InputStream musicSource =
                    HexMines.class.getResourceAsStream("/LoraxMusic.wav");
            if (musicSource != null) {
                InputStream bufferedIn = new BufferedInputStream(musicSource);
                AudioInputStream audioInput =
                        AudioSystem.getAudioInputStream(bufferedIn);
                backgroundMusic = AudioSystem.getClip();
                backgroundMusic.open(audioInput);
                backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
                backgroundMusic.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Assemble the panels to make the frame
     */
    public static void makeFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(frameWidth,frameHeight);
        frame.setLayout(new BorderLayout());

        getGameBoard();
        toggleMusic();
        JPanel timer = getInitialTimer();
        JPanel scores = getScores();
        JButton setDifficulty = getDifficulty();

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(600,50));
        topPanel.add(scores, BorderLayout.CENTER);
        topPanel.add(timer, BorderLayout.EAST);
        topPanel.add(setDifficulty, BorderLayout.WEST);

        frame.setLocationRelativeTo(null);
        frame.add(gameBoard, BorderLayout.CENTER);
        frame.add(topPanel, BorderLayout.NORTH);
    }

    /**
     * Reset the game bookkeeping for a new game
     */
    public static void resetGame() {
        timerObject = new TimerGUI(frameWidth,topColor);
        numFlagsObject = new NumFlagsGUI(topColor,frameWidth,numMines);
        getGameBoard();
    }

    public static void main(String[] args) {
        frame = new JFrame("Minesweeper");
        makeFrame();
        frame.setVisible(true);
    }
}