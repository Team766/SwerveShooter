package com.team766.ViSIONbase;

import javax.swing.*;
import com.team766.framework.AprilTagErrorCode;
import edu.wpi.first.math.geometry.Transform3d;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class AutomaticShooterPowerCalibration extends JFrame {
    private int tagId;
    private boolean ballWentIn;

    private ArrayList<Double> powersTried = new ArrayList<Double>();
    private ArrayList<Boolean> wasTooLong = new ArrayList<Boolean>();

    private ArrayList<Double> distancesWork = new ArrayList<Double>();
    private ArrayList<Boolean> powersWork = new ArrayList<Boolean>();

    JButton startButton = new JButton("Start");
    JButton readyButton = new JButton("Ready");
    JButton ballInButton = new JButton("Ball Went In");
    JButton ballOutButton = new JButton("Ball Went Out");

    
    public AutomaticShooterPowerCalibration() {
        setTitle("Automatic Shooter Power Calibration");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();



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

                JOptionPane.showMessageDialog(null, "Ball launcher is ready. Launching ball.");


                CameraPlus cameraToUse = null; // for now
                try{
                    cameraToUse = VisionForShooter.findCameraThatHas(tagId);
                } catch (AprilTagErrorCode t){
                    return; // lmao
                }

                Transform3d robotToTagId = null; // for now
                try{
                    robotToTagId = cameraToUse.getRobotToBestTag(); // we know this tag will have correct ID, so it will be annoying with the checked exception
                } catch (Exception q){
                    //this should never happen
                    //throw new AprilTagErrorCode("This error should never happen. If you are seeing this, something is messed up with the hardware and network settings (ping is probably really high).", 100);
                }

                double robotXtoTag = robotToTagId.getX();
                double robotYtoTag = robotToTagId.getY();
                double robotZtoTag = robotToTagId.getZ();

                //TODO: Talk to ryan about what is X Y and Z according to this. Like which dirrection it is facing.
                //TODO: Find distance from target
                //TODO: Use lookup table to find value. If there are no values in there currently, use Math.random() for power!!!!
                double power = 0;
                if(powersWork.size() == 0){
                    power = Math.random();
                }else{
                    //Find a value near the current distance in the distancesWork array
                    //Then compute a new value for power according to what works
                }

                ballInButton.setEnabled(true);
                ballOutButton.setEnabled(true);
            }
        });

        ballInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ballWentIn = true;
                // Store values or perform other actions when ball goes in


                //Speed = getSpeed()
                //Distance = getDistance()

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

                    // powersTried.add(getPower());
                    wasTooLong.add(false);
                    JOptionPane.showMessageDialog(null, "Ball went too short.");
                } else {
                    // Handle too long
                    // powersTried.add(getPower());
                    wasTooLong.add(true);
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

}