package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.drive.StandardTrackingWheelLocalizer;

import java.util.List;

@Autonomous(name = "Final Auto Red Wheel", group = "Final")

public class FinalAutoRedWheel extends LinearOpMode {

    DcMotor m1, m2, m3, m4, m5, m6;
    DcMotorEx m7, m8;

    private static final String TFOD_MODEL_ASSET = "/sdcard/FIRST/tflitemodels/2model2.tflite";
    private static final String[] LABELS = {
            "left",
            "center",
            "right"
    };
    private static final String VUFORIA_KEY = "AW4Kjyf/////AAABmXIr/SXbv02xp0txPYNnhm0n1cDgB6aMDCKParQ2uA0v/QtUkGssXl5ck9XB4uIGe6O7g6l511DOFM85owy9jYanoHy+fVF9adBCoF28B/E7TGx/4YDfakBltEG7fzRMt+dBBwEQ13WbNEoUtNx5+HUFI3RaKcxVa0e+0kb+FGvjh9+0Wvy4E2zfPzxrNHFnhF436L48+Z7bz116uAk5lRlpluKY303A3fW5bqh85ze2elNNMLE2SKXjpk9u4hALYdkKCMnc1Oos9kAok7k41I/4gPo4IjqwUot/wXdUE7znU7nP/4QB0cVe83x8VqnmG7sEhxmfk5tIyknYJ/QEXiL0+iucqKw1oGL4q041vFF5";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    String height = "";
    double vnum = 0;

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

        initVuforia();
        initTfod();
        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(1.0, 16.0/9.0);
        }

        if (opModeIsActive() != true) {
            while (opModeIsActive() != true) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());
                        // step through the list of recognitions and display boundary info.
                        int i = 0;
                        for (Recognition recognition : updatedRecognitions) {
                            telemetry.addData("label: ", recognition.getLabel());
                            telemetry.addData("left: ", recognition.getLeft());
                            telemetry.addData("con: ", recognition.getConfidence());
                            vnum = recognition.getLeft();
                            if (vnum < 100) {
                                height = "left";
                            } else if (vnum > 180 && vnum < 350) {
                                height = "center";
                            } else if (vnum > 570) {
                                height = "right";
                            }
                            telemetry.addData("height: ", height);
                            i++;
                        }
                        telemetry.update();
                    }
                }
            }
        }

        waitForStart();

        if (height == "") {
            height = "left";
        }

        if (height == "left") {

            setMotorPowers(-0.3,0.3,-0.3,0.3);
            sleep(700);
            stopMotors();

            while (opModeIsActive()) {
                setMotorPowers(-0.6,-0.6,-0.6,-0.6);

                m6.setTargetPosition(720);
                m6.setPower(0.5);

                myLocalizer.update();
                Pose2d mypose = myLocalizer.getPoseEstimate();
                if (mypose.getY() <= -24) {
                    stopMotors();
                    break;
                }
            }
            stopMotors();

            sleep(200);

            setMotorPowers(0.6,0.6,0.6,0.6);
            sleep(200);
            stopMotors();

            setMotorPowers(0.3,-0.3,0.3,-0.3);
            sleep(75);
            stopMotors();

            m5.setTargetPosition(-700);
            m5.setPower(0.5);
            while (m5.isBusy()) {
            }

            m6.setTargetPosition(370);
            m6.setPower(-0.5);
            while (m6.isBusy()) {
            }

            m5.setTargetPosition(-1350);
            m5.setPower(0.5);
            while (m5.isBusy()) {
            }

            m8.setPower(-0.5);
            sleep(2000);
            while (m8.isBusy()) {
            }
            m8.setPower(0);

            m5.setTargetPosition(-700);
            m5.setPower(0.5);
            while (m5.isBusy()) {
            }

            m6.setTargetPosition(720);
            m6.setPower(0.5);
            while (m6.isBusy()) {
            }

            m5.setTargetPosition(0);
            m5.setPower(-0.5);
            while (m5.isBusy()) {
            }

            m6.setTargetPosition(5);
            m6.setPower(-0.5);
            while (m6.isBusy()) {
            }

            while (opModeIsActive()) {
                setMotorPowers(-0.3,-0.3,0.3,0.3);
                myLocalizer.update();
                Pose2d mypose = myLocalizer.getPoseEstimate();
                if (mypose.getHeading() >= 4.7) {
                    break;
                }
            }
            stopMotors();

            setMotorPowers(-0.5,0.5,-0.5,0.5);
            m6.setTargetPosition(2714);
            m6.setPower(0.5);
            sleep(2000);
            stopMotors();

            setMotorPowers(-0.1,-0.1,-0.1,-0.1);
            m5.setTargetPosition(-2382);
            m5.setPower(0.5);
            sleep(6000);
            stopMotors();

            m7.setPower(0.1);
            sleep(3000);
            m7.setPower(0);

            sleep(100);

            setMotorPowers(0.5,0.5,0.5,0.5);
            sleep(350);
            stopMotors();

            m5.setTargetPosition(0);
            m5.setPower(0.5);
            while (m5.isBusy()) {
            }
            m6.setTargetPosition(-1);
            m6.setPower(0.5);
            while (m6.isBusy()) {
            }


        } else if (height == "center") {

            setMotorPowers(0.3,-0.3,0.3,-0.3);
            sleep(700);
            stopMotors();

            while (opModeIsActive()) {
                setMotorPowers(-0.6,-0.6,-0.6,-0.6);

                m6.setTargetPosition(720);
                m6.setPower(0.5);

                myLocalizer.update();
                Pose2d mypose = myLocalizer.getPoseEstimate();
                if (mypose.getY() <= -20) {
                    stopMotors();
                    break;
                }
            }
            stopMotors();

            sleep(200);

            setMotorPowers(0.6,0.6,0.6,0.6);
            sleep(200);
            stopMotors();

            setMotorPowers(-0.3,0.3,-0.3,0.3);
            sleep(600);
            stopMotors();

            m5.setTargetPosition(-1450);
            m5.setPower(0.5);
            while (m5.isBusy()) {
            }

            m8.setPower(-0.5);
            sleep(2000);
            while (m8.isBusy()) {
            }
            m8.setPower(0);

            m5.setTargetPosition(0);
            m5.setPower(-0.5);
            while (m5.isBusy()) {
            }

            m6.setTargetPosition(5);
            m6.setPower(-0.5);
            while (m6.isBusy()) {
            }

            while (opModeIsActive()) {
                setMotorPowers(-0.3,-0.3,0.3,0.3);
                myLocalizer.update();
                Pose2d mypose = myLocalizer.getPoseEstimate();
                if (mypose.getHeading() >= 4.7) {
                    break;
                }
            }
            stopMotors();

            setMotorPowers(-0.5,0.5,-0.5,0.5);
            m6.setTargetPosition(2714);
            m6.setPower(0.5);
            sleep(2000);
            stopMotors();

            setMotorPowers(-0.1,-0.1,-0.1,-0.1);
            m5.setTargetPosition(-2382);
            m5.setPower(0.5);
            sleep(6000);
            stopMotors();

            m7.setPower(0.1);
            sleep(3000);
            m7.setPower(0);

            sleep(100);

            setMotorPowers(0.5,0.5,0.5,0.5);
            sleep(350);
            stopMotors();

            m5.setTargetPosition(0);
            m5.setPower(0.5);
            while (m5.isBusy()) {
            }
            m6.setTargetPosition(-1);
            m6.setPower(0.5);
            while (m6.isBusy()) {
            }

        } else if (height == "right") {

            setMotorPowers(0.3,-0.3,0.3,-0.3);
            sleep(700);
            stopMotors();

            while (opModeIsActive()) {
                setMotorPowers(-0.6,-0.6,-0.6,-0.6);

                m6.setTargetPosition(1300);
                m6.setPower(0.5);

                myLocalizer.update();
                Pose2d mypose = myLocalizer.getPoseEstimate();
                if (mypose.getY() <= -20) {
                    stopMotors();
                    break;
                }
            }
            stopMotors();

            sleep(200);

            setMotorPowers(0.6,0.6,0.6,0.6);
            sleep(200);
            stopMotors();

            setMotorPowers(-0.3,0.3,-0.3,0.3);
            sleep(1350);
            stopMotors();

            m5.setTargetPosition(-1450);
            m5.setPower(0.5);
            while (m5.isBusy()) {
            }

            m8.setPower(-0.5);
            sleep(2000);
            while (m8.isBusy()) {
            }
            m8.setPower(0);

            m5.setTargetPosition(0);
            m5.setPower(-0.5);
            while (m5.isBusy()) {
            }

            m6.setTargetPosition(5);
            m6.setPower(-0.5);
            while (m6.isBusy()) {
            }

            while (opModeIsActive()) {
                setMotorPowers(-0.3,-0.3,0.3,0.3);
                myLocalizer.update();
                Pose2d mypose = myLocalizer.getPoseEstimate();
                if (mypose.getHeading() >= 4.7) {
                    break;
                }
            }
            stopMotors();

            setMotorPowers(-0.5,0.5,-0.5,0.5);
            m6.setTargetPosition(2714);
            m6.setPower(0.5);
            sleep(2000);
            stopMotors();

            setMotorPowers(-0.1,-0.1,-0.1,-0.1);
            m5.setTargetPosition(-2382);
            m5.setPower(0.5);
            sleep(6000);
            stopMotors();

            m7.setPower(0.1);
            sleep(3000);
            m7.setPower(0);

            sleep(100);

            setMotorPowers(0.5,0.5,0.5,0.5);
            sleep(350);
            stopMotors();

            m5.setTargetPosition(0);
            m5.setPower(0.5);
            while (m5.isBusy()) {
            }
            m6.setTargetPosition(5);
            m6.setPower(0.5);
            while (m6.isBusy()) {
            }


        }


        }

    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.7f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromFile(TFOD_MODEL_ASSET, LABELS);
    }

    void setMotorPowers(double v, double v1, double v2, double v3) {
        m4.setPower(v1);
        m3.setPower(v);
        m2.setPower(v2);
        m1.setPower(v3);
    }

    void stopMotors() {
        m4.setPower(0);
        m3.setPower(0);
        m2.setPower(0);
        m1.setPower(0);
    }

    }
