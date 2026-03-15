package org.firstinspires.ftc.teamcode.cnapsys.teleOps.teleOps;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.cnapsys.core.RobotConfig;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.Alliance;
import org.firstinspires.ftc.teamcode.cnapsys.core.Robot;
import org.firstinspires.ftc.teamcode.cnapsys.subsystems.Turret.TurretConfig;


@TeleOp(name="TeleOpBLUE")
public class teleOpBLUE extends OpMode {
    private Robot robot;
    private final Alliance alliance = Alliance.BLUE;
    private boolean isIntakeActive = true;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, alliance, false);
        robot.follower.startTeleopDrive();
        RobotConfig.COMPENSATE_FOR_VELOCITY = true;
    }

    public void resetIMU() throws InterruptedException {
        robot.follower.poseTracker.resetIMU();
        robot.follower.setPose(RobotConfig.PARK_POSE.mirror());
    }

    @Override
    public void init_loop() {
        if (gamepad1.circleWasPressed()) {
            robot.turret.reset();
            robot.follower.setPose(RobotConfig.PARK_POSE.mirror());
        }
    }

    @Override
    public void loop() {
        // INTAKE TOGGLE
        if (gamepad1.rightBumperWasPressed()) isIntakeActive = !isIntakeActive;
        if (isIntakeActive) robot.intake.enable();
        else robot.intake.disable();
        //SHOOT LOGIC
        if (gamepad1.triangleWasPressed() && !robot.shooter.isBusy()) robot.blocker.activate();
        //INTAKE REVERSE
        if (gamepad1.dpadDownWasPressed()) robot.intake.reverse();
        else if (gamepad1.dpadDownWasReleased()) robot.intake.forward();
        //POWER LIMITER
        if (gamepad1.right_trigger > 0.1f) robot.follower.setMaxPower(RobotConfig.SLOW_FOLLOWER_SPEED);
        else robot.follower.setMaxPower(RobotConfig.MAX_FOLLOWER_SPEED);
        //DEBUG FUNCTIONS
        if (gamepad1.left_bumper) {
            if (gamepad1.dpadLeftWasPressed()) TurretConfig.ROTATION_OFFSET += 3;
            else if (gamepad1.dpadRightWasPressed()) TurretConfig.ROTATION_OFFSET -= 3;
            else if (gamepad1.dpadUpWasPressed()) {
                if (robot.turret.isEnabled()) robot.turret.disable();
                else robot.turret.enable();
            }
            else if (gamepad1.squareWasPressed() && gamepad1.circleWasPressed()) {
                try {
                    resetIMU();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        robot.follower.setTeleOpDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
        robot.update();
        robot.tm.update(telemetry);
    }

    @Override
    public void stop() {
        RobotConfig.ROBOT_POSE = robot.follower.getPose();
    }
}
