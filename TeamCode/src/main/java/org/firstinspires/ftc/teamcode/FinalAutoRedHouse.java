package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.drive.StandardTrackingWheelLocalizer;

@Disabled
@Autonomous(name = "Final Auto Red House", group = "Final")

public class FinalAutoRedHouse extends LinearOpMode {

    DcMotor m1, m2, m3, m4, m5, m6;
    DcMotorEx m7, m8;

    private static final String TFOD_MODEL_ASSET = "/sdcard/FIRST/tflitemodels/mycustommodelv2.tflite";
    private static final String[] LABELS = {
            "left",
            "center",
            "right"
    };
    private static final String VUFORIA_KEY = "AW4Kjyf/////AAABmXIr/SXbv02xp0txPYNnhm0n1cDgB6aMDCKParQ2uA0v/QtUkGssXl5ck9XB4uIGe6O7g6l511DOFM85owy9jYanoHy+fVF9adBCoF28B/E7TGx/4YDfakBltEG7fzRMt+dBBwEQ13WbNEoUtNx5+HUFI3RaKcxVa0e+0kb+FGvjh9+0Wvy4E2zfPzxrNHFnhF436L48+Z7bz116uAk5lRlpluKY303A3fW5bqh85ze2elNNMLE2SKXjpk9u4hALYdkKCMnc1Oos9kAok7k41I/4gPo4IjqwUot/wXdUE7znU7nP/4QB0cVe83x8VqnmG7sEhxmfk5tIyknYJ/QEXiL0+iucqKw1oGL4q041vFF5";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    String whatisrec;

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
                            telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                            telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                    recognition.getLeft(), recognition.getTop());
                            telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                    recognition.getRight(), recognition.getBottom());
                            whatisrec = recognition.getLabel();
                            i++;
                        }
                        telemetry.update();
                    }
                }
            }
        }

        whatisrec = "center";

        waitForStart();

        //center
        if (whatisrec == "right" || whatisrec == "left") {

            // out and arm

            while (opModeIsActive()) {
                setMotorPowers(-0.6,-0.6,-0.6,-0.6);
                myLocalizer.update();
                Pose2d mypose = myLocalizer.getPoseEstimate();
                m6.setTargetPosition(800);
                m6.setPower(0.5);
                if (mypose.getY() <= -13) {
                    stopMotors();
                    break;
                }
            }
            stopMotors();

            m5.setTargetPosition(1300);
            m5.setPower(0.5);
            while (m5.isBusy()) {
            }

            m8.setPower(-0.5);
            sleep(2000);
            stopMotors();
            m8.setPower(0);

            m5.setTargetPosition(0);
            m5.setPower(-0.5);
            while (m5.isBusy()) {
            }

            m6.setTargetPosition(5);
            m6.setPower(-0.5);
            while (m6.isBusy()) {
            }

            // rotate and in

            while (opModeIsActive()) {
                setMotorPowers(-0.3,-0.3,0.3,0.3);
                myLocalizer.update();
                Pose2d mypose = myLocalizer.getPoseEstimate();
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
            m8.setPower(0);

            stopMotors();
            m8.setPower(1);
            sleep(250);

            // back

            m8.setPower(0);
            setMotorPowers(-0.3,-0.1,-0.3,-0.3);
            sleep(1000);

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

                m6.setTargetPosition(1400);
                m6.setPower(0.5);

                if (mypose.getY() <= -13) {
                    break;
                }
            }
            stopMotors();

            m5.setTargetPosition(-1400);
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
                if (mypose.getX() <= -21) {
                    stopMotors();
                    break;
                }
            }
            stopMotors();

            setMotorPowers(-1,1,-1,1);
            sleep(650);
            stopMotors();

            setMotorPowers(1,1,-1,-1);
            sleep(410);
            stopMotors();

        //left
        } else if (whatisrec == "center") {

            // out and arm

            setMotorPowers(-0.2,-0.2,-0.2,-0.2);
            sleep(100);
            stopMotors();

            while (opModeIsActive()) {
                setMotorPowers(0.5,-0.5,0.5,-0.5);
                myLocalizer.update();
                Pose2d mypose = myLocalizer.getPoseEstimate();
                if (mypose.getX() >= 2) {
                    stopMotors();
                    break;
                }
            }
            stopMotors();

            while (opModeIsActive()) {
                setMotorPowers(-0.6,-0.6,-0.6,-0.6);
                myLocalizer.update();
                Pose2d mypose = myLocalizer.getPoseEstimate();
                if (mypose.getY() <= -4) {
                    stopMotors();
                    break;
                }
            }
            stopMotors();

            m6.setTargetPosition(720);
            m6.setPower(0.5);
            while (m6.isBusy()) {
            }

            m5.setTargetPosition(700);
            m5.setPower(0.5);
            while (m5.isBusy()) {
            }

            m6.setTargetPosition(350);
            m6.setPower(-0.5);
            while (m6.isBusy()) {
            }

            m5.setTargetPosition(1750);
            m5.setPower(0.5);
            while (m5.isBusy()) {
            }

            m8.setPower(-0.5);
            sleep(2000);
            while (m8.isBusy()) {
            }
            m8.setPower(0);

            m5.setTargetPosition(700);
            m5.setPower(-0.5);
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
            sleep(1000);

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

                m6.setTargetPosition(1400);
                m6.setPower(0.5);

                if (mypose.getY() <= -13) {
                    break;
                }
            }
            stopMotors();

            m5.setTargetPosition(-1400);
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

            m6.setTargetPosition(-1);
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
                if (mypose.getX() <= -21) {
                    stopMotors();
                    break;
                }
            }
            stopMotors();

            setMotorPowers(-1,1,-1,1);
            sleep(650);
            stopMotors();

            setMotorPowers(1,1,-1,-1);
            sleep(410);
            stopMotors();

        } else {

            //out and arm

            while (opModeIsActive()) {
                setMotorPowers(-0.6,-0.6,-0.6,-0.6);
                myLocalizer.update();
                Pose2d mypose = myLocalizer.getPoseEstimate();

                m6.setTargetPosition(1300);
                m6.setPower(0.5);

                if (mypose.getY() <= -14) {
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
            sleep(1000);

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

                m6.setTargetPosition(1300);
                m6.setPower(0.5);

                if (mypose.getY() <= -13) {
                    break;
                }
            }
            stopMotors();

            m5.setTargetPosition(-1400);
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

            m6.setTargetPosition(-1);
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
                if (mypose.getX() <= -21) {
                    stopMotors();
                    break;
                }
            }
            stopMotors();

            setMotorPowers(-1,1,-1,1);
            sleep(650);
            stopMotors();

            setMotorPowers(1,1,-1,-1);
            sleep(410);
            stopMotors();

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
