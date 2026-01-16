package org.firstinspires.ftc.teamcode.cnapsys.subsystems.indexer;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.cnapsys.core.subsystem;
import org.jetbrains.annotations.ApiStatus;

enum Colors {
    NONE,
    GREEN,
    PURPLE
}

public class indexer implements subsystem {
    private Servo servo;
    private AnalogInput servoFeedback;
    private ColorSensor colorSensor;
    // slot 0 is at 0 degrees, 1 is at 120 degrees, 2 is at 240
    private Colors[] slots;


    public indexer(Servo servo, AnalogInput servoFeedback, ColorSensor colorSensor) {
        this.servo = servo;
        this.servoFeedback = servoFeedback;
        this.colorSensor = colorSensor;
        slots = new Colors[]{Colors.NONE, Colors.NONE, Colors.NONE};
    }

    private double getServoPhysicalPos() {
        double voltage = servoFeedback.getVoltage();
        // Convert voltage to a 0.0 - 1.0 position range
        // (You'll need to calibrate the 0.2 and 3.1 values for your specific servo)
        return Range.scale(voltage, indexerConfig.voltageRangeMin, indexerConfig.voltageRangeMax, 0.0, 1.0);
    }

    public int getNumberOfFilledSlots() {
        int nr = 0;
        for (Colors i : slots) {
            if (i != Colors.NONE) nr++;
        }
        return nr;
    }

    public void selectNone() {
        double rotation = getServoPhysicalPos() * 360.0;

        double closest = 360.0;
        int closestIndex = -1;
        for (int i = 0; i < 3; ++i) {
            if (slots[i] == Colors.NONE) {
                double calculation =  Math.abs(rotation - indexerConfig.intakeRotations[i]);
                if (closest < calculation) {
                    closest = calculation;
                    closestIndex = i;
                }
            }
        }

        if (closestIndex != -1) {
            servo.setPosition(indexerConfig.intakeRotations[closestIndex] / 360.0);
        }
    }

    public void selectPurple() {
        double rotation = getServoPhysicalPos() * 360.0;

        double closest = 360.0;
        int closestIndex = -1;
        for (int i = 0; i < 3; ++i) {
            if (slots[i] == Colors.PURPLE) {
                double calculation =  Math.abs(rotation - indexerConfig.outakeRotations[i]);
                if (closest < calculation) {
                    closest = calculation;
                    closestIndex = i;
                }
            }
        }

        if (closestIndex != -1) {
            servo.setPosition(indexerConfig.outakeRotations[closestIndex] / 360.0);
        }
    }

    public void selectGreen() {
        double rotation = getServoPhysicalPos() * 360.0;

        double closest = 360.0;
        int closestIndex = -1;
        for (int i = 0; i < 3; ++i) {
            if (slots[i] == Colors.GREEN) {
                double calculation =  Math.abs(rotation - indexerConfig.outakeRotations[i]);
                if (closest < calculation) {
                    closest = calculation;
                    closestIndex = i;
                }
            }
        }

        if (closestIndex != -1) {
            servo.setPosition(indexerConfig.outakeRotations[closestIndex] / 360.0);
        }
    }

    public boolean isFull() {
        boolean full = true;
        for (Colors i : slots) {
            if (i == Colors.NONE) full = false;
        }
        return full;
    }

    @Override
    public boolean isBusy() {
        return false;
    }

    @Override
    public void update(long deltaTime) {

    }
}
