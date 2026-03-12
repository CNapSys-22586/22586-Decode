package org.firstinspires.ftc.teamcode.cnapsys.subsystems.Intake;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.cnapsys.core.interfaces.Subsystem;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.SubsystemData;

public class Intake implements Subsystem {
    private final DcMotorEx motor;

    private boolean enabled = false;

    public Intake(DcMotorEx motor) {
        this.motor = motor;
        this.motor.setDirection(DcMotorSimple.Direction.REVERSE);
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

    public void reverse() {
        motor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void forward() {
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void update(double deltaTime, TelemetryManager tm, SubsystemData data) {
        if (enabled) motor.setPower(IntakeConfig.power);
        else motor.setPower(0);
    }
}
