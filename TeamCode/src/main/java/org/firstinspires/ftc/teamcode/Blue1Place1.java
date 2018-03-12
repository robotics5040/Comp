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

@Autonomous(name="Omnibot: Blue1Place1", group="Omnibot")
//@Disabled
public class Blue1Place1 extends AutoPull {

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
        int choosen = Vuforia(cameraMonitorViewId, "blue",vuforia);
        double target = 0;

        switch (choosen) {
            case (1):
                target = 44;
                break;
            case (2):
                target = 52;
                break;
            case (3):
                target = 58.5;
                break;
            default:
                target = 52;
                break;
        }

        telemetry.addData("VuMark", "%s visible", choosen);
        telemetry.update();

        robot.claw1.setPosition(0.5);
        robot.claw2.setPosition(0.5);

        JewelKnock(robot,"blue");
        DriveFor(robot,0.3,0.0,0.0,0.0);
        if(robot.jknock.getPosition() != robot.JKUP) {robot.jknock.setPosition(robot.JKUP);}
        robot.wheelie.setPower(1);
        DriveFor(robot,1.3,1.0,0.0,0.0);
        robot.wheelie.setPower(0);
        DriveFor(robot,0.3,0.0,0.0,0.0);

        DriveFor(robot,1,0,0,1);
        //RotateTo0(robot,0, startG, startG2);
        rotateTo(robot, -90,0);
        DriveFor(robot,0.3,0.0,0.0,0.0);

        //DriveFor(robot, 0.4,0,-1,0);
        DriveFor(robot,1.0,-1,0,0);
        DriveFor(robot,0.55,0.36,0,0);
        DriveFor(robot,0.3,0,0,0);

        telemetry.addLine("Lineup 1 Complete");
        telemetry.update();

        robot.grabber.setTargetPosition(350);

        boolean dis2 = false;
        int count = 0;
        runtime.reset();
        double speed = 0.285;
        while (dis2 == false && runtime2.seconds() < 23 && opModeIsActive()) {
            double distanceRight = ((robot.ultra_right.getVoltage() / 5) * 512) + 2.5;// robot.ultra_right.getDistance(DistanceUnit.CM);
            telemetry.addData("Right", distanceRight);
            telemetry.update();

            if (distanceRight > target+0.3) {
                onmiDrive(robot, -speed, 0.0, 0.0);
            }
            else if (distanceRight < target-0.3) {
                onmiDrive(robot,speed,0.0,0.0);
            }
            else {
                if(count == 1) {
                    speed = 0.25;
                }
                onmiDrive(robot,0.0, 0.0, 0.0);
                DriveFor(robot,0.3,0,0,0);
                if(count == 2) {
                    rotateTo(robot, -90,0);
                    DriveFor(robot, 0.3, 0, 0, 0);
                }
                if(count == 3) {
                    dis2 = true;
                }
                count ++;
            }
        }
        onmiDrive(robot,0.0, 0.0, 0.0);
        DriveFor(robot,0.6,-1,0,0);
        DriveFor(robot,0.3,0,0,0);

        telemetry.addLine("Lineup 2 Complete");
        telemetry.update();

        robot.dumper.setPower(0.4);
        runtime.reset();
        while (robot.dumper.getCurrentPosition() <= 470 && opModeIsActive() && runtime2.seconds() < 28 && runtime.seconds() < 2.0) {
            robot.dumper.setTargetPosition(480);
            //onmiDrive(robot, 0,.26,0);
        }
        DriveFor(robot,0.5, 0.4, 0.0, 0.0);

        while (robot.dumper.getCurrentPosition() >= 5 && opModeIsActive()) {
            robot.dumper.setTargetPosition(0);
        }

        if(runtime2.seconds() < 28) {
            DriveFor(robot, 1.0, -0.8, 0.0, 0.0);
            DriveFor(robot, 0.5, 0.5, 0.0, 0.0);
        }
        DriveFor(robot,1.0, 0.0, 0.0, 0.0);
    }
}