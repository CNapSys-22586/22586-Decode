package org.firstinspires.ftc.teamcode.cnapsys.subsystems.Blocker;

import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.cnapsys.core.interfaces.Subsystem;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.SubsystemData;

public class Blocker implements Subsystem {

    private final Servo servo;
    private double lastDeltaTime;
    private enum blockerState {
        OPEN, OPENING, CLOSED
    }

    blockerState state = blockerState.CLOSED;

    public Blocker(Servo servo) {
        this.servo = servo;
        this.servo.setPosition(BlockerConfig.BLOCKER_SERVO_CLOSED_POS);
    }

    public void activate() {
        state = blockerState.OPENING;
    }

    @Override
    public boolean isBusy() {
        return state != blockerState.CLOSED;
    }

    @Override
    public void update(double deltaTime, TelemetryManager tm, SubsystemData data) {
        switch (state) {
            case CLOSED:
                servo.setPosition(BlockerConfig.BLOCKER_SERVO_CLOSED_POS);
                break;
            case OPENING:
                lastDeltaTime = deltaTime;
                state = blockerState.OPEN;
                break;
            case OPEN:
                servo.setPosition(BlockerConfig.BLOCKER_SERVO_OPEN_POS);
                if (deltaTime - lastDeltaTime >= BlockerConfig.DELAY) state = blockerState.CLOSED;
                break;
        }
    }
}
