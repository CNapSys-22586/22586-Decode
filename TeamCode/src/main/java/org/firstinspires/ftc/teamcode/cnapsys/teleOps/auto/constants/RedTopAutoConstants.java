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

public class RedTopAutoConstants {
    public static final Pose START_POS = new Pose(114.740, 128.127, Math.toRadians(0));

    private final List<Step> steps = new ArrayList<>();
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
        public static Step move(PathChain path, double speed, BooleanSupplier completionCondition) {
            return new Step(Action.MOVE, path, speed, completionCondition);
        }

        // SHOOT with custom condition
        public static Step shoot(BooleanSupplier completionCondition) {
            return new Step(Action.SHOOT, completionCondition);
        }

        // IDLE/WAIT with condition
        public static Step waitFor(BooleanSupplier condition) {
            return new Step(Action.IDLE, condition);
        }
    }

    public RedTopAutoConstants(Follower follower, Robot robot, ElapsedTime timer) {
        List<PathChain> paths = buildPaths(follower);
        steps.add(RedTopAutoConstants.StepBuilder.move(paths.get(0), 1, () -> timer.milliseconds() > 100));
        steps.add(RedTopAutoConstants.StepBuilder.waitFor(() -> !robot.shooter.isBusy() && !robot.turret.isBusy()));
        steps.add(RedTopAutoConstants.StepBuilder.shoot(() -> !robot.blocker.isBusy()));
        steps.add(RedTopAutoConstants.StepBuilder.waitFor(() -> !follower.isBusy()));
        steps.add(RedTopAutoConstants.StepBuilder.move(paths.get(1), 1, () -> !follower.isBusy()));
        steps.add(RedTopAutoConstants.StepBuilder.move(paths.get(2), 1, () -> !follower.isBusy()));
        steps.add(RedTopAutoConstants.StepBuilder.waitFor(() -> !robot.shooter.isBusy() && !robot.turret.isBusy()));
        steps.add(RedTopAutoConstants.StepBuilder.shoot(() -> !robot.blocker.isBusy()));

        steps.add(RedTopAutoConstants.StepBuilder.move(paths.get(3), 0.63, () -> !follower.isBusy()));
        steps.add(RedTopAutoConstants.StepBuilder.waitFor(() -> timer.milliseconds() >= 700));
        steps.add(RedTopAutoConstants.StepBuilder.move(paths.get(4), 1, () -> !follower.isBusy()));
        steps.add(RedTopAutoConstants.StepBuilder.move(paths.get(5), 1, () -> !follower.isBusy()));
        steps.add(RedTopAutoConstants.StepBuilder.waitFor(() -> !robot.shooter.isBusy() && !robot.turret.isBusy()));
        steps.add(RedTopAutoConstants.StepBuilder.shoot(() -> !robot.blocker.isBusy()));

        steps.add(RedTopAutoConstants.StepBuilder.move(paths.get(3), 0.63, () -> !follower.isBusy()));
        steps.add(RedTopAutoConstants.StepBuilder.waitFor(() -> timer.milliseconds() >= 700));
        steps.add(RedTopAutoConstants.StepBuilder.move(paths.get(4), 1, () -> !follower.isBusy()));
        steps.add(RedTopAutoConstants.StepBuilder.move(paths.get(5), 1, () -> !follower.isBusy()));
        steps.add(RedTopAutoConstants.StepBuilder.waitFor(() -> !robot.shooter.isBusy() && !robot.turret.isBusy()));
        steps.add(RedTopAutoConstants.StepBuilder.shoot(() -> !robot.blocker.isBusy()));

        steps.add(RedTopAutoConstants.StepBuilder.move(paths.get(3), 0.63, () -> !follower.isBusy()));
        steps.add(RedTopAutoConstants.StepBuilder.waitFor(() -> timer.milliseconds() >= 700));
        steps.add(RedTopAutoConstants.StepBuilder.move(paths.get(4), 1, () -> !follower.isBusy()));
        steps.add(RedTopAutoConstants.StepBuilder.move(paths.get(5), 1, () -> !follower.isBusy()));
        steps.add(RedTopAutoConstants.StepBuilder.waitFor(() -> !robot.shooter.isBusy() && !robot.turret.isBusy()));
        steps.add(RedTopAutoConstants.StepBuilder.shoot(() -> !robot.blocker.isBusy()));

        steps.add(RedTopAutoConstants.StepBuilder.move(paths.get(3), 0.63, () -> !follower.isBusy()));
        steps.add(RedTopAutoConstants.StepBuilder.waitFor(() -> timer.milliseconds() >= 700));
        steps.add(RedTopAutoConstants.StepBuilder.move(paths.get(4), 1, () -> !follower.isBusy()));
        steps.add(RedTopAutoConstants.StepBuilder.move(paths.get(5), 1, () -> !follower.isBusy()));
        steps.add(RedTopAutoConstants.StepBuilder.waitFor(() -> !robot.shooter.isBusy() && !robot.turret.isBusy()));
        steps.add(RedTopAutoConstants.StepBuilder.shoot(() -> !robot.blocker.isBusy()));

        steps.add(RedTopAutoConstants.StepBuilder.move(paths.get(6), 1, () -> !follower.isBusy()));
        steps.add(RedTopAutoConstants.StepBuilder.move(paths.get(7), 1, () -> !follower.isBusy()));
        steps.add(RedTopAutoConstants.StepBuilder.waitFor(() -> !robot.shooter.isBusy() && !robot.turret.isBusy()));
        steps.add(RedTopAutoConstants.StepBuilder.shoot(() -> !robot.blocker.isBusy()));
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
                .addPath(new BezierCurve(
                        new Pose(129.149, 59.403),
                        new Pose(94.924, 57.326),
                        new Pose(84.030, 77.737)
                ))
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build());

        // Path 4
        paths.add(follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(84.030, 77.737),
                        new Pose(131.564, 60.798)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(-53), Math.toRadians(28))
                .build());

        // Path 5
        paths.add(follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(131.564, 60.798),
                        new Pose(131.440, 54.743)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(28), Math.toRadians(0))
                .build());

        // Path 6
        paths.add(follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(131.440, 54.743),
                        new Pose(105.916, 58.364),
                        new Pose(84.030, 77.737)
                ))
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build());

        // Path 7
        paths.add(follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(84.030, 77.737),
                        new Pose(91.944, 85.476),
                        new Pose(126.997, 83.604)
                ))
                .setTangentHeadingInterpolation()
                .build());

        // Path 8
        paths.add(follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(126.997, 83.604),
                        new Pose(93.268, 107.281)
                ))
                .setTangentHeadingInterpolation()
                .setReversed()
                .build());

        return paths;
    }

    public Step getCurrentStep() {
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