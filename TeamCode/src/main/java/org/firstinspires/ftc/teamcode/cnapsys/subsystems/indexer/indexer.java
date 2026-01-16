package org.firstinspires.ftc.teamcode.cnapsys.subsystems.indexer;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.cnapsys.core.subsystem;
import org.firstinspires.ftc.teamcode.cnapsys.subsystems.pusher.pusherConfig;

public class indexer implements subsystem {
    private Servo servo;
    private AnalogInput servoFeedback;

    private int rotation;
    private int[] slots;

    public indexer(Servo servo, AnalogInput servoFeedback) {
        this.servo = servo;
        this.servoFeedback = servoFeedback;
        slots = new int[]{0, 0, 0};
        rotation = 0;
    }

    double getServoPhysicalPos() {
        double voltage = servoFeedback.getVoltage();
        // Convert voltage to a 0.0 - 1.0 position range
        // (You'll need to calibrate the 0.2 and 3.1 values for your specific servo)
        return Range.scale(voltage, indexerConfig.voltageRangeMin, indexerConfig.voltageRangeMax, 0.0, 1.0);
    }

    @Override
    public boolean isBusy() {
        return false;
    }

    @Override
    public void update(long deltaTime) {

    }
}
