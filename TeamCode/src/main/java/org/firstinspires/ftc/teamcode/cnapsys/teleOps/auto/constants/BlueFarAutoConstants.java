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

public class BlueFarAutoConstants {
    public static final Pose START_POS = new Pose(56.000, 8.000, Math.toRadians(180));

    private final List<BlueFarAutoConstants.Step> steps = new ArrayList<>();
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
        public static BlueFarAutoConstants.Step move(PathChain path, double speed, BooleanSupplier completionCondition) {
            return new BlueFarAutoConstants.Step(Action.MOVE, path, speed, completionCondition);
        }

        // SHOOT with custom condition
        public static BlueFarAutoConstants.Step shoot(BooleanSupplier completionCondition) {
            return new BlueFarAutoConstants.Step(Action.SHOOT, completionCondition);
        }

        // IDLE/WAIT with condition
        public static BlueFarAutoConstants.Step waitFor(BooleanSupplier condition) {
            return new BlueFarAutoConstants.Step(Action.IDLE, condition);
        }
    }

    public BlueFarAutoConstants(Follower follower, Robot robot, ElapsedTime timer) {
        List<PathChain> paths = buildPaths(follower);

        // PRELOAD + LINE 3
        steps.add(BlueFarAutoConstants.StepBuilder.waitFor(() -> timer.milliseconds() >= 100));
        steps.add(BlueFarAutoConstants.StepBuilder.waitFor(() -> !robot.shooter.isBusy() && !robot.turret.isBusy()));
        steps.add(BlueFarAutoConstants.StepBuilder.shoot(() -> !robot.blocker.isBusy()));
        steps.add(BlueFarAutoConstants.StepBuilder.move(paths.get(0), 1, () -> !follower.isBusy()));
        steps.add(BlueFarAutoConstants.StepBuilder.move(paths.get(1), 1, () -> !follower.isBusy()));
        steps.add(BlueFarAutoConstants.StepBuilder.waitFor(() -> timer.milliseconds() >= 100));
        steps.add(BlueFarAutoConstants.StepBuilder.waitFor(() -> !robot.shooter.isBusy() && !robot.turret.isBusy()));
        steps.add(BlueFarAutoConstants.StepBuilder.shoot(() -> !robot.blocker.isBusy()));

        ///HZP FIRST
        steps.add(BlueFarAutoConstants.StepBuilder.move(paths.get(2), 1, () -> !follower.isBusy() || timer.seconds() >= 4));
        steps.add(BlueFarAutoConstants.StepBuilder.move(paths.get(3), 0.6, () -> !follower.isBusy()));
        steps.add(BlueFarAutoConstants.StepBuilder.move(paths.get(4), 0.7, () -> !follower.isBusy()));
        steps.add(BlueFarAutoConstants.StepBuilder.waitFor(() -> timer.milliseconds() >= 100));
        steps.add(BlueFarAutoConstants.StepBuilder.waitFor(() -> !robot.shooter.isBusy() && !robot.turret.isBusy()));
        steps.add(BlueFarAutoConstants.StepBuilder.shoot(() -> !robot.blocker.isBusy()));

        steps.add(BlueFarAutoConstants.StepBuilder.move(paths.get(5), 0.8, () -> !follower.isBusy()));
        steps.add(BlueFarAutoConstants.StepBuilder.move(paths.get(6), 0.7, () -> !follower.isBusy()));
        steps.add(BlueFarAutoConstants.StepBuilder.waitFor(() -> timer.milliseconds() >= 100));
        steps.add(BlueFarAutoConstants.StepBuilder.waitFor(() -> !robot.shooter.isBusy() && !robot.turret.isBusy()));
        steps.add(BlueFarAutoConstants.StepBuilder.shoot(() -> !robot.blocker.isBusy()));

        steps.add(BlueFarAutoConstants.StepBuilder.move(paths.get(5), 0.8, () -> !follower.isBusy()));
        steps.add(BlueFarAutoConstants.StepBuilder.move(paths.get(6), 0.7, () -> !follower.isBusy()));
        steps.add(BlueFarAutoConstants.StepBuilder.waitFor(() -> timer.milliseconds() >= 100));
        steps.add(BlueFarAutoConstants.StepBuilder.waitFor(() -> !robot.shooter.isBusy() && !robot.turret.isBusy()));
        steps.add(BlueFarAutoConstants.StepBuilder.shoot(() -> !robot.blocker.isBusy()));

        steps.add(BlueFarAutoConstants.StepBuilder.move(paths.get(5), 0.8, () -> !follower.isBusy()));
        steps.add(BlueFarAutoConstants.StepBuilder.move(paths.get(6), 0.7, () -> !follower.isBusy()));
        steps.add(BlueFarAutoConstants.StepBuilder.waitFor(() -> timer.milliseconds() >= 100));
        steps.add(BlueFarAutoConstants.StepBuilder.waitFor(() -> !robot.shooter.isBusy() && !robot.turret.isBusy()));
        steps.add(BlueFarAutoConstants.StepBuilder.shoot(() -> !robot.blocker.isBusy()));

        //LEAVE
        steps.add(BlueFarAutoConstants.StepBuilder.move(paths.get(7), 1, () -> !follower.isBusy()));
    }

    private List<PathChain> buildPaths(Follower follower) {
        List<PathChain> paths = new ArrayList<>();

        // Path 1
        paths.add(follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(56.000, 8.000),
                        new Pose(54.304, 43.641),
                        new Pose(39.602, 33.779),
                        new Pose(13.801, 35.538)
                ))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build());

        // Path 2
        paths.add(follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(13.801, 35.538),
                        new Pose(51.286, 12.013)
                ))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build());

        // Path 3
        paths.add(follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(51.286, 12.013),
                        new Pose(10.641, 33.179)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(249))
                .build());

        // Path 4
        paths.add(follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(10.641, 33.179),
                        new Pose(8.779, 10.011)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(249), Math.toRadians(270))
                .build());

        // Path 5
        paths.add(follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(8.779, 10.011),
                        new Pose(51.286, 12.013)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(270), Math.toRadians(180))
                .build());

        // Path 6
        paths.add(follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(51.286, 12.013),
                        new Pose(10.319, 8.625)
                ))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build());

        // Path 7
        paths.add(follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(10.319, 8.625),
                        new Pose(51.286, 12.013)
                ))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build());

        // Path 8
        paths.add(follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(51.286, 12.013),
                        new Pose(35.372, 10.288)
                ))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build());

        return paths;
    }

    public BlueFarAutoConstants.Step getCurrentStep() {
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
