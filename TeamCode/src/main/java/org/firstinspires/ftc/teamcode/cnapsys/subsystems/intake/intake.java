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

    @Override
    public boolean isBusy() {
        int diff = Math.abs(targetRPM - currentRPM);
        return diff <= intakeConfig.threshold;
    }

    @Override
    public void update(long deltaTime) {
        //set these again so they can be configed in real time, maybe should be commented out for efficiency if we find the perfect values
        targetRPM = intakeConfig.RPM;
        pidf.setParams(intakeConfig.kP, intakeConfig.kI, intakeConfig.kD, intakeConfig.kF);

        //calculate current rpm based on the last position and current position, diving by deltatime
        double deltaTicks = motor.getCurrentPosition() - lastPosition;
        lastPosition = motor.getCurrentPosition();
        //deltatime is in ms so we need to multiply by 1000
        double ticksPerSecond = (deltaTicks / deltaTime) * 1000.0;
        //580.4 is the constant for the specific motor, maybe needs calibrating if it doesnt work
        currentRPM = (int)((ticksPerSecond * 60) / 580.4);

        int error = targetRPM - currentRPM;
        double power = pidf.update((int)error);

        // set the power based on the error between current and target rpm
        motor.setPower(power);
    }
}
