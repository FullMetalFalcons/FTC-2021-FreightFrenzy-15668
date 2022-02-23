// package defined
package org.firstinspires.ftc.teamcode;

// import classes
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.drive.StandardTrackingWheelLocalizer;

// teleop is defined
@TeleOp(name = "TeleOp test", group = "Final")

// builds on LinearOpMode
public class TeleOpTest extends LinearOpMode {

    // defines hardware
    DcMotor m1, m2, m3, m4, m5, m6, m7;
    BNO055IMU imu;

    // teleop code
    public void runOpMode() {

        // defines hardware
        m1 = hardwareMap.dcMotor.get("fr_front_encoder");
        m2 = hardwareMap.dcMotor.get("br");
        m3 = hardwareMap.dcMotor.get("bl_left_encoder");
        m4 = hardwareMap.dcMotor.get("fl_right_encoder");
        m5 = hardwareMap.dcMotor.get("rotater");
        m6 = hardwareMap.dcMotor.get("arm");
        m7 = hardwareMap.dcMotor.get("wheel");
        //m3.setDirection(DcMotor.Direction.REVERSE);
        //m1.setDirection(DcMotor.Direction.REVERSE);
        //m4.setDirection(DcMotor.Direction.REVERSE);
        m2.setDirection(DcMotor.Direction.REVERSE);
        m1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        m2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        m3.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        m4.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        m5.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        m6.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // reset encoders - stop
        m5.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m6.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m7.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // reset encoders - start
        m1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        m2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        m3.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        m4.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        m5.setTargetPosition(0);
        m5.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m6.setTargetPosition(0);
        m6.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // setup imu
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.accelerationIntegrationAlgorithm = null;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.calibrationData = null;
        parameters.calibrationDataFile = "";
        parameters.loggingEnabled = false;
        parameters.loggingTag = "Who cares.";
        imu.initialize(parameters);
        Orientation orientation;
        StandardTrackingWheelLocalizer myLocalizer = new StandardTrackingWheelLocalizer(hardwareMap);
        myLocalizer.setPoseEstimate(new Pose2d(0,0, Math.toRadians(90)));

        // sets up variables for all toggles
        double totalpowervalue = 1;
        boolean halfPower = false;
        boolean stickPressed = false;

        // start telemetry
        telemetry.addData("arm: ", m6.getCurrentPosition());
        telemetry.addData("rotater", m5.getCurrentPosition());
        telemetry.addData("x", myLocalizer.getPoseEstimate().getX());
        telemetry.addData("y", myLocalizer.getPoseEstimate().getY());
        telemetry.addData("h", myLocalizer.getPoseEstimate().getHeading());
        telemetry.update();

        waitForStart();

        // opmode code
        while (opModeIsActive()){

            myLocalizer.update();
            Pose2d mypose = myLocalizer.getPoseEstimate();

            // telemetry  data
            telemetry.addData("arm: ", m6.getCurrentPosition());
            telemetry.addData("rotater", m5.getCurrentPosition());
            telemetry.addData("x", mypose.getX());
            telemetry.addData("y", mypose.getY());
            telemetry.addData("h", mypose.getHeading());
            telemetry.update();

            if (!stickPressed && ( (gamepad1.left_stick_button) || (gamepad1.right_stick_button) ) ) {
                halfPower = !halfPower;
                stickPressed = true;
            } else if (stickPressed && !( (gamepad1.left_stick_button) || (gamepad1.right_stick_button) ) ) {
                stickPressed = false;
            }
            if (halfPower == true) {
                totalpowervalue = 0.25;
            } else if (halfPower == false) {
                totalpowervalue = 1;
            }

            drive();

            if (gamepad1.start) {
                m5.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                m6.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            } else if (gamepad1.options || gamepad1.share) {
                m5.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                m6.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }

            if (gamepad1.right_trigger > 0.1) {
                m6.setPower(gamepad1.right_trigger);
            } else if (gamepad1.left_trigger > 0.1) {
                m6.setPower(-gamepad1.left_trigger);
            } else if (gamepad1.right_bumper) {
                m5.setPower(0.5);
            } else if (gamepad1.left_bumper) {
                m5.setPower(-0.5);
            } else if (gamepad1.a) {
                m6.setTargetPosition(595);
                m6.setPower(0.75);
                while (m6.isBusy()) {
                    drive();
                }
                m5.setTargetPosition(-1243);
                m5.setPower(-0.75);
                while (m5.isBusy()) {
                    drive();
                }
                m6.setTargetPosition(148);
                m6.setPower(-0.75);
                while (m6.isBusy()) {
                    drive();
                }
            } else if (gamepad1.b) {
                m6.setTargetPosition(595);
                m6.setPower(0.75);
                while (m6.isBusy()) {
                    drive();
                }
                m5.setTargetPosition(0);
                m5.setPower(0.75);
                while (m5.isBusy()) {
                    drive();
                }
                m6.setTargetPosition(108);
                m6.setPower(-0.75);
                while (m6.isBusy()) {
                    drive();
                }
            } else {
                m5.setPower(0);
                m6.setPower(0);
            }

            if (gamepad1.dpad_up) {
                m6.setTargetPosition(4379);
                m6.setPower(0.5);
                while (m6.isBusy()) {
                    drive();
                }
                m5.setTargetPosition(-572);
                m5.setPower(-0.5);
                while (m5.isBusy()) {
                    drive();
                }
            }

            if (gamepad1.x) {
                m7.setPower(Range.clip(m7.getPower() - .01, -1, 0));
            } else {
                m7.setPower(0);
            }

        }

        // stop motors at end
        m1.setPower(0);
        m2.setPower(0);
        m3.setPower(0);
        m4.setPower(0);
        m5.setPower(0);
        m6.setPower(0);

    }

    void drive() {
        double y = -gamepad1.left_stick_y; // Remember, this is reversed!
        double x = -gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
        double rx = -gamepad1.right_stick_x;
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y - x + rx) / denominator;
        double backLeftPower = (y + x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;
        m2.setPower(frontLeftPower);
        m1.setPower(backLeftPower);
        m3.setPower(frontRightPower);
        m4.setPower(backRightPower);
    }

}

