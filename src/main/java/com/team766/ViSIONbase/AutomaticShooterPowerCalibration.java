import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AutomaticShooterPowerCalibration extends JFrame {
    private int tagId;
    private boolean ballWentIn;

    public AutomaticShooterPowerCalibration() {
        setTitle("Automatic Shooter Power Calibration");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();

        JButton startButton = new JButton("Start");
        JButton readyButton = new JButton("Ready");
        JButton ballInButton = new JButton("Ball Went In");
        JButton ballOutButton = new JButton("Ball Went Out");

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tagId = Integer.parseInt(JOptionPane.showInputDialog("Enter Tag ID:"));
                JOptionPane.showMessageDialog(null, "Insert ball into shooter.");
                readyButton.setEnabled(true);
            }
        });

        readyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform actions when Ready button is clicked
                JOptionPane.showMessageDialog(null, "Ball launcher is ready.");
                ballInButton.setEnabled(true);
                ballOutButton.setEnabled(true);
            }
        });

        ballInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ballWentIn = true;
                // Store values or perform other actions when ball goes in
                JOptionPane.showMessageDialog(null, "Ball went in. Values stored.");
                reset();
            }
        });

        ballOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ballWentIn = false;
                int option = JOptionPane.showOptionDialog(null,
                        "Did the ball go too short or too long?",
                        "Ball Out",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"Too Short", "Too Long"},
                        "Too Short");

                if (option == JOptionPane.YES_OPTION) {
                    // Handle too short
                    JOptionPane.showMessageDialog(null, "Ball went too short.");
                } else {
                    // Handle too long
                    JOptionPane.showMessageDialog(null, "Ball went too long.");
                }
                reset();
            }
        });

        panel.add(startButton);
        panel.add(readyButton);
        panel.add(ballInButton);
        panel.add(ballOutButton);

        readyButton.setEnabled(false);
        ballInButton.setEnabled(false);
        ballOutButton.setEnabled(false);

        add(panel);
        setVisible(true);
    }

    private void reset() {
        readyButton.setEnabled(false);
        ballInButton.setEnabled(false);
        ballOutButton.setEnabled(false);
    }