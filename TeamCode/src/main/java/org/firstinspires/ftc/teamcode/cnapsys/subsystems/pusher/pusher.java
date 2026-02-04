package org.firstinspires.ftc.teamcode.cnapsys.subsystems.pusher;


import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.cnapsys.core.interfaces.subsystem;

public class pusher implements subsystem {
    private final Servo servo;
    private final AnalogInput servoFeedback;
    private int state = -1;
    private double goToPos = pusherConfig.idlePosition, lastDelta, dt;
    public pusher(Servo servo, AnalogInput servoFeedback) {
        this.servo = servo;
        this.servoFeedback = servoFeedback;
        servo.setPosition(goToPos);
    }

    private double getServoPhysicalPos() {
        double voltage = Math.max(0, servoFeedback.getVoltage());
        return 1.0 - (voltage / pusherConfig.encVoltage);
    }

    @Override
    public boolean isBusy() {
        return (Math.abs(getServoPhysicalPos() - goToPos) > pusherConfig.threshold || dt - lastDelta < pusherConfig.delay);
    }

    public boolean isIdle() {
        return (state == -1) && !isBusy();
    }

    @Override
    public void update(double deltaTime, TelemetryManager tm) {
        dt = deltaTime;
        tm.addData("pusher estimated position: ", getServoPhysicalPos());
        tm.addData("pusher actual position: ", servo.getPosition());
        switch (state) {
            case -1:
                goToPos = pusherConfig.idlePosition;
                break;
            case 0:
                goToPos = pusherConfig.engagedPosition;
                if (!isBusy()) {
                    state = -1;
                    lastDelta = dt;
                }
                break;
        }
        servo.setPosition(goToPos);
    }

    public void activate() {
        lastDelta = dt;
        state = 0;
    }
}
