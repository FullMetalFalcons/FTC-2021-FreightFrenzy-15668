package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Autonomous(name = "Final Auto Red House V3", group = "Final")

public class FinalAutoRedHouse3 extends LinearOpMode {

    DcMotor m5, m6;
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
    int arm = 0;
    int rotater = 0;
    double speed = 0;
    double vnum = 0;

    @Override
    public void runOpMode() {

        // configuration of motors
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
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
        m7.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        m8.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        Pose2d startPose = new Pose2d(0, 0, Math.toRadians(0));
        Map<String, Trajectory> tlist = new HashMap<>() ;

        Vector2d Waypoint = new Vector2d(-23.5,-1.5);
        Trajectory FirstPoint = drive.trajectoryBuilder(startPose).lineToConstantHeading(Waypoint).build();
        tlist.put("FristPoint", FirstPoint);

        Pose2d Waypoint2 = new Pose2d(0,0, Math.toRadians(90));
        Trajectory SecondPoint = drive.trajectoryBuilder(FirstPoint.end()).lineToLinearHeading(Waypoint2).build();
        tlist.put("SecondPoint", SecondPoint);

        Trajectory ThirdPoint = drive.trajectoryBuilder(SecondPoint.end()).strafeRight(3).build();
        tlist.put("ThirdPoint", ThirdPoint);

        Trajectory FourthPoint = drive.trajectoryBuilder(ThirdPoint.end()).forward(37).build();
        tlist.put("FourthPoint", FourthPoint);

        Pose2d Waypoint3 = new Pose2d(2,31, Math.toRadians(90));
        Trajectory FifthPoint = drive.trajectoryBuilder(FourthPoint.end()).lineToLinearHeading(Waypoint3).build();
        tlist.put("FifthPoint", FifthPoint);

        Pose2d Waypoint4 = new Pose2d(4,0, Math.toRadians(90));
        Trajectory SeventhPoint = drive.trajectoryBuilder(FifthPoint.end()).lineToLinearHeading(Waypoint4).build();
        tlist.put("SeventhPoint", SeventhPoint);

        Pose2d Waypoint5 = new Pose2d(-20,-2, Math.toRadians(90));
        Trajectory EighthPoint = drive.trajectoryBuilder(SeventhPoint.end()).lineToLinearHeading(Waypoint5).build();
        tlist.put("EighthPoint", EighthPoint);

        Pose2d Waypoint6 = new Pose2d(0,0, Math.toRadians(90));
        Trajectory NinthPoint = drive.trajectoryBuilder(EighthPoint.end()).lineToLinearHeading(Waypoint6).build();
        tlist.put("NinthPoint", NinthPoint);


        initVuforia();
        initTfod();
        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(1.0, 16.0 / 9.0);
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
                            telemetry.addData("left: ", recognition.getLeft());
                            telemetry.addData("h: ", recognition.getHeight());
                            vnum = recognition.getLeft();
                            if (vnum < 100 && recognition.getHeight() < 275) {
                                height = "left";
                            } else if (vnum > 300 && vnum < 450 && recognition.getHeight() < 275) {
                                height = "center";
                            } else if (vnum > 650 && recognition.getHeight() < 275) {
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
            height = "right";
        }

        if (height == "left") {
            arm = 475;
            rotater = 1375;
            speed = -0.10;
        } else if (height =="center") {
            arm = 932;
            rotater = 1447;
            speed = -0.5;
        } else if (height =="right") {
            arm = 1333;
            rotater = 1458;
            speed = -0.5;
        }

        m6.setTargetPosition(arm);
        m6.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m6.setPower(0.5);
        drive.followTrajectory(tlist.get("FristPoint"));
        while (m6.isBusy()) {
        }

        m5.setTargetPosition(rotater);
        m5.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m5.setPower(0.5);
        while (m5.isBusy()) {
        }

        sleep(1600);

        m8.setPower(speed);
        sleep(1500);
        m8.setPower(0);

        m5.setTargetPosition(0);
        m5.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m5.setPower(0.5);
        drive.followTrajectory(tlist.get("SecondPoint"));
        while (m5.isBusy()) {
        }

        m6.setTargetPosition(0);
        m6.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m6.setPower(0.5);
        while (m6.isBusy()) {
        }

        sleep(100);

        drive.setMotorPowers(0.7,-0.7,0.7,-0.7);
        sleep(500);

        drive.followTrajectory(tlist.get("FourthPoint"));
        sleep(100);

        drive.setMotorPowers(0.1,0.5,0.1,0.5);
        m8.setPower(1);
        sleep(2000);
        drive.setMotorPowers(0,0,0,0);
        m8.setPower(1);
        sleep(250);
        m8.setPower(0);
        sleep(100);

        drive.followTrajectory(tlist.get("FifthPoint"));
        sleep(100);

        //drive.setMotorPowers(0.7,-0.7,0.7,0.7);
        //sleep(600);

        //drive.setMotorPowers(0,0,0,0);
        //sleep(100);

        drive.followTrajectory(tlist.get("SeventhPoint"));
        sleep(100);

        m6.setTargetPosition(1333);
        m6.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m6.setPower(0.5);
        drive.followTrajectory(tlist.get("EighthPoint"));
        while (m6.isBusy()) {
        }

        m5.setTargetPosition(-1696);
        m5.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m5.setPower(0.5);
        while (m5.isBusy()) {
        }

        sleep(1800);

        m8.setPower(-0.5);
        sleep(2000);
        m8.setPower(0);

        m5.setTargetPosition(0);
        m5.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m5.setPower(0.5);
        while (m5.isBusy()) {
        }

        m6.setTargetPosition(0);
        m6.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        m6.setPower(0.5);
        drive.followTrajectory(tlist.get("NinthPoint"));
        while (m6.isBusy()) {
        }
        sleep(100);

        drive.setMotorPowers(0.7,-0.7,0.7,-0.7);
        sleep(500);

        drive.setMotorPowers(0,0,0,0);
        sleep(100);

        drive.setMotorPowers(0.4,0.5,0.5,0.5);
        sleep(1500);

        drive.setMotorPowers(0,0,0,0);
        sleep(100);

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




    }
