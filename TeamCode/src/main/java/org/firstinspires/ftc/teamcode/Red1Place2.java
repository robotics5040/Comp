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

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

@Autonomous(name="Omnibot: Red1Place2", group="Omnibot")
//@Disabled
public class Red1Place2 extends AutoPull {

    HardwareOmniRobot robot   = new HardwareOmniRobot();
    ElapsedTime runtime = new ElapsedTime();
    ElapsedTime runtime2 = new ElapsedTime();

    @Override public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, true);
        robot.dumperColor.enableLed(true);

        RobotLog.ii("5040MSG","Robot Inited");

        robot.grabber.setPower(0.6);
        RobotLog.ii("5040MSG","Grabber set power");
        //robot.grabber.setTargetPosition(robot.GRABBER_AUTOPOS);

        RobotLog.ii("5040MSG","Grabber set up");


        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = "AUBrQCz/////AAAAGXg5njs2FEpBgEGX/o6QppZq8c+tG+wbAB+cjpPcC5bwtGmv+kD1lqGbNrlHctdvrdmTJ9Fm1OseZYM15VBaiF++ICnjCSY/IHPhjGW9TXDMAOv/Pdz/T5H86PduPVVKvdGiQ/gpE8v6HePezWRRWG6CTA21itPZfj0xDuHdqrAGGiIQXcUbCTfRAkY7HwwRfQOM1aDhmeAaOvkPPCnaA228iposAByBHmA2rkx4/SmTtN82rtOoRn3/I1PA9RxMiWHWlU67yMQW4ExpTe2eRtq7fPGCCjFeXqOl57au/rZySASURemt7pwbprumwoyqYLgK9eJ6hC2UqkJO5GFzTi3XiDNOYcaFOkP71P5NE/BB    ";

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(parameters);


        /*while (robot.gyro.isCalibrating() && robot.gyro2.isCalibrating()){
            telemetry.addLine("Calibrating gyro");
            telemetry.update();
        }*/
        RobotLog.ii("5040MSG","Gyro Calibrated");
        while (!(isStarted() || isStopRequested())) {

            // Display the light level while we are waiting to start
            //telemetry.addData("HEADING",robot.gyro.getHeading());
            //telemetry.addData("heading2", robot.gyro2.getHeading());
            telemetry.addData("calibration", robot.imu.isGyroCalibrated());
            telemetry.update();
            idle();
        }
        //int startG = robot.gyro.getHeading();
        //int startG2 = robot.gyro2.getHeading();
        RobotLog.ii("5040MSG","Robot started");
        //waitForStart();
        runtime2.reset();

        robot.jkcolor.enableLed(true);
        robot.jkcolor2.enableLed(true);
        robot.jknock.setPosition(0.13);

        RobotLog.ii("5040MSG","Run vufloria");
        //int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        int choosen = Vuforia(cameraMonitorViewId, "red",vuforia);
        double target = 0;

        switch (choosen) {
            case (1):
                target = 45;
                break;
            case (2):
                target = 52.5;
                break;
            case (3):
                target = 60;
                break;
            default:
                target = 52.5;
                break;
        }

        /*int glyphColor1;
        //brown    1 is brown 2 is gray 0 is none
        if(robot.dumperColor.alpha() < 20) {
            glyphColor1 = 1;
        }
        else {
            glyphColor1 = 2;
        }*/

        telemetry.addData("VuMark", "%s visible", choosen);
        telemetry.update();

        robot.claw1.setPosition(0.64);
        robot.claw2.setPosition(0.36);

        JewelKnock(robot,"red");



        DriveFor(robot,0.3,0.0,0.0,0.0);
        if(robot.jknock.getPosition() != robot.JKUP) {robot.jknock.setPosition(robot.JKUP);}
        robot.wheelie.setPower(-1);
        DriveFor(robot,1.1,-1.0,0.0,0.0);
        robot.wheelie.setPower(0);

        DriveFor(robot,1,0,0,1);
        //RotateTo0(robot,0, startG, startG2);
        rotateTo(robot, -90,0);
        robot.grabber.setTargetPosition(0);
        DriveFor(robot,0.2,0,0,0);
        robot.glyphStop.setPosition(0.8);
        DriveFor(robot,1.5,1,0,0);
        robot.claw1.setPosition(0.5);
        robot.claw2.setPosition(0.5);
        DriveFor(robot,0.3,0,0,0);
        DriveFor(robot, 1,-1,0,0);


        //Shaking off glyph

        //DriveFor(robot,0.3,0,0,1);
        //DriveFor(robot,0.3,0,0,-1);
        robot.glyphStop.setPosition(0.1);
        robot.grabber.setPower(0.4);
        robot.grabber.setTargetPosition(400);
        rotateTo(robot,-90,0);
        DriveFor(robot, 1.0,-1,0,0);
        DriveFor(robot,0.2,1,0,0);

        telemetry.addLine("Lineup 1 Complete");
        telemetry.update();

        boolean dis2 = false, there = false;
        int count = 0;
        runtime.reset();
        double speed = 0.35;
        if(choosen == 2){
            speed = 0.25;
        }
        while (dis2 == false && runtime2.seconds() < 17 && opModeIsActive()) {
            double distanceLeft = ((robot.ultra_left.getVoltage() / 5) * 512) + 2.5;// robot.ultra_right.getDistance(DistanceUnit.CM);
            telemetry.addData("Left", distanceLeft);
            telemetry.update();

            if (distanceLeft > target+0.4) {
                onmiDrive(robot, speed, 0.0, 0.0);
                there = true;
            }
            else if (distanceLeft < target-0.4) {
                onmiDrive(robot,-speed,0.0,0.0);
                if(there == true) {
                    speed = 0.25;
                }
            }
            else {
                if(count == 1) {
                    speed = 0.3;
                }
                onmiDrive(robot,0.0, 0.0, 0.0);
                DriveFor(robot,0.3,0,0,0);
                if(count == 1) {
                    rotateTo(robot, -90, 0);
                    DriveFor(robot, 0.3, 0, 0, 0);
                }
                else if(count == 2){
                    dis2 = true;
                }
                count ++;
            }
        }
        onmiDrive(robot,0.0, 0.0, 0.0);
        //rotateTo(robot,-90,0);
        DriveFor(robot,0.4,-1,0,0);
        DriveFor(robot,0.3,0,0,0);

        telemetry.addLine("Lineup 2 Complete");
        telemetry.update();

        robot.dumper.setPower(0.6);
        runtime.reset();
        while (robot.dumper.getCurrentPosition() <= 470 && opModeIsActive() && runtime2.seconds() < 28 && runtime.seconds() < 2) {
            robot.dumper.setTargetPosition(480);
            //onmiDrive(robot, 0,.28,0);
        }
        DriveFor(robot,0.5, 0.4, 0.0, 0.0);


        onmiDrive(robot,0,0,0);
        while (robot.dumper.getCurrentPosition() >= 5 && opModeIsActive()) {
            robot.dumper.setTargetPosition(0);
        }

        robot.grabber.setPower(0.6);
        robot.grabber.setTargetPosition(520);
        DriveFor(robot,0.4,0,0,0);
        robot.claw1.setPosition(0.64);
        robot.claw2.setPosition(0.36);
        DriveFor(robot,0.3,0,0,0);
        robot.grabber.setTargetPosition(robot.GRABBER_AUTOPOS);
        DriveFor(robot,0.3,0,0,0);

        boolean dump = false;
        DriveFor(robot,0.3,0,0,0);
        //telemetry.addData("DumperColor", robot.dumperColor.alpha());
        if(choosen != 1 && choosen != 3) {

            DriveFor(robot,0.3, -1,0,0);
            DriveFor(robot,0.2, 1,0,0);
            DriveFor(robot,0.32, 0,1,0);
            dump = true;

            /*int glyphColor2;
            //brown    1 is brown 2 is gray 0 is none
            if(robot.dumperColor.alpha() < 20) {
                glyphColor2 = 1;
                telemetry.addLine("brown");
            }
            else if(robot.dumperColor.alpha() < 10){
                dump = false;
                glyphColor2 = 0;
            }
            else {
                telemetry.addLine("gray");
                glyphColor2 = 2;
            }
            if(glyphColor1 != glyphColor2) {
                dump = true;
            }
            else if(glyphColor2 != 0) {
                DriveFor(robot,0.4,-1,0,0);
                //DriveFor(robot,0.2,1,0,0);
                DriveFor(robot,0.4,0,1,0);
                dump = true;
            }
            telemetry.update();*/
        }
        else {
            dump = true;
        }

        DriveFor(robot,0.4,-1,0,0);

        if(dump == true) {
            runtime.reset();
            while (robot.dumper.getCurrentPosition() <= 470 && opModeIsActive() && runtime2.seconds() < 28 && runtime.seconds() < 2) {
                robot.dumper.setTargetPosition(480);
                //onmiDrive(robot, 0,.3,0);
            }
            //onmiDrive(robot,0,0,0);
            DriveFor(robot,0.4, 0.4, 0.0, 0.0);

            while (robot.dumper.getCurrentPosition() >= 5 && opModeIsActive()) {
                robot.dumper.setTargetPosition(0);
            }
        }


        if(runtime2.seconds() < 28) {
            DriveFor(robot, 1.0, -1, 0.0, 0.0);
            //DriveFor(robot, 0.5, 0.5, 0.0, 0.0);
        }
        DriveFor(robot,0.3, 1, 0.0, 0.0);
    }
}
