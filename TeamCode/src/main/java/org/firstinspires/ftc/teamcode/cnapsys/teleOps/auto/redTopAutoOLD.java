package org.firstinspires.ftc.teamcode.cnapsys.teleOps.auto;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.cnapsys.core.Robot;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.Alliance;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.Colors;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.FieldState;


public class redTopAutoOLD extends OpMode {
    private Robot robot;
    private final Alliance alliance = Alliance.RED;
    private static final Pose startPos = new Pose(120.685, 127.204, Math.toRadians(126));
    private Paths paths;
    private int state = 0;
    private ElapsedTime timer = new ElapsedTime();
    private Limelight3A limeLight;

    public static class Paths {
        public PathChain Path1;
        public PathChain Path2;
        public PathChain Path3;
        public PathChain Path4;
        public PathChain Path5;

        public Paths(Follower follower) {
            Path1 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    startPos,

                                    new Pose(103.435, 109.617)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(126))

                    .build();

            Path2 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(103.435, 109.617),
                                    new Pose(84.826, 81.181),
                                    new Pose(122.901, 83.385)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            Path3 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(122.901, 83.385),

                                    new Pose(103.215, 109.526)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(48))

                    .build();

            Path4 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(103.215, 109.526),
                                    new Pose(90.730, 66.781),
                                    new Pose(81.024, 61.160),
                                    new Pose(108.364, 58.694),
                                    new Pose(129.369, 59.150)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(0))

                    .build();

            Path5 = follower.pathBuilder().addPath(
                            new BezierCurve(
                                    new Pose(129.369, 59.150),
                                    new Pose(78.984, 41.512),
                                    new Pose(93.075, 83.093),
                                    new Pose(103.448, 109.504)
                            )
                    ).setConstantHeadingInterpolation(Math.toRadians(36))

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
        timer.reset();
        robot.indicatorLight.turnOff();
    }

    @Override
    public void stop() {
        Robot.defaultPose = robot.follower.getPose();
    }

    @Override
    public void loop() {
        if (limeLight.isRunning() && timer.seconds() >= 1) {
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
                robot.follower.followPath(paths.Path1);
                state = 1;
                break;
            case 1:
                if (!robot.follower.isBusy() && FieldState.isInit()) {
                    robot.shootAll();
                    state = 2;
                }
                break;
            case 2:
                if (!robot.isBusy()) {
                    robot.follower.setMaxPower(0.5);
                    robot.follower.followPath(paths.Path2);
                    timer.reset();
                    state = 3;
                }
                break;
            case 3:
                if (!robot.follower.isBusy() && timer.seconds() > 4) {
                    robot.follower.setMaxPower(1);
                    robot.follower.followPath(paths.Path3);
                    state = 4;
                }
                break;
            case 4:
                if (!robot.follower.isBusy()) {
                    robot.shootAll();
                    state = 5;
                }
                break;
            case 5:
                if (!robot.isBusy()) {
                    robot.follower.setMaxPower(0.5);
                    robot.follower.followPath(paths.Path4);
                    timer.reset();
                    state = 6;
                }
                break;
            case 6:
                if (!robot.follower.isBusy() && timer.seconds() > 4) {
                    robot.follower.setMaxPower(1);
                    robot.follower.followPath(paths.Path5);
                    state = 7;
                }
                break;
            case 7:
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
