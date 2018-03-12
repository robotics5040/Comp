package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.Range;

/**
 * Created by jedch on 1/17/2018.
 */

public class RelicDeliverySystem {

    private HardwareOmniRobot robot;

    private double increment = 0.04;

    /**
     * Instantiates the <code>RelicDeliverySystem</code>
     *
     * @param robot the robot object that contains the needed hardware for the relic slide
     */
    public RelicDeliverySystem(HardwareOmniRobot robot) {
        //Initialize the robot based on the given one
        this.robot = robot;
    }

    /**
     * <code>moveSlide</code> is used to move the relic slide in and out
     *
     * @param joystick a double value of a joystick that moves the slide forward
     */
    public void moveSlide(double joystick) {
        //Sets the poser to a default of zero
        double power = 0.0;

        //Deactivates the relic stopper and moves the slide forward based on the joystick
        if (joystick < -0.1) {
            robot.relicStopper.setPosition(1);
            power = -1.0;
        } else if (joystick > 0.1) {
            robot.relicStopper.setPosition(1);
            power = 1.0;
        }

        //Set the power to the slide
        robot.relicMotor.setPower(power);
    }

    /**
     * <code>moveWrist</code> is used to move the wrist on the relic slide
     *
     * @param joystick      a double value from the controller that is used to move the wrist
     * @param halfwayButton a boolean from the controller that moves the relic wrist to the
     *                      halfway position
     */
    public void moveWrist(double joystick, boolean halfwayButton) {
        // Fetch the actual value of the wrist
        double rwCurrent = robot.relicWrist.getPosition(), rwGoal = rwCurrent;

        //Change the speed of the wrist if it excedes the position 0.3;
        if (rwCurrent > 0.70) {
            increment = 0.01;
        } else {
            increment = 0.04;
        }

        //Move the servo based on the speed
        if (joystick < -0.1) {
            rwGoal = rwCurrent - increment;
        } else if (joystick > 0.1) {
            rwGoal = rwCurrent + increment;
        }

        if (halfwayButton) {
            rwGoal = 0.50;
        }

        //Make sure that the new position isn't out of bounds
        Range.clip(rwGoal, 0.0, 1.0);

        //Apply the new position
        robot.relicWrist.setPosition(rwGoal);
    }

    /**
     * Opens the claw to various grips based on the inputted buttons
     *
     * @param buttonOpen
     * @param buttonPartway
     */
    public void openClaw(boolean buttonOpen, boolean buttonPartway) {
        if (buttonOpen) {
            robot.relicClaw.setPosition(0.3);
        } else if (buttonPartway) {
            robot.relicClaw.setPosition(0.546);
        } else {
            robot.relicClaw.setPosition(0.6);
        }
    }
}