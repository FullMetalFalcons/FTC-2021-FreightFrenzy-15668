package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.google.ar.core.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.vuforia.Vec2F;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import java.util.HashMap;
import java.util.Map;

/*
 * This is a simple routine to test translational drive capabilities.
 */
@Autonomous(name = "TestAutoOn", group = "Final")
public class TestAutoON extends LinearOpMode {

    DcMotor m5, m6;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        m5 = hardwareMap.dcMotor.get("rotater");
        m6 = hardwareMap.dcMotor.get("arm");
        m5.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        m6.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        Pose2d startPose = new Pose2d(0, 0, Math.toRadians(0));

        Map<String, Trajectory> rtlist = new HashMap<>() ;

        Vector2d Waypoint = new Vector2d(24,24);
        Trajectory FirstPoint = drive.trajectoryBuilder(startPose).splineToConstantHeading(Waypoint, Math.toRadians(0)).build();
        rtlist.put("FristPoint", FirstPoint);
        Vector2d Waypoint2 = new Vector2d(48,48);
        Trajectory SecondPoint = drive.trajectoryBuilder(FirstPoint.end()).splineToConstantHeading(Waypoint2, Math.toRadians(0)).build();
        rtlist.put("SecondPoint", SecondPoint);

        waitForStart();

        if (isStopRequested()) return;

        drive.followTrajectory(rtlist.get("FristPoint"));
        drive.followTrajectory(rtlist.get("SecondPoint"));

    }

}
