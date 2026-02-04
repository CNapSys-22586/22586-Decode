package org.firstinspires.ftc.teamcode.cnapsys.teleOps.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.teamcode.cnapsys.core.Robot;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.Alliance;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.Colors;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.FieldState;

@Autonomous(name="Red Far Auto")
public class redFarAuto extends OpMode {
    private Robot robot;
    private final Alliance alliance = Alliance.RED;
    private static final Pose startPos = new Pose(87.726, 8.154, Math.toRadians(90));
    private Paths paths;
    private int state = 0;
    private final ElapsedTime timer = new ElapsedTime();
    private Limelight3A limeLight;
    public static class Paths {
        public PathChain Path1;
        public PathChain Path2;
        public PathChain Path4;
        public PathChain Path3;

        public Paths(Follower follower) {
            Path1 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    startPos,

                                    new Pose(88.094, 14.015)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(61))

                    .build();

            Path2 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(88.094, 14.015),
                                    new Pose(104.802, 9.201),
                                    new Pose(132.201, 9.513)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(-6))

                    .build();

            Path4 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(132.201, 9.513),

                                    new Pose(133.043, 12.851)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            Path3 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(133.043, 12.851),

                                    new Pose(91.379, 17.575)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(61))

                    .build();
        }
    }

    @Override
    public void init() {
        Robot.defaultPose = startPos;
        robot = new Robot(hardwareMap, alliance, false);
        robot.intake.enable();
        limeLight = hardwareMap.get(Limelight3A.class, "limelight");
        limeLight.start();
        robot.indexer.fillSlot(Colors.PURPLE, Colors.PURPLE, Colors.GREEN);
        paths = new Paths(robot.follower);
    }

    @Override
    public void init_loop() {
        if (limeLight.isConnected()) robot.indicatorLight.setColor(Colors.GREEN);
        else robot.indicatorLight.setColor(Colors.RED);
    }

    @Override
    public void start() {
        robot.indicatorLight.turnOff();
    }

    @Override
    public void stop() {
        Robot.defaultPose = robot.follower.getPose();
    }

    @Override
    public void loop() {
        if (limeLight.isRunning()) {
            LLResult result = limeLight.getLatestResult();
            if (result != null && result.isValid()) {
                int id = result.getFiducialResults().get(0).getFiducialId();
                FieldState.setPattern(id);
                limeLight.stop();
            }
        }
        robot.tm.addData("Auto State: ", state);
        switch (state) {
            case -1:
                break;
            case 0:
                if (FieldState.isInit()) {
                    robot.follower.followPath(paths.Path1);
                    state = 1;
                }
                break;
            case 1:
                if (!robot.follower.isBusy()) {
                    robot.shootAll();
                    state = 2;
                }
                break;
            case 2:
                if (!robot.isBusy()) {
                    robot.follower.setMaxPower(0.78);
                    robot.follower.followPath(paths.Path2);
                    timer.reset();
                    state = 3;
                }
                break;
            case 3:
                if (!robot.follower.isBusy() && timer.seconds() > 3.5) {
                    robot.follower.setMaxPower(1);
                    robot.follower.followPath(paths.Path4);
                    state = 4;
                }
                break;
            case 4:
                if (!robot.follower.isBusy()) {
                    robot.follower.followPath(paths.Path3);
                    state = 5;
                }
                break;
            case 5:
                if (!robot.follower.isBusy()) {
                    robot.shootAll();
                    state = -2;
                }
                break;
            case -2:
                if (!robot.isBusy()) {
                    Robot.defaultPose = robot.follower.getPose();
                    state = -1;
                }
                break;
        }
        robot.update();
        robot.tm.update(telemetry);
    }
}
