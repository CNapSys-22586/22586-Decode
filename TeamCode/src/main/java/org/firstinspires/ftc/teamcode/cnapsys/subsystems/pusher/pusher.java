package org.firstinspires.ftc.teamcode.cnapsys.subsystems.pusher;

import android.os.MessageQueue;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.cnapsys.core.subsystem;

enum PusherState {
    IDLE,
    RETURNING,
    PUSHING
}
public class pusher implements subsystem {
    private Servo servo;
    private AnalogInput servoFeedback;
    private PusherState state;
    public pusher(Servo servo, AnalogInput servoFeedback) {
        this.servo = servo;
        this.servoFeedback = servoFeedback;
        state = PusherState.IDLE;
    }
    public boolean isBusy() {
        return state != PusherState.IDLE;
    }

    public void update(long deltaTime) {
        double voltage = servoFeedback.getVoltage();
        // Convert voltage to a 0.0 - 1.0 position range
        // (You'll need to calibrate the 0.2 and 3.1 values for your specific servo)
        double physicalPos = Range.scale(voltage, 0.2, 3.1, 0.0, 1.0);
        switch (state) {
            case IDLE:
                break;
            case PUSHING:
                break;
            case RETURNING:
                break;
        }
    }

    public void activate() {
        this.servo.setPosition(pusherConfig.engagedPosition);
        state = PusherState.PUSHING;
    }
}
