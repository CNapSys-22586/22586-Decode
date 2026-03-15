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

public class BlueNearConstants {
    public static final Pose START_POS = new Pose(29.260, 128.127, Math.toRadians(180));

    private final List<BlueNearConstants.Step> steps = new ArrayList<>();
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
        public static BlueNearConstants.Step move(PathChain path, double speed, BooleanSupplier completionCondition) {
            return new BlueNearConstants.Step(Action.MOVE, path, speed, completionCondition);
        }

        // SHOOT with custom condition
        public static BlueNearConstants.Step shoot(BooleanSupplier completionCondition) {
            return new BlueNearConstants.Step(Action.SHOOT, completionCondition);
        }

        // IDLE/WAIT with condition
        public static BlueNearConstants.Step waitFor(BooleanSupplier condition) {
            return new BlueNearConstants.Step(Action.IDLE, condition);
        }
    }

    public BlueNearConstants(Follower follower, Robot robot, ElapsedTime timer) {
        List<PathChain> paths = buildPaths(follower);

        steps.add(BlueNearConstants.StepBuilder.move(paths.get(0), 1, () -> timer.milliseconds() > 100));
        steps.add(BlueNearConstants.StepBuilder.waitFor(() -> !robot.shooter.isBusy() && !robot.turret.isBusy()));
        steps.add(BlueNearConstants.StepBuilder.shoot(() -> !robot.blocker.isBusy()));
        steps.add(BlueNearConstants.StepBuilder.waitFor(() -> !follower.isBusy()));
        steps.add(BlueNearConstants.StepBuilder.move(paths.get(1), 1, () -> !follower.isBusy()));
        steps.add(BlueNearConstants.StepBuilder.move(paths.get(2), 1, () -> !follower.isBusy()));
        steps.add(BlueNearConstants.StepBuilder.waitFor(() -> !robot.shooter.isBusy() && !robot.turret.isBusy()));
        steps.add(BlueNearConstants.StepBuilder.shoot(() -> !robot.blocker.isBusy()));

        steps.add(BlueNearConstants.StepBuilder.move(paths.get(3), 0.63, () -> !follower.isBusy()));
        steps.add(BlueNearConstants.StepBuilder.waitFor(() -> timer.milliseconds() >= 700));
        steps.add(BlueNearConstants.StepBuilder.move(paths.get(4), 1, () -> !follower.isBusy()));
        steps.add(BlueNearConstants.StepBuilder.move(paths.get(5), 1, () -> !follower.isBusy()));
        steps.add(BlueNearConstants.StepBuilder.waitFor(() -> !robot.shooter.isBusy() && !robot.turret.isBusy()));
        steps.add(BlueNearConstants.StepBuilder.shoot(() -> !robot.blocker.isBusy()));

        steps.add(BlueNearConstants.StepBuilder.move(paths.get(3), 0.63, () -> !follower.isBusy()));
        steps.add(BlueNearConstants.StepBuilder.waitFor(() -> timer.milliseconds() >= 700));
        steps.add(BlueNearConstants.StepBuilder.move(paths.get(4), 1, () -> !follower.isBusy()));
        steps.add(BlueNearConstants.StepBuilder.move(paths.get(5), 1, () -> !follower.isBusy()));
        steps.add(BlueNearConstants.StepBuilder.waitFor(() -> !robot.shooter.isBusy() && !robot.turret.isBusy()));
        steps.add(BlueNearConstants.StepBuilder.shoot(() -> !robot.blocker.isBusy()));

        steps.add(BlueNearConstants.StepBuilder.move(paths.get(3), 0.63, () -> !follower.isBusy()));
        steps.add(BlueNearConstants.StepBuilder.waitFor(() -> timer.milliseconds() >= 700));
        steps.add(BlueNearConstants.StepBuilder.move(paths.get(4), 1, () -> !follower.isBusy()));
        steps.add(BlueNearConstants.StepBuilder.move(paths.get(5), 1, () -> !follower.isBusy()));
        steps.add(BlueNearConstants.StepBuilder.waitFor(() -> !robot.shooter.isBusy() && !robot.turret.isBusy()));
        steps.add(BlueNearConstants.StepBuilder.shoot(() -> !robot.blocker.isBusy()));

        steps.add(BlueNearConstants.StepBuilder.move(paths.get(3), 0.63, () -> !follower.isBusy()));
        steps.add(BlueNearConstants.StepBuilder.waitFor(() -> timer.milliseconds() >= 700));
        steps.add(BlueNearConstants.StepBuilder.move(paths.get(4), 1, () -> !follower.isBusy()));
        steps.add(BlueNearConstants.StepBuilder.move(paths.get(5), 1, () -> !follower.isBusy()));
        steps.add(BlueNearConstants.StepBuilder.waitFor(() -> !robot.shooter.isBusy() && !robot.turret.isBusy()));
        steps.add(BlueNearConstants.StepBuilder.shoot(() -> !robot.blocker.isBusy()));

        steps.add(BlueNearConstants.StepBuilder.move(paths.get(6), 1, () -> !follower.isBusy()));
        steps.add(BlueNearConstants.StepBuilder.move(paths.get(7), 1, () -> !follower.isBusy()));
        steps.add(BlueNearConstants.StepBuilder.waitFor(() -> !robot.shooter.isBusy() && !robot.turret.isBusy()));
        steps.add(BlueNearConstants.StepBuilder.shoot(() -> !robot.blocker.isBusy()));
    }

    private List<PathChain> buildPaths(Follower follower) {
        List<PathChain> paths = new ArrayList<>();

        // Path 1
        paths.add(follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(29.260, 128.127),
                        new Pose(59.970, 77.737)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))
                .build());

        // Path 2
        paths.add(follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(59.970, 77.737),
                        new Pose(49.120, 57.249),
                        new Pose(14.851, 59.403)
                ))
                .setTangentHeadingInterpolation()
                .build());

        // Path 3
        paths.add(follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(14.851, 59.403),
                        new Pose(49.076, 57.326),
                        new Pose(59.970, 77.737)
                ))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build());

        // Path 4
        paths.add(follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(59.970, 77.737),
                        new Pose(11.356, 62.030)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(233), Math.toRadians(152))
                .build());

        // Path 5
        paths.add(follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(11.356, 62.030),
                        new Pose(12.560, 54.743)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(152), Math.toRadians(180))
                .build());

        // Path 6
        paths.add(follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(12.560, 54.743),
                        new Pose(38.084, 58.364),
                        new Pose(59.970, 77.737)
                ))
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build());

        // Path 7
        paths.add(follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(59.970, 77.737),
                        new Pose(52.056, 85.476),
                        new Pose(17.003, 83.604)
                ))
                .setTangentHeadingInterpolation()
                .build());

        // Path 8
        paths.add(follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(17.003, 83.604),
                        new Pose(50.732, 107.281)
                ))
                .setTangentHeadingInterpolation()
                .setReversed()
                .build());

        return paths;
    }

    public BlueNearConstants.Step getCurrentStep() {
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
