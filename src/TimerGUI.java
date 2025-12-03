import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Creates a timer panel to display the time since the game has started. The
 * game is started when the first hex cell is clicked and ends when the game
 * is won/lost.
 * @author Amber Smith
 */
public class TimerGUI {

    /** Objects to store the timer information for GUI */
    private final JPanel timerPanel;
    private final JLabel timerLabel;
    private final Timer timer;

    /** seconds timer has been running and whether the timer is started */
    private int totalSeconds;
    private boolean running = false;

    /**
     * Constructor for timer, makes a label and panel to put the timer in
     * @param frameWidth used to determine the size of the panel
     * @param color set the color of the panel
     */
    public TimerGUI(int frameWidth, Color color) {
        timerLabel = new JLabel("Time: 00:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        timerPanel = new JPanel();
        timerPanel.add(timerLabel);
        timerPanel.setPreferredSize
                ((new Dimension(frameWidth/4,50)));
        timerPanel.setBackground(color);

        totalSeconds = 0;

        timer = new Timer(1000,new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                totalSeconds++;
                updateTimerLabel();
            }
        });
    }

    /**
     * Method to update the timer label for change in seconds
     */
    private void updateTimerLabel() {
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds % 3600) / 60;

        String time = String.format("Time: %02d:%02d", minutes, seconds);
        timerLabel.setText(time);
    }

    /**
     * start the timer
     */
    public void start() {
        if (!running) {
            running = true;
            timer.start();
        }
    }

    /**
     * stop the timer
     */
    public void stop() {
        if (running) {
            timer.stop();
            running = false;
        }
    }

    /**
     * get the timer panel
     * @return timer panel
     */
    public JPanel getPanel() {
        return timerPanel;
    }
}