package org.firstinspires.ftc.teamcode.cnapsys.teleOps.auto.constants;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.cnapsys.core.Robot;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

public class RedTopAutoHomosapiensConstants {
    public static final Pose START_POS = new Pose(114.740, 128.127, Math.toRadians(0));

    private final List<RedTopAutoHomosapiensConstants.Step> steps = new ArrayList<>();
    private int currentStepIndex = 0;

    // Inner class to represent a single step
    public static class Step {
        public final Action action;
        public final PathChain path;
        public final double speed;
        public final BooleanSupplier completionCondition;

        // For MOVE actions (with default or custom completion condition)
        private Step(Action action, PathChain path, double speed, BooleanSupplier condition) {
            this.action = action;
            this.path = path;
            this.speed = speed;
            this.completionCondition = condition;
        }

        // For SHOOT actions (with default or custom completion condition)
        private Step(Action action, BooleanSupplier condition) {
            this.action = action;
            this.path = null;
            this.speed = 1.0;
            this.completionCondition = condition;
        }

        // Check if this step is complete
        public boolean isComplete() {
            return completionCondition.getAsBoolean();
        }
    }

    // Builder methods for creating steps
    public static class StepBuilder {
        // MOVE with custom speed
        public static RedTopAutoHomosapiensConstants.Step move(PathChain path, double speed, BooleanSupplier completionCondition) {
            return new RedTopAutoHomosapiensConstants.Step(Action.MOVE, path, speed, completionCondition);
        }

        // SHOOT with custom condition
        public static RedTopAutoHomosapiensConstants.Step shoot(BooleanSupplier completionCondition) {
            return new RedTopAutoHomosapiensConstants.Step(Action.SHOOT, completionCondition);
        }

        // IDLE/WAIT with condition
        public static RedTopAutoHomosapiensConstants.Step waitFor(BooleanSupplier condition) {
            return new RedTopAutoHomosapiensConstants.Step(Action.IDLE, condition);
        }
    }

    public RedTopAutoHomosapiensConstants(Follower follower, Robot robot, ElapsedTime timer) {
        List<PathChain> paths = buildPaths(follower);
        steps.add(RedTopAutoHomosapiensConstants.StepBuilder.move(paths.get(0), 0.9, () -> timer.milliseconds() > 100));
        steps.add(RedTopAutoHomosapiensConstants.StepBuilder.waitFor(() -> !robot.shooter.isBusy() && !robot.turret.isBusy()));
        steps.add(RedTopAutoHomosapiensConstants.StepBuilder.shoot(() -> !robot.blocker.isBusy()));
        steps.add(RedTopAutoHomosapiensConstants.StepBuilder.waitFor(() -> !follower.isBusy()));
        steps.add(RedTopAutoHomosapiensConstants.StepBuilder.move(paths.get(1), 1, () -> !follower.isBusy()));
        steps.add(RedTopAutoHomosapiensConstants.StepBuilder.move(paths.get(2), 1, () -> !follower.isBusy()));
        steps.add(RedTopAutoHomosapiensConstants.StepBuilder.waitFor(() -> timer.milliseconds() >= 1000));
        steps.add(RedTopAutoHomosapiensConstants.StepBuilder.move(paths.get(3), 1, () -> !follower.isBusy()));
        steps.add(RedTopAutoHomosapiensConstants.StepBuilder.waitFor(() -> timer.milliseconds() >= 200));
        steps.add(RedTopAutoHomosapiensConstants.StepBuilder.waitFor(() -> !robot.shooter.isBusy() && !robot.turret.isBusy()));
        steps.add(RedTopAutoHomosapiensConstants.StepBuilder.shoot(() -> !robot.blocker.isBusy()));

        //LINE 1
        steps.add(RedTopAutoHomosapiensConstants.StepBuilder.move(paths.get(4), 1, () -> !follower.isBusy()));
        steps.add(RedTopAutoHomosapiensConstants.StepBuilder.move(paths.get(5), 1, () -> !follower.isBusy()));
        steps.add(RedTopAutoHomosapiensConstants.StepBuilder.waitFor(() -> timer.milliseconds() >= 200));
        steps.add(RedTopAutoHomosapiensConstants.StepBuilder.waitFor(() -> !robot.shooter.isBusy() && !robot.turret.isBusy()));
        steps.add(RedTopAutoHomosapiensConstants.StepBuilder.shoot(() -> !robot.blocker.isBusy()));

        steps.add(RedTopAutoHomosapiensConstants.StepBuilder.move(paths.get(6), 1, () -> !follower.isBusy()));
        steps.add(RedTopAutoHomosapiensConstants.StepBuilder.waitFor(() -> timer.milliseconds() >= 1000));
        steps.add(RedTopAutoHomosapiensConstants.StepBuilder.move(paths.get(7), 1, () -> !follower.isBusy()));

        //GATE CYCLE
        steps.add(RedTopAutoHomosapiensConstants.StepBuilder.move(paths.get(8), 1, () -> !follower.isBusy()));
        steps.add(RedTopAutoHomosapiensConstants.StepBuilder.waitFor(() -> timer.milliseconds() >= 1000));
        steps.add(RedTopAutoHomosapiensConstants.StepBuilder.move(paths.get(7), 1, () -> !follower.isBusy()));
    }

    private List<PathChain> buildPaths(Follower follower) {
        List<PathChain> paths = new ArrayList<>();

        // Path 1
        paths.add(follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(114.740, 128.127),
                        new Pose(84.030, 77.737)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
                .build());

        // Path 2
        paths.add(follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(84.030, 77.737),
                        new Pose(94.880, 57.249),
                        new Pose(129.149, 59.403)
                ))
                .setTangentHeadingInterpolation()
                .build());

        // Path 3
        paths.add(follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(129.149, 59.403),
                        new Pose(128.635, 68.231)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-90))
                .build());

        // Path 4
        paths.add(follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(128.635, 68.231),
                        new Pose(102.556, 62.201),
                        new Pose(84.030, 77.737)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(-90), Math.toRadians(0))
                .build());

        // Path 5 (was Path7 in original)
        paths.add(follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(84.030, 77.737),
                        new Pose(91.944, 85.476),
                        new Pose(126.997, 83.604)
                ))
                .setTangentHeadingInterpolation()
                .build());

        // Path 6 (was first Path8 in original - got overwritten)
        paths.add(follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(126.997, 83.604),
                        new Pose(91.944, 85.476),
                        new Pose(84.030, 77.737)
                ))
                .setTangentHeadingInterpolation()
                .setReversed()
                .build());

        // Path 7 (was first Path9 in original - got overwritten)
        paths.add(follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(84.030, 77.737),
                        new Pose(102.556, 62.201),
                        new Pose(128.635, 68.231)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-90))
                .build());

        // Path 8 (was second Path8 in original - overwrote first)
        paths.add(follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(128.635, 68.231),
                        new Pose(112.374, 68.224)
                ))
                .setConstantHeadingInterpolation(Math.toRadians(-90))
                .build());

        // Path 9 (was second Path9 in original - overwrote first)
        paths.add(follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(112.374, 68.224),
                        new Pose(128.635, 68.231)
                ))
                .setConstantHeadingInterpolation(Math.toRadians(-90))
                .build());

        return paths;
    }

    public RedTopAutoHomosapiensConstants.Step getCurrentStep() {
        if (currentStepIndex >= 0 && currentStepIndex < steps.size()) {
            return steps.get(currentStepIndex);
        }
        return null;
    }

    public void advanceStep() {
        currentStepIndex++;
    }

    public boolean isComplete() {
        return currentStepIndex >= steps.size();
    }
}
