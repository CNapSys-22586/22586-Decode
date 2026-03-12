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
        robot.turret.reset(); //REMOVE THIS FOR ACTUAL GAME SINCE IT IS RAN DURING AUTO
        robot.follower.startTeleopDrive();
    }

    @Override
    public void loop() {
        // INTAKE TOGGLE
        if (gamepad1.rightBumperWasPressed()) isIntakeActive = !isIntakeActive;
        if (isIntakeActive) robot.intake.enable();
        else robot.intake.disable();
        //SHOOT LOGIC
        if (gamepad1.triangleWasPressed() && !robot.turret.isBusy() && !robot.shooter.isBusy()) robot.blocker.activate();
        else if (gamepad1.triangleWasPressed()) gamepad1.rumbleBlips(3);
        //INTAKE REVERSE WHILE PRESSED
        if (gamepad1.dpadLeftWasPressed()) robot.intake.reverse();
        else if (gamepad1.dpadLeftWasReleased()) robot.intake.forward();
        //POWER LIMITER
        if (gamepad1.right_trigger > 0.1f) robot.follower.setMaxPower(RobotConfig.SLOW_FOLLOWER_SPEED);
        else robot.follower.setMaxPower(RobotConfig.MAX_FOLLOWER_SPEED);

        robot.tm.update(telemetry);
        robot.follower.setTeleOpDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
        robot.update();
    }

    @Override
    public void stop() {
        //Robot.defaultPose = robot.follower.getPose();
    }
}
