package org.firstinspires.ftc.teamcode.cnapsys.teleOps.auto;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.cnapsys.core.Robot;
import org.firstinspires.ftc.teamcode.cnapsys.core.RobotConfig;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.Alliance;
import org.firstinspires.ftc.teamcode.cnapsys.teleOps.auto.constants.RedTopAutoHomosapiensConstants;

@Autonomous(name="AutoHomoSapiens")
public class RedTopHomosapiens extends OpMode {

    private Robot robot;
    private final Alliance alliance = Alliance.RED;
    private RedTopAutoHomosapiensConstants constants;
    private RedTopAutoHomosapiensConstants.Step currentStep;
    private final ElapsedTime stepTimer = new ElapsedTime();
    private boolean stepExecuted = false;

    @Override
    public void init() {
        RobotConfig.ROBOT_POSE = RedTopAutoHomosapiensConstants.START_POS;
        robot = new Robot(hardwareMap, alliance, true);
        robot.intake.enable();
        constants = new RedTopAutoHomosapiensConstants(robot.follower, robot, stepTimer);
        RobotConfig.COMPENSATE_FOR_VELOCITY = true;
    }

    @Override
    public void start() {
        stepTimer.reset();
        currentStep = constants.getCurrentStep();

    }

    @Override
    public void stop() {
        RobotConfig.ROBOT_POSE = robot.follower.getPose();
    }

    @Override
    public void loop() {

        // Main autonomous logic - super simple now!
        if (constants.isComplete()) {
            // Autonomous complete, do nothing
            robot.tm.addData("STATUS", "COMPLETE");
        } else if (currentStep != null) {
            // Execute the step if not already executed
            if (!stepExecuted) {
                executeStep(currentStep);
                stepExecuted = true;
            }

            // Check if step is complete
            if (currentStep.isComplete()) {
                constants.advanceStep();
                currentStep = constants.getCurrentStep();
                stepExecuted = false;
                stepTimer.reset();
            }
        }

        // Telemetry
        if (currentStep != null) {
            robot.tm.addData("ACTION", currentStep.action);
            robot.tm.addData("SPEED", String.format("%.2f", currentStep.speed));
        }
        robot.tm.addData("STEP TIME", String.format("%.2f", stepTimer.seconds()));

        robot.update();
        robot.tm.update(telemetry);
    }

    private void executeStep(RedTopAutoHomosapiensConstants.Step step) {
        switch (step.action) {
            case MOVE:
                robot.follower.setMaxPower(step.speed);
                robot.follower.followPath(step.path, true);
                break;

            case SHOOT:
                robot.blocker.activate();
                break;

            case IDLE:
                // Nothing to execute, just wait for condition
                break;
        }
    }
}
