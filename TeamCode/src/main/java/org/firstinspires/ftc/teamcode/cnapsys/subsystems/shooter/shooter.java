package org.firstinspires.ftc.teamcode.cnapsys.subsystems.shooter;

import static org.firstinspires.ftc.teamcode.cnapsys.core.utils.MathUtils.linearInterpolation;
import static org.firstinspires.ftc.teamcode.cnapsys.subsystems.shooter.shooterConfig.table;

import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.cnapsys.core.utils.PIDF;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.ShooterPoint;
import org.firstinspires.ftc.teamcode.cnapsys.core.interfaces.subsystem;

public class shooter implements subsystem {

    private final DcMotorEx motorA;
    private final DcMotorEx motorB;
    private final Servo servo;
    private boolean enabled = false;
    private double shooterTargetTPS = 0;
    private double shooterAngle = 30;
    private final PIDF pidf = new PIDF(shooterConfig.kp, shooterConfig.ki, shooterConfig.kd, shooterConfig.kf);

    public shooter (DcMotorEx motorA, DcMotorEx motorB, Servo hoodServo) {
        this.motorA = motorA;
        this.motorB = motorB;
        this.motorA.setDirection(DcMotorSimple.Direction.REVERSE);
        this.motorB.setDirection(DcMotorSimple.Direction.REVERSE);
        this.motorA.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        this.motorB.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        this.servo = hoodServo;
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public void setShooterTarget(double distance) {
        ShooterPoint target = interpolate(distance);
        this.shooterTargetTPS = target.TPS;
        this.shooterAngle = target.hoodAngle;
    }

    @Override
    public boolean isBusy() {
        return (Math.abs(motorA.getVelocity() - shooterTargetTPS) > shooterConfig.threshold);
    }

    private static ShooterPoint interpolate(double distance) {

        if (table == null || table.length == 0) {
            throw new IllegalArgumentException("Shooter table is empty");
        }

        if (distance <= table[0].distanceFromTarget) {
            return table[0];
        }

        if (distance >= table[table.length - 1].distanceFromTarget) {
            return table[table.length - 1];
        }

        for (int i = 0; i < table.length - 1; i++) {
            ShooterPoint p1 = table[i];
            ShooterPoint p2 = table[i + 1];

            if (distance >= p1.distanceFromTarget && distance <= p2.distanceFromTarget) {
                double tps = linearInterpolation(
                        distance,
                        p1.distanceFromTarget, p2.distanceFromTarget,
                        p1.TPS, p2.TPS
                );

                double angle = linearInterpolation(
                        distance,
                        p1.distanceFromTarget, p2.distanceFromTarget,
                        p1.hoodAngle, p2.hoodAngle
                );

                return new ShooterPoint(distance, tps, angle);
            }
        }

        return table[table.length - 1];
    }

    @Override
    public void update(double deltaTime, TelemetryManager tm) {
        tm.addData("shooter target TPS: ", shooterTargetTPS);
        tm.addData("shooter actual TPS: ", motorA.getVelocity());
        tm.addData("shooter is busy: ", isBusy());
        tm.addData("shooter hood angle: ", shooterAngle);
        tm.addData("shooter hood servo pos: ", (shooterAngle * shooterConfig.GEAR_RATIO) / 360);
        pidf.setParams(shooterConfig.kp, shooterConfig.ki, shooterConfig.kd, shooterConfig.kf);
        servo.setPosition((shooterAngle * shooterConfig.GEAR_RATIO) / 360);
        double error = shooterTargetTPS - motorA.getVelocity();
        tm.addData("shooter error: ", error);
        double power = Math.max(0, pidf.update(error));
        tm.addData("shooter motors power: ", power);
        if (enabled) {
            motorA.setPower(power);
            motorB.setPower(power);
        }
        else {
            motorA.setPower(0);
            motorB.setPower(0);
        }
    }
}
