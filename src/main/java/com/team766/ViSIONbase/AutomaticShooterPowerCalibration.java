package com.team766.ViSIONbase;

import javax.swing.*;
import com.team766.framework.AprilTagErrorCode;
import com.team766.robot.Robot;
import edu.wpi.first.math.geometry.Transform3d;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;

public class AutomaticShooterPowerCalibration extends JFrame {
    private int tagId;
    private boolean ballWentIn;

    private ArrayList<Double> powersTried = new ArrayList<Double>();
    private ArrayList<Boolean> wasTooLong = new ArrayList<Boolean>();

    private ArrayList<Double> distancesWork = new ArrayList<Double>();
    private ArrayList<Double> powersWork = new ArrayList<Double>();

    private double latestDistance = -1;

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

                double distance = 0; //TODO: Find distance from target

                latestDistance = distance;

                double power = 0;

                if(powersTried.size() == 0){
                    if(powersWork.size() <= 1){
                        power = Math.random();
                    }else{
                        //Find a value near the current distance in the distancesWork array
                        //Then compute a new value for power according to what works
                        double closestOnMin = distancesWork.get(0);
                        double closestOnMax = distancesWork.get(0);

                        int indexMin = 0;
                        int indexMax = 0;

                        for(int i = 1; i < distancesWork.size(); i++){
                            if(distancesWork.get(i) < closestOnMin && Math.abs(distancesWork.get(i) - distance) < Math.abs(closestOnMin - distance)){
                                closestOnMin = distancesWork.get(i);
                                indexMin = i;
                            }
                            if(distancesWork.get(i) > closestOnMax && Math.abs(distancesWork.get(i) - distance) < Math.abs(closestOnMax - distance)){
                                closestOnMax = distancesWork.get(i);
                                indexMax = i;
                            }
                        }

                        double difference = closestOnMax - closestOnMin;
                        double powerDifference = powersWork.get(indexMax) - powersWork.get(indexMin);

                        double differenceFromMin = distance - closestOnMin;

                        power = powersWork.get(indexMin) + (differenceFromMin / difference) * powerDifference;
                    }
                } else {
                    if(powersTried.size() == 1){
                        if(wasTooLong.get(0)){
                            power = powersTried.get(0) - 0.1;
                        }else{
                            power = powersTried.get(0) + 0.1;
                        }
                    } else if (powersTried.size() == 2){
                        if(wasTooLong.get(0) && wasTooLong.get(1)){
                            power = powersTried.get(0) - 0.1;
                        }else if(wasTooLong.get(0) && !wasTooLong.get(1)){
                            power = powersTried.get(0) + 0.05;
                        }else if(!wasTooLong.get(0) && wasTooLong.get(1)){
                            power = powersTried.get(0) - 0.05;
                        }else{
                            power = powersTried.get(0) + 0.1;
                        }
                    } else {
                        int countLong = 0;
                        int countShort = 0;
                        for(int i = 0; i < powersTried.size(); i++){
                            if(wasTooLong.get(i)){
                                countLong++;
                            }else{
                                countShort++;
                            }
                        }

                        
                        if(countLong > countShort && !wasTooLong.get(wasTooLong.size() - 1)){ // for this case we have been doing a lot of long shots and we just got a close one
                            power = powersTried.get(powersTried.size() - 1) + 0.03;
                        } else if(countShort > countLong && wasTooLong.get(wasTooLong.size() - 1)){ // if we have been doing a lot of short shots and we just got a long one
                            power = powersTried.get(powersTried.size() - 1) - 0.03;
                        } else if (wasTooLong.get(wasTooLong.size() - 1) && !wasTooLong.get(wasTooLong.size() - 2)){ // if we just did a short shot and then a long shot
                            power = powersTried.get(powersTried.size() - 1) - 0.03;
                        } else if (!wasTooLong.get(wasTooLong.size() - 1) && wasTooLong.get(wasTooLong.size() - 2)){ // if we just did a long shot and then a short shot
                            power = powersTried.get(powersTried.size() - 1) + 0.03;
                        } else if (wasTooLong.get(wasTooLong.size() - 1)){
                            power = powersTried.get(powersTried.size() - 1) - 0.05;
                        } else {
                            power = powersTried.get(powersTried.size() - 1) + 0.05;
                        }
                    }

                }
                powersTried.add(power);
                Robot.shooter.shoot(power); //TODO: Make sure this runs for ample time
                
                ballInButton.setEnabled(true);
                ballOutButton.setEnabled(true);
            }
        });

        ballInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ballWentIn = true;
                // Store values or perform other actions when ball goes in

                if(latestDistance == -1){
                    JOptionPane.showMessageDialog(null, "Error: Distance was not found.");
                    return;
                }

                distancesWork.add(latestDistance);
                powersWork.add(powersTried.get(powersTried.size() - 1));

                JOptionPane.showMessageDialog(null, "Ball went in. Values stored.");

                powersTried.clear();
                wasTooLong.clear();

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
                    wasTooLong.add(false);
                    JOptionPane.showMessageDialog(null, "Ball went too short.");
                } else {
                    // Handle too long
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
        startButton.setEnabled(true);
        readyButton.setEnabled(false);
        ballInButton.setEnabled(false);
        ballOutButton.setEnabled(false);

        //finish if there are more than 7 values that work
        if (distancesWork.size() > 7){
            try {
                finish();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void finish() throws IOException {
        addDataToFile(createUniqueFileNameAndFile());
    }

    private void addDataToFile(File file) throws IOException {
        String filePath = file.getPath();
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            for (int i = 0; i < distancesWork.size(); i++) {
                printWriter.println(distancesWork.get(i));
                printWriter.println(powersWork.get(i));
            }
            printWriter.close();
        } catch (IOException e) {
            throw e;
        }
    }

    private File createUniqueFileNameAndFile() throws IOException {
        String fileName = "src/main/java/com/team766/ViSIONbase/ShooterValueDataGenerated.dfa";
        File file = new File(fileName);
        int i = 1;
        while (file.exists()) {
            fileName = "src/main/java/com/team766/ViSIONbase/ShooterValueDataGenerated" + i + ".dfa";
            file = new File(fileName);
            i++;
        }
        // create file
        File newFile = new File(fileName);
        newFile.createNewFile();

        return newFile;
        
    }

}