/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * This file provides basic Teleop driving for an Omni-wheeled robot (omnibot for short).
 * The code is structured as an Iterative OpMode
 *
 * This OpMode uses the common Pushbot hardware class to define the devices on the robot.
 * All device access is managed through the HardwarePushbot class.
 *
 * This particular OpMode executes a basic Omni Drive Teleop for an Omnibot
 * It raises and lowers the claw using the Gampad Y and A buttons respectively.
 * It also opens and closes the claws slowly using the left and right Bumper buttons.
 *
 */

@TeleOp(name="Pushbot: Omnibot Pushbot Start-X", group="Pushbot")
//@Disabled
public class OmniBot_Iterative extends OpMode{
    private double position = 0.0;
    public int  pressed = 0,up=10;

    /**
     * The instance fields: <code>gamepadMode1</code> and <code>gamepadMode2</code> determine which controller is using the alternative controll scheme.
     */
    private int gamepadMode1 = 0, gamepadMode2 = 0;


    double wrist_num = 0;
    boolean run2=false, goup = false,dumperReset = false, grabberDown=true, run =false, grabberReset=false;


    /**
     * Directional Variables; <code>front-facing, back-facing, left-facing,</code> and <code>right-facing</code> refer towich direction the robot
     * move towards.
     * *Note: the front of the robot refers to the side with the claw/grabber mechanism
     */
    private boolean frontFacing = true, rightFacing = false, leftFacing = false, backFacing = false;

    /**
     *  <code>isStartXPressable</code> is used for the toggle of start-x, it is used to make switching between the two
     *  modes easier to accomplish.
     */
    private boolean isStartXPressable = true;


    boolean closed = true;

    private double front, side, rotate;

    ElapsedTime runtime = new ElapsedTime();
    /* Declare OpMode members. */
    private HardwareOmniRobot robot; // use the class created to define a Pushbot's hardware
    private RelicDeliverySystem rds;

    public OmniBot_Iterative() {
        robot = new HardwareOmniRobot();
        rds   = new RelicDeliverySystem(robot);
    }

    // could also use HardwarePushbotMatrix class.
    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        /*
         * Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap, false);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        robot.grabber.setPower(0.6);
        robot.dumper.setPower(0.5);
        robot.jknock.setPosition(0.8);
        robot.claw1.setPosition(0.65);
        robot.claw2.setPosition(0.35);
        robot.relicClaw.setPosition(0.6);
        robot.glyphStop.setPosition(0.1);
        robot.relicWrist.setPosition(0.02);
        robot.relicStopper.setPosition(0.0);

    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        //note: The joystick goes negative when pushed forwards, so negate it)

        /*
         *   Gamepad 1
         */
        //Joystick Inputs
        double left_stick_y1  = gamepadMode1 == 0 ? gamepad1.left_stick_y : 0; //Basicly an if statement that
        double right_stick_y1 = gamepadMode1 == 0 ? gamepad1.right_stick_y : 0;
        //right_trigger1  = gamepad1.right_trigger;//unused
        double left_stick_x1   = gamepad1.left_stick_x;
        double right_stick_x1  = gamepad1.right_stick_x;
        boolean stick_press1 = gamepad1.left_stick_button || gamepad1.right_stick_button;

        //Bumpers and Triggers
        boolean left_bumper1  = gamepad1.left_bumper  && gamepadMode1 == 0;
        boolean right_bumper1 = gamepad1.right_bumper && gamepadMode1 == 0;
        boolean left_trigger1  = gamepad1.left_trigger  > 0.3 && gamepadMode1 == 0;//unused
        boolean right_trigger1 = gamepad1.right_trigger > 0.3 && gamepadMode1 == 0;

        //Button inputs
        boolean b_button1 = gamepad1.b && gamepadMode1 == 0;
        boolean a_button1 = gamepad1.a && gamepadMode1 == 0;
        boolean y_button1 = gamepad1.y && gamepadMode1 == 0;
        boolean x_button1 = gamepad1.x && gamepadMode1 == 0;
        boolean home_button1 = gamepad1.guide;

        //Directional Pad Inputs
        boolean dup1    = gamepad1.dpad_up    && gamepadMode1 == 0;
        boolean ddown1  = gamepad1.dpad_down  && gamepadMode1 == 0;
        boolean dleft1  = gamepad1.dpad_left  && gamepadMode1 == 0;
        boolean dright1 = gamepad1.dpad_right && gamepadMode1 == 0;

        //Auxillary Inputs
        boolean left_stick_press1  = gamepad1.right_stick_button && gamepadMode1 == 0;
        boolean right_stick_press1 = gamepad1.left_stick_button  && gamepadMode1 == 0;
        boolean home   = gamepad1.guide && gamepadMode1 == 0;
        boolean back1   = gamepad1.back  && gamepadMode1 == 0;
        boolean start1 = gamepad1.start && gamepadMode1 == 0;

        /*
         *   Gamepad 2
         */
        //Joystick Inputs
        double left_stick_y2   = gamepadMode2 == 0 ? gamepad2.left_stick_y : 0;
        double right_stick_y_2 = gamepadMode2 == 0 ? gamepad2.right_stick_y : 0;
        double left_stick_x2   = gamepad2.left_stick_x;
        double right_stick_x2  = gamepad2.right_stick_x;

        //Bumpers and Triggers
        boolean left_bumper2  = gamepad2.left_bumper  && gamepadMode2 == 0;
        boolean right_bumper2 = gamepad2.right_bumper && gamepadMode2 == 0;
        boolean left_trigger2  = gamepad2.left_trigger > 0.3  && gamepadMode2 == 0;
        boolean right_trigger2 = gamepad2.right_trigger > 0.3 && gamepadMode2 == 0;

        //Button inputs
        boolean back2 = gamepad2.back && gamepadMode2 == 0;
        boolean b_button2 = gamepad2.b && gamepadMode2 == 0;
        boolean a_button2 = gamepad2.a && gamepadMode2 == 0;
        boolean y_button2 = gamepad2.y && gamepadMode2 == 0;
        boolean x_button2 = gamepad2.x && gamepadMode2 == 0;

        //Directional Pad Inputs
        boolean dup2    = gamepad2.dpad_up    && gamepadMode2 == 0;
        boolean ddown2  = gamepad2.dpad_down  && gamepadMode2 == 0;
        boolean dleft2  = gamepad2.dpad_left  && gamepadMode2 == 0;
        boolean dright2 = gamepad2.dpad_right && gamepadMode2 == 0;

        //Auxillary Inputs
        boolean left_stick_press2  = gamepad2.right_stick_button && gamepadMode2 == 0;
        boolean right_stick_press2 = gamepad2.left_stick_button  && gamepadMode2 == 0;
        boolean home2 = gamepad2.guide  && gamepadMode2 == 0;
        boolean start2 = gamepad2.start && gamepadMode2 == 0;

        /*
         *   Gamepad 3
         */
        //Joystick Inputs
        double left_stick_y3  = (gamepad2.left_stick_y * gamepadMode2) + (gamepad1.left_stick_y * gamepadMode1);
        double right_stick_y3 = (gamepad2.right_stick_y * gamepadMode2) + (gamepad1.right_stick_y * gamepadMode1);
        double left_stick_x3  = (gamepad2.left_stick_x * gamepadMode2) + (gamepad1.left_stick_x * gamepadMode1);
        double right_stick_x3 = (gamepad2.right_stick_x * gamepadMode2) + (gamepad1.right_stick_x * gamepadMode1);
        boolean right_trigger3 = (gamepad2.right_trigger > 0.3 && gamepadMode2 == 1) || (gamepad1.right_trigger > 0.3 && gamepadMode1 == 1);
        boolean left_trigger3  = (gamepad2.left_trigger  > 0.3 && gamepadMode2 == 1) || (gamepad1.left_trigger > 0.3 && gamepadMode1 == 1);
        boolean right_bumper3 = (gamepad2.right_bumper && gamepadMode2 == 1) || (gamepad1.right_bumper && gamepadMode1 == 1);
        boolean left_bumper3  = (gamepad2.left_bumper  && gamepadMode2 == 1) || (gamepad1.left_bumper  && gamepadMode1 == 1);

        //Button inputs
        boolean b_button3 = (gamepad2.b && gamepadMode2 == 1) || (gamepad1.b && gamepadMode1 == 1);
        boolean a_button3 = (gamepad2.a && gamepadMode2 == 1) || (gamepad1.a && gamepadMode1 == 1);
        boolean y_button3 = (gamepad2.y && gamepadMode2 == 1) || (gamepad1.y && gamepadMode1 == 1);
        boolean x_button3 = (gamepad2.x && gamepadMode2 == 1) || (gamepad1.x && gamepadMode1 == 1);

        //Directional Pad Inputs
        boolean dup3    = (gamepad2.dpad_up    && gamepadMode2 == 1) || (gamepad1.dpad_up    && gamepadMode1 == 1);
        boolean ddown3  = (gamepad2.dpad_down  && gamepadMode2 == 1) || (gamepad1.dpad_down  && gamepadMode1 == 1);
        boolean dleft3  = (gamepad2.dpad_left  && gamepadMode2 == 1) || (gamepad1.dpad_left  && gamepadMode1 == 1);
        boolean dright3 = (gamepad2.dpad_right && gamepadMode2 == 1) || (gamepad1.dpad_right && gamepadMode1 == 1);

        //Auxillary Inputs
        boolean right_stick_press3  = (gamepad2.right_stick_button && gamepadMode2 == 1) || (gamepad1.right_stick_button && gamepadMode1 == 1);
        boolean left_stick_press3 = (gamepad2.left_stick_button  && gamepadMode2 == 1)  || (gamepad1.left_stick_button  && gamepadMode1 == 1);
        boolean home3 = (gamepad2.guide && gamepadMode2 == 1);
        boolean start3 = (gamepad1.start && gamepadMode1 == 1) || (gamepad2.start && gamepadMode2 == 1);

        /*
            Switch the controller mode
            mapped to Start-x
         */
        if (x_button1 && start1 && isStartXPressable) {
            gamepadMode1 = 1;
            gamepadMode2 = 0;
            isStartXPressable = false;
        }else if (x_button2 && start2 && isStartXPressable) {
            gamepadMode1 = 0;
            gamepadMode2 = 1;
            isStartXPressable = false;
        }else if (x_button3 && start3 && isStartXPressable) {
            gamepadMode1 = 0;
            gamepadMode2 = 0;
            isStartXPressable = false;
        }
        isStartXPressable = !((x_button1 && start1) || (x_button2 && start2) || (x_button3 && start3));


        //slight adjustments for driver
        if(ddown1 == true) {
            left_stick_y1 = 0.4;
        }
        if(dup1 == true) {
            left_stick_y1 = -0.4;
        }
        if(dleft1 == true) {
            left_stick_x1 = -0.4;
        }
        if(dright1 == true) {
            left_stick_x1 = 0.4;
        }

        //changes front of robot for driver using a,b,x,y
        if(y_button1 == true || frontFacing == true) {
            front = left_stick_y1 * -1;
            side = left_stick_x1 * -1;
            rotate = right_stick_x1 * -1;

            frontFacing = true;
            leftFacing = false;
            rightFacing = false;
            backFacing = false;
        }
        if(x_button1 == true || leftFacing == true) {
            front = left_stick_x1;
            side = left_stick_y1 * -1;
            rotate = right_stick_y1 * -1;

            frontFacing = false;
            leftFacing = true;
            rightFacing = false;
            backFacing = false;
        }
        if(b_button1 == true || rightFacing == true) {
            front = left_stick_x1 * -1;
            side = left_stick_y1;
            rotate = right_stick_y1;

            frontFacing = false;
            leftFacing = false;
            rightFacing = true;
            backFacing = false;
        }
        if(a_button1 == true || backFacing == true) {
            front = left_stick_y1;
            side = left_stick_x1;
            rotate = right_stick_x1;

            frontFacing = false;
            leftFacing = false;
            rightFacing = false;
            backFacing = true;
        }

        if(left_bumper1 == true) {
            front /= 2;
            side /= 2;
            rotate /= 2;
        }
        if(stick_press1  == true) {
            rotate /= 2;
            front /= 2;
            side /= 2;
        }

        if(left_trigger1 == true) {
            robot.glyphRake.setPosition(.98);
        }
        else {
            robot.glyphRake.setPosition(0);
        }

        robot.onmiDrive(side, front, rotate);

        /*
            The home button moves the grabber down to the bottom position, while this process is happening
         */
        if(home2 == true || grabberDown == false) {
            if (run == false) {
                robot.grabber.setPower(0.6);
                robot.grabber.setTargetPosition(-1 * robot.GRABBER_AUTOPOS);
                run = true;
                grabberDown = false;
                run2 = false;
                runtime.reset();

            } else if (robot.grabber.getCurrentPosition() > ((-1 * robot.GRABBER_AUTOPOS) + 10) && grabberDown == false && runtime.seconds() < 2) {
                telemetry.addLine("Waiting to get to bottom");
                telemetry.update();
            } else if (robot.grabber.getCurrentPosition() <= ((-1 * robot.GRABBER_AUTOPOS) + 10) && grabberDown == false || runtime.seconds() > 2) {
                robot.grabber.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                robot.grabber.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                grabberDown = true;
                run = false;
            }
        }
        else if (left_bumper2 == true) {
            robot.glyphStop.setPosition(0.1);
            //robot.grabber.setPower(0.6);
            if(robot.grabber.getCurrentPosition() >= 400) {
                robot.grabber.setPower(0.3);
            }
            else {
                robot.grabber.setPower(0.6);
            }
            robot.grabber.setTargetPosition(550);

        }
        else if(left_trigger2) {
            robot.glyphStop.setPosition(0.1);
            robot.grabber.setPower(0.35);
            //robot.glyphStop.setPosition(0.6);
            robot.grabber.setTargetPosition(350);
        }
        else if(left_bumper1 == true) {
            robot.grabber.setPower(0.6);
            //robot.glyphStop.setPosition(0.6);
            robot.grabber.setTargetPosition(450);
            robot.claw1.setPosition(0.7);
            robot.claw2.setPosition(0.25);
            run2 = true;
            runtime.reset();
        }
        else if(run2 == true && runtime.seconds() > 2.0) {
            robot.grabber.setPower(0.2);
        }
        else if(run2 == false){
            if(robot.grabber.getCurrentPosition() <= 20) {
                robot.grabber.setPower(0);
            }
            else {
                robot.grabber.setPower(0.4);
            }
            robot.grabber.setTargetPosition(5);
        }
        if(dup2 == true) {
            //robot.glyphStop.setPosition(0.6);
            robot.grabber.setPower(0.2);
            robot.grabber.setTargetPosition(1500);
            grabberReset = true;
        }
        else if(ddown2 == true) {
            //robot.glyphStop.setPosition(0.6);
            robot.grabber.setPower(0.2);
            robot.grabber.setTargetPosition(-1500);
            grabberReset = true;
        }
        else if(grabberReset == true) {
            //Reset the position and encoders of the grabber
            robot.grabber.setPower(0);
            robot.grabber.setTargetPosition(robot.grabber.getCurrentPosition());
            robot.grabber.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.grabber.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            grabberReset = false;
            robot.grabber.setPower(1);
        }

        /*if(left_bumper2 == true || left_trigger2 > 0.3 || dup2 == true || ddown2 == true || robot.grabber.getCurrentPosition() > 10 || grabberDown == false) {
            //robot.glyphStop.setPosition(0.6);
        }
        else{
            //robot.glyphStop.setPosition(0.4);
        }*/
        if(right_trigger1 && left_bumper1 == false && !left_trigger2  && robot.grabber.getCurrentPosition() < 150 && dup2 == false && ddown2 == false && run2 == false) {
            telemetry.addLine("BOP!");
            robot.glyphStop.setPosition(0.55);
        }
        else if(back2 == true) {
            robot.glyphStop.setPosition(0.8);
        }
        else {
            robot.glyphStop.setPosition(0.1);
        }
        //wheelie controlls
        if(left_bumper1 == true) {
            robot.wheelie.setPower(-1.0);
            goup = true;
        }
        else if(right_bumper1){
            robot.wheelie.setPower(1.0);
        }
        else {
            robot.wheelie.setPower(0.0);
        }

        if (back1) {
            run2 = true;
        }

        //dumper controls
        if (right_bumper2 == true) {
            robot.dumper.setPower(0.5);
            robot.dumper.setTargetPosition(480);
        }
        else if(right_trigger2) {
            if(robot.dumper.getCurrentPosition() >= -200) {
                robot.dumper.setPower(0.6);
            }
            else {
                robot.dumper.setPower(0);
            }
            robot.dumper.setTargetPosition(-300);
            dumperReset = true;
        }
        else if(dumperReset == true) {
            robot.dumper.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.dumper.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            dumperReset = false;
        }
        else {
            if(robot.dumper.getCurrentPosition() <= 5) {
                robot.dumper.setPower(0.1);
            }
            else {
                robot.dumper.setPower(0.5);
            }
            robot.dumper.setTargetPosition(0);
        }
        /*
         *Claw Controls
        */
        //closes claws
        if (x_button2 == true || run2 == true) {
            robot.claw1.setPosition(0.55);
            robot.claw2.setPosition(0.48);
        }
        //all the way open
        else if(y_button2 == true) {
            robot.claw1.setPosition(0.72);
            robot.claw2.setPosition(0.28);
        }
        //DABBING FOR AT END
        else if(home_button1 == true) {
            robot.claw1.setPosition(1);
            robot.claw2.setPosition(0.58);
        }
        //part way open when not pressing a button
        else {
            robot.claw1.setPosition(0.65);
            robot.claw2.setPosition(0.35);
        }

        /*
         * Move the Relic Slide
         */
        //uses the right stick on either the third or second controller to move the slide
        rds.moveSlide(right_stick_y3 + right_stick_y_2);
        //Uses the left stick on either the third or second controller to the wrist
        //Uses the x button on the third controller to move the wrist to the halfway position
        rds.moveWrist(left_stick_y3 + left_stick_y2, a_button3);
        //Fully opens the claw fully with the a button on the second controller or the right
        //bumper on the third controller
        //Partially opens the claw with the b button on the second controller or the left bumper
        //of the third controller
        rds.openClaw(right_bumper3 || a_button2, left_bumper3 || b_button2);


        // Send telemetry message to signify robot running;
        telemetry.addLine();
        telemetry.addData("grabber position", robot.grabber.getCurrentPosition());
        telemetry.addLine();
        telemetry.addLine("Controller Telemetry:");
        //Controller 1
        if (gamepadMode1 == 0) {
            telemetry.addLine("--------------------------------------------------");
            telemetry.addLine("CONTROLLER 1");
            telemetry.addData("Left Joystick (x, y)", left_stick_x1 + ", " + left_stick_y1);
            telemetry.addData("Right Joystick (x, y)", right_stick_x1 + ", " + right_stick_y1);
            telemetry.addData("Left Trigger", left_trigger1);
            telemetry.addData("Right Trigger", right_trigger1);
            telemetry.addData("Left Bumper", left_bumper1);
            telemetry.addData("Right Bumper", right_bumper1);
            telemetry.addData("A Button", a_button1);
            telemetry.addData("B Button", b_button1);
            telemetry.addData("X Button", x_button1);
            telemetry.addData("Y Button", y_button1);
            telemetry.addData("Dpad Up", dup1);
            telemetry.addData("Dpad Down", ddown1);
            telemetry.addData("Dpad Left" , dleft1);
            telemetry.addData("Dpad Right", dright1);
            telemetry.addData("Back", back1);
        }

        if (gamepadMode2 == 0) {
            telemetry.addLine("--------------------------------------------------");
            telemetry.addLine("CONTROLLER 2");

            telemetry.addData("Left Joystick y",  left_stick_y2);
            telemetry.addData("Right Joystick y", right_stick_y_2);
            telemetry.addData("Left Trigger", left_trigger2);
            telemetry.addData("Right Trigger", right_trigger2);
            telemetry.addData("Left Bumper", left_bumper2);
            telemetry.addData("Right Bumper", right_bumper2);
            telemetry.addData("A Button", a_button2);
            telemetry.addData("B Button", b_button2);
            telemetry.addData("X Button", x_button2);
            telemetry.addData("Y Button", y_button2);
            telemetry.addData("Dpad Up", dup2);
            telemetry.addData("Dpad Down", ddown2);
            telemetry.addData("Home", home2);
        }

        if (gamepadMode1 == 1 || gamepadMode2 == 1) {
            telemetry.addLine("--------------------------------------------------");
            telemetry.addLine("CONTROLLER 3");
            if (gamepadMode1 == 1) {
                telemetry.addLine("Being Controlled by Controller 1");
            } else {
                telemetry.addLine("Being Controlled by Controller 2");
            }
            telemetry.addData("Left Joystick (x, y)", left_stick_x3 + ", " + left_stick_y3);
            telemetry.addData("Right Joystick (x, y)", right_stick_x3 + ", " + right_stick_y3);
            telemetry.addData("Left Trigger", left_trigger3);
            telemetry.addData("Right Trigger", right_trigger3);
            telemetry.addData("Left Bumper", left_bumper3);
            telemetry.addData("Right Bumper", right_bumper3);
            telemetry.addData("A Button", a_button3);
            telemetry.addData("B Button", b_button3);
            telemetry.addData("X Button", x_button3);
            telemetry.addData("Y Button", y_button3);
            telemetry.addData("Dpad Up", dup3);
            telemetry.addData("Dpad Down", ddown3);
            telemetry.addData("Dpad Left" , dleft3);
            telemetry.addData("Dpad Right", dright3);
            telemetry.addData("Start", start3);
            telemetry.addData("Home", home3);
        }

        telemetry.addLine("--------------------------------------------------");
        telemetry.addLine("Servos");
        telemetry.addData("glyphStop Position", robot.glyphStop.getPosition());
        telemetry.addData("claw1 Position", robot.claw1.getPosition());
        telemetry.addData("claw2 Position", robot.claw2.getPosition());
        telemetry.addData("relic Stop Position", robot.relicStopper.getPosition());
        telemetry.addData("relic Wrist Position", robot.relicWrist.getPosition());
        telemetry.addData("relic Claw Position", robot.relicClaw.getPosition());
        telemetry.addData("jewel knock Position", robot.jknock.getPosition());

        telemetry.addLine("--------------------------------------------------");
        telemetry.addLine("Motors");
        telemetry.addData("dumper Position", robot.dumper.getCurrentPosition());
        telemetry.addData("grabber Position", robot.grabber.getCurrentPosition());
        telemetry.addData("relic Motor Power", robot.relicMotor.getPower());
        telemetry.addData("Platform wheel Power", robot.wheelie.getPower());

        telemetry.addLine("--------------------------------------------------");
        telemetry.addLine("Sensors");
        telemetry.addData("color 1", robot.jkcolor.blue());
        telemetry.addData("color 1", robot.jkcolor2.blue());
        telemetry.addData("dumper ODS", robot.glyphDetect.getRawLightDetected());
        telemetry.addData("Ultra Back ", ((robot.ultra_back.getVoltage() / 5) * 512) + 2.5);
        telemetry.addData("Ultra Left ", ((robot.ultra_left.getVoltage() / 5) * 512) + 2.5);
        telemetry.addData("Ultra Right ", ((robot.ultra_right.getVoltage() / 5) * 512) + 2.5);
        telemetry.addLine("What is my name?: Spitz");
        telemetry.addData("rotate: ",rotate);

    }


    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }

}