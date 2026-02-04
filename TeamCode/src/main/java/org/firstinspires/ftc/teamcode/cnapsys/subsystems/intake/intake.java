package org.firstinspires.ftc.teamcode.cnapsys.subsystems.intake;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.cnapsys.core.interfaces.subsystem;

public class intake implements subsystem {
    private final DcMotorEx motor;

    private boolean enabled = false;

    public intake(DcMotorEx motor) {
        this.motor = motor;
        this.motor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public boolean isBusy() {
        return enabled;
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public double getCurrentTPS() {
        return motor.getVelocity();
    }

    public void reverse() {
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void forward() {
        motor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    @Override
    public void update(double deltaTime, TelemetryManager tm) {
        if (enabled) motor.setPower(intakeConfig.power);
        else motor.setPower(0);
    }
}
