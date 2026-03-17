package org.firstinspires.ftc.teamcode.cnapsys.subsystems.Shooter;

import static androidx.core.math.MathUtils.clamp;

import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.cnapsys.core.RobotConfig;
import org.firstinspires.ftc.teamcode.cnapsys.core.utils.PFF;
import org.firstinspires.ftc.teamcode.cnapsys.core.interfaces.Subsystem;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.ShooterPoint;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.SubsystemData;

public class Shooter implements Subsystem {

    private final DcMotorEx motorA;
    private final DcMotorEx motorB;
    private final Servo hoodServo;
    private double distance;
    private double targetTPS = 0;
    private boolean enabled = false, idle = true;
    private final ShooterInterpolator interpolator;
    private final PFF pff = new PFF(ShooterConfig.KP, ShooterConfig.KV, ShooterConfig.KS);

    public Shooter(DcMotorEx motorA, DcMotorEx motorB, Servo hoodServo) {
        this.motorA = motorA;
        this.motorB = motorB;
        this.motorA.setDirection(DcMotorSimple.Direction.FORWARD);
        this.motorB.setDirection(DcMotorSimple.Direction.REVERSE);
        this.motorA.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        this.motorB.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        this.hoodServo = hoodServo;
        setHoodPosition(ShooterConfig.HOOD_ANGLE_MIN);
        this.interpolator = new ShooterInterpolator(ShooterConfig.INTERPOLATION_POINTS);
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public void setIdle() {
        idle = true;
    }

    public void setEngaged() {
        idle = false;
    }

    @Override
    public boolean isBusy() {
        if (idle || !enabled) return false;
        return Math.abs(motorA.getVelocity() - targetTPS) > ShooterConfig.THRESHOLD;
    }

    private void setHoodPosition(double angle) {
        hoodServo.setPosition(angle * ShooterConfig.GEAR_RATIO / 255 - 1.180);
    }

    public void setHoodPos(ShooterPoint point) {
        double angle = clamp(point.maxHoodAngle - Math.abs(point.TPS - motorA.getVelocity() - 20) * ShooterConfig.HOOD_COMPENSATION_AMOUNT, ShooterConfig.HOOD_ANGLE_MIN, ShooterConfig.HOOD_ANGLE_MAX);
        setHoodPosition(angle);
    }

    public double getTOF() {
        double speed = motorA.getVelocity() * 2 * Math.PI * 0.072 * ShooterConfig.EFFICIENCY;
        return distance / speed;
    }

    @Override
    public void update(double deltaTime, TelemetryManager tm, SubsystemData data) {

        //GET TARGET DISTANCE
        if (RobotConfig.COMPENSATE_FOR_VELOCITY) distance = data.follower.getPose().distanceFrom(data.goalPoseAdjusted);
        else distance = data.follower.getPose().distanceFrom(data.goalPose);

        //GET INTERPOLATED DATA
        ShooterPoint target = interpolator.interpolateData(distance);
        targetTPS = target.TPS;

        if (idle) targetTPS = ShooterConfig.IDLE_TPS;

        double power = pff.update(targetTPS, motorA.getVelocity());

        //DEBUG TELEMETRY
        if (ShooterConfig.DEBUG_MODE) {
            tm.addData("Shooter/Enabled", enabled);
            tm.addData("Shooter/isIdle", idle);
            tm.addData("Shooter/Power", power);
            tm.addData("Shooter/Target TPS", targetTPS);
            tm.addData("Shooter/Error", targetTPS - motorA.getVelocity());
            tm.addData("Shooter/TOF", getTOF());
            tm.addData("Shooter/Current TPS", motorA.getVelocity());
            tm.addData("Shooter/HoodAngle", target.minHoodAngle);
            interpolator.setShooterPoints(ShooterConfig.INTERPOLATION_POINTS);
            pff.setParams(ShooterConfig.KP, ShooterConfig.KV, ShooterConfig.KS);
        }

        if (enabled) {
            motorA.setPower(power);
            motorB.setPower(power);
            setHoodPos(target);
        }
        else {
            motorA.setPower(0);
            motorB.setPower(0);
        }
    }
}