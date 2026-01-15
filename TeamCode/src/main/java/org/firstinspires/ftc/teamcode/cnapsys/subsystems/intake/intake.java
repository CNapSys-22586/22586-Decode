package org.firstinspires.ftc.teamcode.cnapsys.subsystems.intake;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.cnapsys.core.PIDF;
import org.firstinspires.ftc.teamcode.cnapsys.core.subsystem;

public class intake implements subsystem {
    private DcMotor motor;
    private int currentRPM;
    private int targetRPM;
    final double CPR = 580.4;
    private int lastPosition;
    private PIDF pidf;

    public intake(DcMotor motor) {
        this.motor = motor;
        this.motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lastPosition = motor.getCurrentPosition();
        pidf = new PIDF(intakeConfig.kP, intakeConfig.kI, intakeConfig.kD, intakeConfig.kF);
    }

    public boolean isBusy() {
        int diff = Math.abs(targetRPM - currentRPM);
        return diff <= intakeConfig.threshold;
    }

    public void update(long deltaTime) {
        targetRPM = intakeConfig.RPM;
        pidf.setParams(intakeConfig.kP, intakeConfig.kI, intakeConfig.kD, intakeConfig.kF);

        double deltaTicks = motor.getCurrentPosition() - lastPosition;
        lastPosition = motor.getCurrentPosition();
        double ticksPerSecond = deltaTicks / deltaTime;
        currentRPM = (int)((ticksPerSecond * 60) / 580.4);

        int error = targetRPM - currentRPM;
        double power = pidf.update((int)error);

        motor.setPower(power);
    }
}
