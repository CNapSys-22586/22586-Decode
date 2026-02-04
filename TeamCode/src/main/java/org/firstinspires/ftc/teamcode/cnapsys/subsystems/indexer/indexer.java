package org.firstinspires.ftc.teamcode.cnapsys.subsystems.indexer;

import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.cnapsys.core.vars.Colors;
import org.firstinspires.ftc.teamcode.cnapsys.core.utils.colorSensor;
import org.firstinspires.ftc.teamcode.cnapsys.core.interfaces.subsystem;

public class indexer implements subsystem {
    private final Servo servo;
    private final AnalogInput servoFeedback;
    private final colorSensor colorSensor;
    private Colors[] slots = {Colors.UNKNOWN, Colors.UNKNOWN, Colors.UNKNOWN};
    private int selected, state = -1;
    private double gotoPos = indexerConfig.intakeRotations[0], lastDeltaTime;

    public indexer(Servo servo, AnalogInput servoFeedback, colorSensor colorSensor) {
        this.servo = servo;
        this.servoFeedback = servoFeedback;
        slots = new Colors[]{Colors.UNKNOWN, Colors.UNKNOWN, Colors.UNKNOWN};
        this.colorSensor = colorSensor;
    }

    public void selectNone() {
        for (int i = 0; i < 3; ++i) {
            if (slots[i] == Colors.UNKNOWN) {
                gotoPos = indexerConfig.intakeRotations[i];
                selected = i;
                break;
            }
        }
    }

    public boolean selectGreen() {
        for (int i = 0; i < 3; ++i) {
            if (slots[i] == Colors.GREEN) {
                gotoPos = indexerConfig.outakeRotations[i];
                selected = i;
                return true;
            }
        }
        return false;
    }

    public boolean selectPurple() {
        for (int i = 0; i < 3; ++i) {
            if (slots[i] == Colors.PURPLE) {
                gotoPos = indexerConfig.outakeRotations[i];
                selected = i;
                return true;
            }
        }
        return false;
    }

    public int getSelected() {
        return selected;
    }

    public void fillSlot(int slotId, Colors color) {
        slots[slotId] = color;
    }
    public void fillSlot(Colors color0, Colors color1, Colors color2) {
        slots = new Colors[] {color0, color1, color2};
    }
    public void emptySlot(int slotId) {
        slots[slotId] = Colors.UNKNOWN;
    }

    public boolean isFull() {
        for (Colors i : slots) {
            if (i == Colors.UNKNOWN) return false;
        }
        return true;
    }

    public boolean isEmpty() {
        for (Colors i : slots) {
            if (i != Colors.UNKNOWN) return false;
        }
        return true;
    }

    private double getServoPhysicalPos() {
        double voltage = servoFeedback.getVoltage();
        return (voltage / indexerConfig.AVG_VOLTAGE);
    }

    public void setAutomatic () {
        state = -1;
    }

    public void setManual () {
        state = 0;
    }

    @Override
    public boolean isBusy() {
        return (Math.abs(getServoPhysicalPos() - gotoPos) > indexerConfig.threshold);
    }

    @Override
    public void update(double deltaTime, TelemetryManager tm) {
        servo.setPosition(gotoPos);
        switch (state) {
            case -1:
                if (isFull()) gotoPos = indexerConfig.outakeRotations[0];
                else selectNone();
                state = 1;
                lastDeltaTime = deltaTime;
                break;
            case 0:
                break;
            case 1:
                if (deltaTime - lastDeltaTime >= indexerConfig.delay && !isBusy() && !colorSensor.isObjectDetected()) {
                    state = 2;
                }
                break;
            case 2:
                if (colorSensor.isObjectDetected() && !isFull()) {
                    fillSlot(selected, colorSensor.getColor());
                    state = -1;
                }
                break;

        }
        tm.addData("indexer estimated position: ", getServoPhysicalPos());
        tm.addData("indexer actual position: ", servo.getPosition());
        tm.addData("is indexer busy: ", isBusy());
        tm.addData("indexer state: ", state);
        tm.addData("is obj detected in indexer: ", colorSensor.isObjectDetected());
    }
}
