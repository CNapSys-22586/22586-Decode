package org.firstinspires.ftc.teamcode.cnapsys.subsystems.pusher;

import android.os.MessageQueue;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.cnapsys.core.subsystem;

import java.sql.Time;

enum PusherState {
    IDLE,
    RETURNING,
    PUSHING
}
public class pusher implements subsystem {
    private Servo servo;
    private AnalogInput servoFeedback;
    private PusherState state;
    private long lastStateChange; // timer add deltatime to it and reset it to 0
    public pusher(Servo servo, AnalogInput servoFeedback) {
        this.servo = servo;
        this.servoFeedback = servoFeedback;
        state = PusherState.IDLE;
        lastStateChange = 0;
    }
    public boolean isBusy() {
        //the pusher is busy if its not idle
        return state != PusherState.IDLE;
    }

    public void update(long deltaTime) {
        if (state == PusherState.IDLE) return;

        // no reason to add to the timer if not engaged or returning
        lastStateChange += deltaTime;

        double voltage = servoFeedback.getVoltage();
        // Convert voltage to a 0.0 - 1.0 position range
        // (You'll need to calibrate the 0.2 and 3.1 values for your specific servo)
        double physicalPos = Range.scale(voltage, pusherConfig.voltageRangeMin, pusherConfig.voltageRangeMax, 0.0, 1.0);

        switch (state) {
            case PUSHING: {
                double diff = Math.abs(pusherConfig.engagedPosition - physicalPos);
                //servo moved to the engaged position, move it back
                if (diff < pusherConfig.threshold) {
                    state = PusherState.RETURNING;
                    lastStateChange = 0;
                    this.servo.setPosition(pusherConfig.idlePosition);
                    break;
                }
                //if it didnt engage in time try to engage it again
                if (lastStateChange > pusherConfig.wait) {
                    lastStateChange = 0;
                    this.servo.setPosition(pusherConfig.engagedPosition);
                }
                break;
            }
            case RETURNING: {
                double diff = Math.abs(physicalPos - pusherConfig.idlePosition);
                //pusher returned so make it idle
                if (diff < pusherConfig.threshold) {
                    state = PusherState.IDLE;
                    lastStateChange = 0;
                    break;
                }
                //if it didnt return in the wait time, try to return it again
                if (lastStateChange > pusherConfig.wait) {
                    lastStateChange = 0;
                    this.servo.setPosition(pusherConfig.idlePosition);
                }
                break;
            }
        }
    }

    public void activate() {
        this.servo.setPosition(pusherConfig.engagedPosition);
        state = PusherState.PUSHING;
        lastStateChange = 0;
    }
}
