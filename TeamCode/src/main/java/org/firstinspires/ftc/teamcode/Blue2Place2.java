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
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

@Autonomous(name="Omnibot: Blue2Place2", group="Blue2Auto")
//@Disabled
public class Blue2Place2 extends AutoPull {

    HardwareOmniRobot robot   = new HardwareOmniRobot();
    ElapsedTime runtime = new ElapsedTime();
    ElapsedTime runtime2 = new ElapsedTime();

    @Override public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, true);
        RobotLog.ii("5040MSG","Robot Inited");

        //robot.grabber.setPower(0.75);
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
        int adjustment = 0;
        RobotLog.ii("5040MSG","Gyro Calibrated");
        while (!(isStarted() || isStopRequested())) {

            if(gamepad1.left_stick_y > 0.5)
                adjustment -= 10;
            else if(gamepad1.left_stick_y < -0.5)
                adjustment += 10;
            if(gamepad1.back == true) {
                robot.claw2.setPosition(0);
            }
            // Display the light level while we are waiting to start
            //telemetry.addData("HEADING",robot.gyro.getHeading());
            //telemetry.addData("heading2", robot.gyro2.getHeading());
            telemetry.addData("calibration", robot.imu.isGyroCalibrated());
            telemetry.addData("potentiometer",(robot.potentiometer.getVoltage()*((float)1023/68))-37.5);
            telemetry.update();

            robot.grabber.setTargetPosition(robot.GRABBER_AUTOPOS+adjustment);
            idle();
        }
        //int startG = robot.gyro.getHeading();
        //int startG2 = robot.gyro2.getHeading();
        RobotLog.ii("5040MSG","Robot started");
        //waitForStart();
        runtime2.reset();

        robot.jkcolor.enableLed(true);
        robot.jkcolor2.enableLed(true);
        robot.jknock.setPosition(robot.JKDOWN);

        RobotLog.ii("5040MSG","Run vufloria");
        //int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        int choosen = Vuforia(cameraMonitorViewId, "blue",vuforia);
        double target = 0;

        switch (choosen) {
            case (1):
                target = 22;
                break;
            case (2):
                target = 30;
                break;
            case (3):
                target = 36.5;
                break;
            default:
                target = 30;
                break;
        }

        telemetry.addData("VuMark", "%s visible", choosen);
        telemetry.update();

        JewelKnock(robot,"blue");
        DriveFor(robot,0.3,0.0,0.0,0.0,true);
        robot.jknock.setPosition(robot.JKUP);

        robot.wheelie.setPower(1);
        DriveFor(robot,0.8,1.0,0.0,0.0,false);
        robot.wheelie.setPower(0);
        DriveFor(robot,0.5,0.4,0.0,0.0,false);
        DriveFor(robot,0.5,-0.4,0.0,0.0,false);
        DriveFor(robot,0.3,0.0,0.0,0.0,true);

        DriveFor(robot,1.4,0,0,-1,false);
        //RotateTo0(robot,0, startG, startG2);
        rotateTo180();
        DriveFor(robot,0.3,0.0,0.0,0.0,true);

        DriveFor(robot, 0.45,0,-1,0,false);
        DriveFor(robot,0.7,-1,0,0,false);
        DriveFor(robot,0.2,1,0,0,false);
        DriveFor(robot,0.3,0,0,0,true);

        robot.claw1.setPosition(0.6);
        robot.claw2.setPosition(0.4);

        robot.grabber.setTargetPosition(350+adjustment);

        telemetry.addLine("Lineup 1 Complete");
        telemetry.update();

        boolean dis2 = false;
        int count = 0;
        runtime.reset();
        double speed = 0.36;
        while (dis2 == false && runtime2.seconds() < 15 && opModeIsActive()) {
            double distanceRight = ((robot.ultra_right.getVoltage() / 5) * 512) + 2.5;// robot.ultra_right.getDistance(DistanceUnit.CM);
            telemetry.addData("Right", distanceRight);
            telemetry.update();

            if (distanceRight > target+0.4) {
                omniDrive(robot, -speed, 0.0, 0.0,true);
            }
            else if (distanceRight < target-0.4) {
                omniDrive(robot,speed,0.0,0.0,true);
            }
            else {
                count++;
                if(count == 1) {
                    speed = 0.32;
                    omniDrive(robot,0.0, 0.0, 0.0,true);
                    DriveFor(robot,0.3,0,0,0,true);
                    //rotateTo180();
                    //DriveFor(robot, 0.3, 0, 0, 0,true);
                    dis2 = true;
                }
            }
        }
        omniDrive(robot,0.0, 0.0, 0.0,true);
        DriveFor(robot,0.3,-1,-0.2,0,false);
        DriveFor(robot,0.3,0,0,0,false);

        telemetry.addLine("Lineup 2 Complete");
        telemetry.update();

        robot.dumper.setPower(0.6);

        dumpGlyph(robot);

        DriveFor(robot, 0.3, -1, 0.0, 0.0, false);
        DriveFor(robot, 0.2, 1, 0.0, 0.0, false);

        double distanceRight = ((robot.ultra_right.getVoltage() / 5) * 512) + 2.5;
        while(opModeIsActive() == true && distanceRight < 38.5) {
            distanceRight = ((robot.ultra_right.getVoltage() / 5) * 512) + 2.5;
            omniDrive(robot, 1, 0, 0, false);
        }
        omniDrive(robot,0,0,0,true);


        DriveFor(robot,0.3,0,0,0,true);


        robot.grabber.setTargetPosition(0);

        double distanceBack = ((robot.ultra_back.getVoltage() / 5) * 512) + 2.5;
        while(opModeIsActive() == true && distanceBack < 24) {
            distanceBack = ((robot.ultra_back.getVoltage() / 5) * 512) + 2.5;
            omniDrive(robot, 0, 1, 0, false);
        }
        omniDrive(robot,0,0,0,true);

        robot.glyphStop.setPosition(0.5);

        DriveFor(robot,1,.8,0,-1,false);
        robot.claw1.setPosition(0.48);
        robot.claw2.setPosition(0.52);
        DriveFor(robot,0.3,0,0,0,true);
        DriveFor(robot,1,-1,0,1,false);

        robot.glyphStop.setPosition(0.1);
        DriveFor(robot,0.3,0,0,0,true);

        //place second glyph in dumper
        robot.grabber.setPower(1);
        DriveFor(robot,0.45,0,0,0,true);
        robot.grabber.setTargetPosition(550+adjustment);
        rotateTo180();
        robot.claw1.setPosition(0.6);
        robot.claw2.setPosition(0.4);
        DriveFor(robot,0.45,0,0,0,true);
        robot.grabber.setTargetPosition(350+adjustment);


        distanceBack = ((robot.ultra_back.getVoltage() / 5) * 512) + 2.5;
        while(opModeIsActive() == true && distanceBack > 18) {
            distanceBack = ((robot.ultra_back.getVoltage() / 5) * 512) + 2.5;
            omniDrive(robot,0,-1,0, true);
        }
        omniDrive(robot,0,0,0,true);

        distanceBack = ((robot.ultra_back.getVoltage() / 5) * 512) + 2.5;
        while(opModeIsActive() == true && distanceBack < 12) {
            distanceBack = ((robot.ultra_back.getVoltage() / 5) * 512) + 2.5;
            omniDrive(robot,0,.7,0,true);
        }
        omniDrive(robot,0,0,0,true);


        distanceRight = ((robot.ultra_right.getVoltage() / 5) * 512) + 2.5;
        while(opModeIsActive() == true && distanceRight > 38) {
            distanceRight = ((robot.ultra_right.getVoltage() / 5) * 512) + 2.5;
            omniDrive(robot, -1, 0, 0, true);
        }
        omniDrive(robot,0,0,0,true);

        if(choosen != 1 && choosen != 3) {
            DriveFor(robot, 0.4, -1,0,0,false);
        }
        else {
            DriveFor(robot, 0.3, -1,0,0,false);
        }

        robot.dumper.setTargetPosition(480);
        while (robot.dumper.getCurrentPosition() <= 470 && opModeIsActive()&& runtime.seconds() < 0.5) {
            robot.dumper.setTargetPosition(480);
            telemetry.addData("dumper", robot.dumper.getCurrentPosition());
            telemetry.update();
            //onmiDrive(robot, 0,.4,0);
        }
        if(choosen != 1 && choosen != 3) {
            DriveFor(robot, 0.5, 0.4, 0.0, 0.0, true);
        }
        else {
            DriveFor(robot,0.5,0.5,0,0,true);
        }

        runtime.reset();

        DriveFor(robot,0.5, 0, 0.0, 0.0,true);
        while (robot.dumper.getCurrentPosition() >= 10 && opModeIsActive() && runtime.seconds() < .5) {
            telemetry.addData("dumper", robot.dumper.getCurrentPosition());
            telemetry.update();
            robot.dumper.setTargetPosition(5);
        }
        robot.dumper.setTargetPosition(5);
        if(runtime2.seconds() < 28) {
            DriveFor(robot, 0.7, -1, 0.0, 0.0,false);
            //DriveFor(robot, 0.5, 0.5, 0.0, 0.0);
        }
        DriveFor(robot,0.2, 1, 0.0, 0.0,false);
        robot.dumper.setPower(0);
    }

    public void rotateTo180()  {
        float heading = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;//getGyro(robot) - gyro;
        double speed = 0.4;
        boolean go = false;

        runtime.reset();
        while (heading != 180 && opModeIsActive() && runtime.seconds() < 1.5) {
            telemetry.addData("HEADING", heading);
            telemetry.addData("speed", speed);
            telemetry.update();
            heading = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;//robot.gyro.getHeading() - gyro;
            if(heading < -100) {
                omniDrive(robot, 0, 0, -speed,true);
                go = true;
            }
            else if (180+0.5 > heading) {
                omniDrive(robot, 0.0, 0.0, speed,true);
                if (speed > 0.35 && go == true) {
                    speed -= 0.01;
                }
            }
        }
        omniDrive(robot, 0.0, 0.0, 0.0,true);
    }
}