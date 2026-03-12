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

public class RedFarAutoConstants {
    public static final Pose START_POS = new Pose(88.000, 8.000, Math.toRadians(0));

    private final List<RedFarAutoConstants.Step> steps = new ArrayList<>();
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
        public static RedFarAutoConstants.Step move(PathChain path, double speed, BooleanSupplier completionCondition) {
            return new RedFarAutoConstants.Step(Action.MOVE, path, speed, completionCondition);
        }

        // SHOOT with custom condition
        public static RedFarAutoConstants.Step shoot(BooleanSupplier completionCondition) {
            return new RedFarAutoConstants.Step(Action.SHOOT, completionCondition);
        }

        // IDLE/WAIT with condition
        public static RedFarAutoConstants.Step waitFor(BooleanSupplier condition) {
            return new RedFarAutoConstants.Step(Action.IDLE, condition);
        }
    }

    public RedFarAutoConstants(Follower follower, Robot robot, ElapsedTime timer) {
        List<PathChain> paths = buildPaths(follower);

        // PRELOAD + LINE 3
        steps.add(RedFarAutoConstants.StepBuilder.waitFor(() -> timer.milliseconds() >= 100));
        steps.add(RedFarAutoConstants.StepBuilder.waitFor(() -> !robot.shooter.isBusy() && !robot.turret.isBusy()));
        steps.add(RedFarAutoConstants.StepBuilder.shoot(() -> !robot.blocker.isBusy()));
        steps.add(RedFarAutoConstants.StepBuilder.move(paths.get(0), 1, () -> !follower.isBusy()));
        steps.add(RedFarAutoConstants.StepBuilder.move(paths.get(1), 1, () -> !follower.isBusy()));
        steps.add(RedFarAutoConstants.StepBuilder.waitFor(() -> !robot.shooter.isBusy() && !robot.turret.isBusy()));
        steps.add(RedFarAutoConstants.StepBuilder.shoot(() -> !robot.blocker.isBusy()));

        //HZP FIRST
        steps.add(RedFarAutoConstants.StepBuilder.move(paths.get(2), 1, () -> !follower.isBusy()));
        steps.add(RedFarAutoConstants.StepBuilder.move(paths.get(3), 1, () -> !follower.isBusy()));
        steps.add(RedFarAutoConstants.StepBuilder.move(paths.get(4), 1, () -> !follower.isBusy()));
        steps.add(RedFarAutoConstants.StepBuilder.waitFor(() -> !robot.shooter.isBusy() && !robot.turret.isBusy()));
        steps.add(RedFarAutoConstants.StepBuilder.shoot(() -> !robot.blocker.isBusy()));

        steps.add(RedFarAutoConstants.StepBuilder.move(paths.get(5), 1, () -> !follower.isBusy()));
        steps.add(RedFarAutoConstants.StepBuilder.move(paths.get(6), 1, () -> !follower.isBusy()));
        steps.add(RedFarAutoConstants.StepBuilder.waitFor(() -> !robot.shooter.isBusy() && !robot.turret.isBusy()));
        steps.add(RedFarAutoConstants.StepBuilder.shoot(() -> !robot.blocker.isBusy()));

        steps.add(RedFarAutoConstants.StepBuilder.move(paths.get(5), 1, () -> !follower.isBusy()));
        steps.add(RedFarAutoConstants.StepBuilder.move(paths.get(6), 1, () -> !follower.isBusy()));
        steps.add(RedFarAutoConstants.StepBuilder.waitFor(() -> !robot.shooter.isBusy() && !robot.turret.isBusy()));
        steps.add(RedFarAutoConstants.StepBuilder.shoot(() -> !robot.blocker.isBusy()));

        //LEAVE
        steps.add(RedFarAutoConstants.StepBuilder.move(paths.get(7), 1, () -> !follower.isBusy()));
    }

    private List<PathChain> buildPaths(Follower follower) {
        List<PathChain> paths = new ArrayList<>();

        // Path 1
        paths.add(follower.pathBuilder()
                .addPath(new BezierCurve(
                        new Pose(88.000, 8.000),
                        new Pose(89.696, 43.641),
                        new Pose(104.398, 33.779),
                        new Pose(130.199, 35.538)
                ))
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build());

        // Path 2
        paths.add(follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(130.199, 35.538),
                        new Pose(92.714, 12.013)
                ))
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build());

        // Path 3
        paths.add(follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(92.714, 12.013),
                        new Pose(133.359, 33.179)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-69))
                .build());

        // Path 4
        paths.add(follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(133.359, 33.179),
                        new Pose(135.221, 10.011)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(-69), Math.toRadians(-90))
                .build());

        // Path 5 (was Path6 in original - Path5 missing)
        paths.add(follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(135.221, 10.011),
                        new Pose(92.714, 12.013)
                ))
                .setLinearHeadingInterpolation(Math.toRadians(-90), Math.toRadians(0))
                .build());

        // Path 6 (was Path7 in original)
        paths.add(follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(92.714, 12.013),
                        new Pose(133.681, 8.625)
                ))
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build());

        // Path 7 (was Path8 in original)
        paths.add(follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(133.681, 8.625),
                        new Pose(92.714, 12.013)
                ))
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build());

        // Path 8 (was Path9 in original)
        paths.add(follower.pathBuilder()
                .addPath(new BezierLine(
                        new Pose(92.714, 12.013),
                        new Pose(108.628, 10.288)
                ))
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build());

        return paths;
    }

    public RedFarAutoConstants.Step getCurrentStep() {
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
