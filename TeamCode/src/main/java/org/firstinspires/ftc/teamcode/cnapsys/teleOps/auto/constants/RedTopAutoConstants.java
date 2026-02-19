package org.firstinspires.ftc.teamcode.cnapsys.teleOps.auto.constants;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.cnapsys.core.Robot;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.Action;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.FieldState;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.IdleConditions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

public class RedTopAutoConstants {
    public static final Pose START_POS = new Pose(120.685, 127.204, Math.toRadians(126));

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

        // For IDLE/WAIT actions (must provide condition)
        private Step(Action action, IdleConditions idleCondition, BooleanSupplier condition) {
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
            return new Step(Action.IDLE, null, condition);
        }

        // IDLE with enum condition helper
        public static Step waitFor(IdleConditions idleCondition, BooleanSupplier condition) {
            return new Step(Action.IDLE, idleCondition, condition);
        }
    }

    public RedTopAutoConstants(Follower follower, Robot robot, ElapsedTime timer) {
        List<PathChain> paths = buildPaths(follower);

        // Example autonomous sequence for a 9 artifact auto + leave
        steps.add(StepBuilder.move(paths.get(0), 1.0, () -> !follower.isBusy()));
        steps.add(StepBuilder.shoot(() -> !robot.isBusy()));

        steps.add(StepBuilder.move(paths.get(1), 0.5, () -> !follower.isBusy()));
        steps.add(StepBuilder.waitFor(() -> robot.indexer.isFull() || timer.seconds() >= 2));

        steps.add(StepBuilder.move(paths.get(2), 1.0, () -> !follower.isBusy()));
        steps.add(StepBuilder.shoot(() -> !robot.isBusy()));

        steps.add(StepBuilder.move(paths.get(3), 0.5, () -> !follower.isBusy()));
        steps.add(StepBuilder.waitFor(() -> robot.indexer.isFull() || timer.seconds() >= 2));

        steps.add(StepBuilder.move(paths.get(4), 1.0, () -> !follower.isBusy()));
        steps.add(StepBuilder.shoot(() -> !robot.isBusy()));

        steps.add(StepBuilder.move(paths.get(5), 0.8, () -> !follower.isBusy()));
    }

    private List<PathChain> buildPaths(Follower follower) {
        List<PathChain> paths = new ArrayList<>();

        // Path 1
        paths.add(follower.pathBuilder()
                .addPath(new BezierLine(
                        START_POS,
                        new Pose(103.435, 109.617)
                ))
                .setConstantHeadingInterpolation(Math.toRadians(126))
                .build());

        // Path 2
        paths.add(follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(103.435, 109.617),
                        new Pose(84.826, 81.181),
                        new Pose(122.901, 83.385)
                ))
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build());

        // Path 3
        paths.add(follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(122.901, 83.385),
                        new Pose(103.215, 109.526)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(48))
                .build());

        // Path 4
        paths.add(follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(103.215, 109.526),
                        new Pose(90.730, 66.781),
                        new Pose(81.024, 61.160),
                        new Pose(108.364, 58.694),
                        new Pose(129.369, 59.150)
                ))
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build());

        // Path 5
        paths.add(follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(129.369, 59.150),
                        new Pose(78.984, 41.512),
                        new Pose(93.075, 83.093),
                        new Pose(103.448, 109.504)
                ))
                .setConstantHeadingInterpolation(Math.toRadians(36))
                .build());

        // Path 6
        paths.add(follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(103.448, 109.504),
                        new Pose(122.227, 96.967)

                ))
                .setConstantHeadingInterpolation(Math.toRadians(0))
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