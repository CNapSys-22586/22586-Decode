package org.firstinspires.ftc.teamcode.cnapsys.teleOps.teleOps;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.Alliance;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.Colors;
import org.firstinspires.ftc.teamcode.cnapsys.core.Robot;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.FieldState;


@TeleOp(name="TeleOpBLUE")
public class teleOpBLUE extends OpMode {

    private Robot robot;
    private final Alliance alliance = Alliance.BLUE;
    private ElapsedTime timer;
    private Limelight3A limeLight;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, alliance, true);
        robot.intake.enable();
        robot.follower.startTeleopDrive();
        limeLight = hardwareMap.get(Limelight3A.class, "limelight");
        if (!FieldState.isInit()) limeLight.start();
    }

    @Override
    public void init_loop() {
        if (limeLight.isConnected()) robot.indicatorLight.setColor(Colors.GREEN);
        else robot.indicatorLight.setColor(Colors.RED);
    }

    @Override
    public void start() {
        timer = new ElapsedTime();
        robot.indicatorLight.turnOff();
    }

    @Override
    public void loop() {
        if (!FieldState.isInit() && limeLight.isRunning()) {
            LLResult result = limeLight.getLatestResult();
            if (result != null && result.isValid()) {
                int id = result.getFiducialResults().get(0).getFiducialId();
                FieldState.setPattern(id);
                limeLight.stop();
            }
        }
        if (gamepad1.rightBumperWasPressed()) robot.shootPurple();
        else if (gamepad1.leftBumperWasPressed()) robot.shootGreen();
        else if (gamepad1.triangleWasPressed()) robot.shootAll();
        if (gamepad1.dpadUpWasPressed()) robot.cancelShooting();
        if (gamepad1.dpadLeftWasPressed()) robot.intake.reverse();
        else if (gamepad1.dpadLeftWasReleased()) robot.intake.forward();
        if (gamepad1.left_trigger > 0.1f) robot.follower.setMaxPower(0.6);
        else robot.follower.setMaxPower(1);
        if (timer.seconds() / 60 >= 2) robot.intake.disable();
        robot.tm.update(telemetry);
        robot.follower.setTeleOpDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
        robot.update();
    }

    @Override
    public void stop() {
        //Robot.defaultPose = robot.follower.getPose();
    }
}
