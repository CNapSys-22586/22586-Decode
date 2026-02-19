package org.firstinspires.ftc.teamcode.cnapsys.teleOps.auto;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.cnapsys.core.Robot;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.Alliance;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.Colors;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.FieldState;
import org.firstinspires.ftc.teamcode.cnapsys.teleOps.auto.constants.RedTopAutoConstants;


@Autonomous(name="Red Auto Top IMPROVED 9 artefacts")
public class RedTopAuto extends OpMode {

    private Robot robot;
    private final Alliance alliance = Alliance.RED;
    private RedTopAutoConstants constants;
    private RedTopAutoConstants.Step currentStep;
    private final ElapsedTime stepTimer = new ElapsedTime();
    private Limelight3A limeLight;
    private boolean stepExecuted = false;

    @Override
    public void init() {
        Robot.defaultPose = RedTopAutoConstants.START_POS;
        robot = new Robot(hardwareMap, alliance, false);
        robot.intake.enable();
        limeLight = hardwareMap.get(Limelight3A.class, "limelight");
        limeLight.start();
        robot.indexer.fillSlot(Colors.PURPLE, Colors.PURPLE, Colors.GREEN);
        constants = new RedTopAutoConstants(robot.follower, robot, stepTimer);
    }

    @Override
    public void init_loop() {
        if (limeLight.isConnected()) robot.indicatorLight.setColor(Colors.GREEN);
        else robot.indicatorLight.setColor(Colors.RED);
    }

    @Override
    public void start() {
        stepTimer.reset();
        robot.indicatorLight.turnOff();
        currentStep = constants.getCurrentStep();
    }

    @Override
    public void stop() {
        Robot.defaultPose = robot.follower.getPose();
    }

    @Override
    public void loop() {
        // Intake control logic
        if (robot.indexer.isFull() && !robot.indexer.isBusy() && !robot.isBusy()) {
            robot.intake.reverse();
        } else {
            robot.intake.forward();
        }

        // Limelight pattern detection
        if (limeLight.isRunning() && stepTimer.seconds() >= 1) {
            LLResult result = limeLight.getLatestResult();
            if (result != null && result.isValid()) {
                int id = result.getFiducialResults().get(0).getFiducialId();
                FieldState.setPattern(id);
                limeLight.stop();
            }
        }

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

    private void executeStep(RedTopAutoConstants.Step step) {
        switch (step.action) {
            case MOVE:
                robot.follower.setMaxPower(step.speed);
                robot.follower.followPath(step.path);
                break;

            case SHOOT:
                robot.shootAll();
                break;

            case IDLE:
                // Nothing to execute, just wait for condition
                break;
        }
    }
}