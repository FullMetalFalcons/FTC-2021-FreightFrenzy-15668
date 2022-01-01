// package defined
package org.firstinspires.ftc.teamcode;

// import classes

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.drive.StandardTrackingWheelLocalizer;

// teleop is defined
@TeleOp(name = "Blue Final TeleOp", group = "Final")

// extends LinearOpMode
public class FinalTeleOpBlue extends LinearOpMode {

    // declare other motors
    DcMotor m1, m2, m3, m4, m5, m6;
    DcMotorEx m7, m8;

    // runOpMode
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

        // sets up variables for all toggles
        double totalpowervalue = 1;
        boolean halfPower = false;
        boolean stickPressed = false;

        waitForStart();

        // telemetry
        telemetry.addData("x", myLocalizer.getPoseEstimate().getX());
        telemetry.addData("y", myLocalizer.getPoseEstimate().getY());
        telemetry.addData("rotater", m5.getCurrentPosition());
        telemetry.addData("arm", m6.getCurrentPosition());
        telemetry.addData("wheel v:", m7.getVelocity());
        telemetry.addData("wheel p:", m7.getPower());
        telemetry.addData("intake", m8.getVelocity());
        telemetry.update();

        while (opModeIsActive()) {

            // telmetry
            myLocalizer.update();
            Pose2d mypose = myLocalizer.getPoseEstimate();
            telemetry.addData("x", mypose.getX());
            telemetry.addData("y", mypose.getY());
            telemetry.addData("rotater", m5.getCurrentPosition());
            telemetry.addData("arm", m6.getCurrentPosition());
            telemetry.addData("wheel v:", m7.getVelocity());
            telemetry.addData("wheel s:", m7.getPower());
            telemetry.addData("intake", m8.getVelocity());
            telemetry.update();

            // drive code
            if (!stickPressed && ((gamepad1.left_stick_button) || (gamepad1.right_stick_button))) {
                halfPower = !halfPower;
                stickPressed = true;
            } else if (stickPressed && !((gamepad1.left_stick_button) || (gamepad1.right_stick_button))) {
                stickPressed = false;
            }
            if (halfPower == true) {
                totalpowervalue = 0.3;
            } else if (halfPower == false) {
                totalpowervalue = 1;
            }
            drive(totalpowervalue);

            // code for intake
            if (gamepad2.right_trigger > 0.1) {
                m8.setPower(gamepad2.right_trigger);
            } else if (gamepad2.left_trigger > 0.1) {
                m8.setPower(-gamepad2.left_trigger);
            } else {
                m8.setPower(0);
            }

            // code for wheel
            if (gamepad2.b) {
                m7.setPower(Range.clip(m7.getPower() + .01, 0, 1));
            } else {
                m7.setPower(0);
            }

            // code for rotater
            if ( (m5.getMode() == DcMotor.RunMode.RUN_USING_ENCODER) && (m6.getMode() == DcMotor.RunMode.RUN_USING_ENCODER) ) {
                if ((Math.abs(m5.getCurrentPosition()) <= 20) && (m6.getCurrentPosition() < 300)) {
                    m5.setPower(0);
                } else if ((Math.abs(gamepad2.right_stick_x) > 0.1) && (Math.abs(m5.getCurrentPosition()) < 2600)) {
                    m5.setPower(gamepad2.right_stick_x * .5);
                } else if (m5.getCurrentPosition() > 2600) {
                    m5.setPower(-0.1);
                } else if (m5.getCurrentPosition() < -2600) {
                    m5.setPower(0.1);
                } else {
                    m5.setPower(0);
                }
            }

            // code for arm
            if ( (m5.getMode() == DcMotor.RunMode.RUN_USING_ENCODER) && (m6.getMode() == DcMotor.RunMode.RUN_USING_ENCODER) ) {
                if ((m6.getCurrentPosition() <= 7)) {
                    m6.setPower(0.2);
                } else if ((Math.abs(gamepad2.left_stick_y) > 0.1) && (Math.abs(m5.getCurrentPosition()) < 100)) {
                    m6.setPower(-gamepad2.left_stick_y * .5);
                } else if ((Math.abs(gamepad2.left_stick_y) > 0.1) && (Math.abs(m5.getCurrentPosition()) > 850)) {
                    m6.setPower(-gamepad2.left_stick_y * .5);
                } else if ((Math.abs(m5.getCurrentPosition()) > 100) && (Math.abs(m5.getCurrentPosition()) < 850) && (m6.getCurrentPosition() > 500)) {
                    m6.setPower(-gamepad2.left_stick_y * .5);
                } else if ((Math.abs(m5.getCurrentPosition()) > 100) && (Math.abs(m5.getCurrentPosition()) < 850) && (m6.getCurrentPosition() < 500)) {
                    m6.setPower(1);
                } else {
                    m6.setPower(0);
                }
            }

            // code for run to position
            if (gamepad2.start) {
                m5.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                m6.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            } else if (gamepad2.options || gamepad2.share) {
                m5.setTargetPosition(0);
                m5.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                m6.setTargetPosition(0);
                m6.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
            if (gamepad2.y) {
                m6.setTargetPosition(595);
                m6.setPower(0.5);
                while (m6.isBusy()) {
                    drive(totalpowervalue);
                }
                m5.setTargetPosition(-1734);
                m5.setPower(-0.5);
                while (m5.isBusy()) {
                    drive(totalpowervalue);
                }
                m6.setTargetPosition(251);
                m6.setPower(-0.5);
                while (m6.isBusy()) {
                    drive(totalpowervalue);
                }
            } else if (gamepad2.x) {
                m6.setTargetPosition(595);
                m6.setPower(0.5);
                while (m6.isBusy()) {
                    drive(totalpowervalue);
                }
                m5.setTargetPosition(-1445);
                m5.setPower(-0.5);
                while (m5.isBusy()) {
                    drive(totalpowervalue);
                }
                m6.setTargetPosition(251);
                m6.setPower(-0.5);
                while (m6.isBusy()) {
                    drive(totalpowervalue);
                }
            } else if (gamepad2.a) {
                m6.setTargetPosition(595);
                m6.setPower(0.5);
                while (m6.isBusy()) {
                    drive(totalpowervalue);
                }
                m5.setTargetPosition(-10);
                m5.setPower(0.5);
                while (m5.isBusy()) {
                    drive(totalpowervalue);
                }
                m6.setTargetPosition(30);
                m6.setPower(-0.5);
                while (m6.isBusy()) {
                    drive(totalpowervalue);
                }

            }

        }

        // stop motors at end
        m1.setPower(0);
        m2.setPower(0);
        m3.setPower(0);
        m4.setPower(0);
        m5.setPower(0);
        m6.setPower(0);
        m7.setPower(0);
        m8.setPower(0);
    }

    void drive(double pw) {
        double y = -gamepad1.left_stick_y; // Remember, this is reversed!
        double x = -gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
        double rx = -gamepad1.right_stick_x;
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y - x + rx) / denominator;
        double backLeftPower = (y + x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;
        m2.setPower(frontLeftPower * pw);
        m1.setPower(backLeftPower * pw);
        m3.setPower(frontRightPower * pw);
        m4.setPower(backRightPower * pw);
    }

}
