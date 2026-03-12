package org.firstinspires.ftc.teamcode.cnapsys.teleOps.auto;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.cnapsys.core.Robot;
import org.firstinspires.ftc.teamcode.cnapsys.core.RobotConfig;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.Alliance;
import org.firstinspires.ftc.teamcode.cnapsys.teleOps.auto.constants.RedFarAutoConstants;

@Autonomous(name="Auto Far RED")
public class RedFarAuto extends OpMode {

    private Robot robot;
    private final Alliance alliance = Alliance.RED;
    private RedFarAutoConstants constants;
    private RedFarAutoConstants.Step currentStep;
    private final ElapsedTime stepTimer = new ElapsedTime();
    private boolean stepExecuted = false;

    @Override
    public void init() {
        RobotConfig.ROBOT_POSE = RedFarAutoConstants.START_POS;
        robot = new Robot(hardwareMap, alliance, true);
        robot.intake.enable();
        constants = new RedFarAutoConstants(robot.follower, robot, stepTimer);
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

    private void executeStep(RedFarAutoConstants.Step step) {
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
