// package defined
package org.firstinspires.ftc.teamcode;

//import packages

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.drive.StandardTrackingWheelLocalizer;

// auto is defined
@Autonomous(name = "TestAutoRedHouse3 Rev 1", group = "Final")

// extends LinearOpMode
public class TestAutoRedHouse3Rev1 extends LinearOpMode {

    // declare other motors
    DcMotor m1, m2, m3, m4, m5, m6;
    DcMotorEx m7, m8;

    @Override
    public void runOpMode() {

        // configuration of motors
        m1 = hardwareMap.dcMotor.get("fr_front_encoder");
        m2 = hardwareMap.dcMotor.get("br");
        m3 = hardwareMap.dcMotor.get("bl_left_encoder");
        m4 = hardwareMap.dcMotor.get("fl_right_encoder");
        m2.setDirection(DcMotor.Direction.REVERSE);
        m5 = hardwareMap.dcMotor.get("rotater");
        m6 = hardwareMap.dcMotor.get("arm");
        m7 = (DcMotorEx) hardwareMap.dcMotor.get("wheel");
        m8 = (DcMotorEx) hardwareMap.dcMotor.get("intake");
        m1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        m2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        m3.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        m4.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        m5.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        m6.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        m7.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        m8.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        m1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m4.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m5.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m6.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m7.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m8.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        m2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        m3.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        m4.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        m5.setTargetPosition(0);
        m5.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m6.setTargetPosition(0);
        m6.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m7.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m8.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        StandardTrackingWheelLocalizer myLocalizer = new StandardTrackingWheelLocalizer(hardwareMap);
        myLocalizer.setPoseEstimate(new Pose2d(0,0, Math.toRadians(90)));

        // Wait for the game to begin
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();

        waitForStart();

        //out and arm

        while (opModeIsActive()) {
            setMotorPowers(-0.6,-0.6,-0.6,-0.6);
            myLocalizer.update();
            Pose2d mypose = myLocalizer.getPoseEstimate();

            m6.setTargetPosition(1200);
            m6.setPower(0.5);

            if (mypose.getY() <= -15) {
                stopMotors();
                break;
            }
        }
        stopMotors();

        m5.setTargetPosition(1380);
        m5.setPower(0.5);
        while (m5.isBusy()) {
        }

        m8.setPower(-0.12);
        while (m8.isBusy()) {
        }
        sleep(2000);
        m8.setPower(0);

        m5.setTargetPosition(0);
        m5.setPower(-0.5);
        while (m5.isBusy()) {
        }

        // rotate and in

        while (opModeIsActive()) {
            setMotorPowers(-0.3,-0.3,0.3,0.3);
            myLocalizer.update();
            Pose2d mypose = myLocalizer.getPoseEstimate();

            m6.setTargetPosition(5);
            m6.setPower(-0.5);

            if (mypose.getHeading() >= 3.05) {
                break;
            }
        }
        stopMotors();

        setMotorPowers(-0.2,-0.2,-0.2,-0.2);
        sleep(250);
        stopMotors();

        while (opModeIsActive()) {
            setMotorPowers(0.7,-0.7,0.7,-0.7);
            myLocalizer.update();
            Pose2d mypose = myLocalizer.getPoseEstimate();
            if (mypose.getY() >= 1) {
                break;
            }
        }
        sleep(300);
        stopMotors();

        // up and grab

        while (opModeIsActive()) {
            setMotorPowers(0.5,0.5,0.5,0.5);
            myLocalizer.update();
            Pose2d mypose = myLocalizer.getPoseEstimate();
            if (mypose.getX() <= -20) {
                stopMotors();
                break;
            }
        }
        stopMotors();

        sleep(200);

        setMotorPowers(0.1,0.3,0.3,0.3);
        m8.setPower(1);
        sleep(2000);

        stopMotors();
        m8.setPower(1);
        sleep(250);

        // back

        m8.setPower(0);
        setMotorPowers(-0.3,-0.1,-0.3,-0.3);
        sleep(1500);

        setMotorPowers(0.5,-0.5,0.5,-0.5);
        sleep(1000);
        stopMotors();

        while (opModeIsActive()) {
            setMotorPowers(-0.3,-0.3,-0.3,-0.3);
            myLocalizer.update();
            Pose2d mypose = myLocalizer.getPoseEstimate();
            if (mypose.getX() >= 5) {
                break;
            }
        }
        stopMotors();

        // out and arm

        while (opModeIsActive()) {
            setMotorPowers(-0.5,0.5,-0.5,0.5);
            myLocalizer.update();
            Pose2d mypose = myLocalizer.getPoseEstimate();
            if (mypose.getY() <= -16) {
                break;
            }
        }
        stopMotors();

        m6.setTargetPosition(1200);
        m6.setPower(0.5);
        while (m6.isBusy()) {
        }

        m5.setTargetPosition(-1480);
        m5.setPower(-0.5);
        while (m5.isBusy()) {
        }

        m8.setPower(-0.12);
        while (m8.isBusy()) {
        }
        sleep(2000);
        m8.setPower(0);

        m5.setTargetPosition(-5);
        m5.setPower(0.5);
        while (m5.isBusy()) {
        }

        m6.setTargetPosition(5);
        m6.setPower(-0.5);
        while (m6.isBusy()) {
        }

        // back

        setMotorPowers(0.7,-0.7,0.7,-0.7);
        sleep(1500);
        stopMotors();

        // up

        while (opModeIsActive()) {
            setMotorPowers(0.5,0.5,0.5,0.5);
            myLocalizer.update();
            Pose2d mypose = myLocalizer.getPoseEstimate();
            if (mypose.getX() <= -19) {
                stopMotors();
                break;
            }
        }
        stopMotors();

        setMotorPowers(-1,1,-1,1);
        sleep(700);
        stopMotors();

        setMotorPowers(1,1,-1,-1);
        sleep(260);
        stopMotors();


    }

    // Methods

    // Drive Method
    void setMotorPowers(double v, double v1, double v2, double v3) {
        m4.setPower(v1);
        m3.setPower(v);
        m2.setPower(v2);
        m1.setPower(v3);
    }

    // Stop Method
    void stopMotors() {
        m4.setPower(0);
        m3.setPower(0);
        m2.setPower(0);
        m1.setPower(0);
    }

}
