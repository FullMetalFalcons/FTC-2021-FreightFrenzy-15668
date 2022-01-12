// package defined
package org.firstinspires.ftc.teamcode;

//import packages

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.StandardTrackingWheelLocalizer;

// auto is defined
@Autonomous(name = "Test Auto", group = "Final")

// extends LinearOpMode
public class TestAuto extends LinearOpMode {

    SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

    // declare other motors
    DcMotor m5, m6;
    DcMotorEx m7, m8;

    @Override
    public void runOpMode() {

        // configuration of motors

        m5 = hardwareMap.dcMotor.get("rotater");
        m6 = hardwareMap.dcMotor.get("arm");
        m7 = (DcMotorEx) hardwareMap.dcMotor.get("wheel");
        m8 = (DcMotorEx) hardwareMap.dcMotor.get("intake");
        m5.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        m6.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        m7.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        m8.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        m5.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m6.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m7.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m8.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m5.setTargetPosition(0);
        m5.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m6.setTargetPosition(0);
        m6.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m7.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m8.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        StandardTrackingWheelLocalizer myLocalizer = new StandardTrackingWheelLocalizer(hardwareMap);
        myLocalizer.setPoseEstimate(new Pose2d(0, 0, Math.toRadians(90)));

        // Wait for the game to begin
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();

        waitForStart();

        m6.setTargetPosition(1200);
        m6.setPower(0.5);
        while (m6.isBusy()) {
            sleep(100);
        }

        m5.setTargetPosition(1380);
        m5.setPower(0.5);
        while (m5.isBusy()) {
            sleep(100);
        }

        m8.setPower(-0.12);
        while (m8.isBusy()) {
            sleep(100);
        }
        sleep(2000);
        m8.setPower(0);

        m5.setTargetPosition(0);
        m5.setPower(-0.5);
        while (m5.isBusy()) {
            sleep(100);
        }

        m6.setTargetPosition(5);
        m6.setPower(-0.5);
        while (m6.isBusy()) {
            sleep(100);
        }


        // Methods


    }

}