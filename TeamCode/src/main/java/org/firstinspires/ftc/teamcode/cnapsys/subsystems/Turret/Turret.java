package org.firstinspires.ftc.teamcode.cnapsys.subsystems.Turret;
import static com.pedropathing.math.MathFunctions.clamp;

import android.annotation.SuppressLint;

import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.cnapsys.core.RobotConfig;
import org.firstinspires.ftc.teamcode.cnapsys.core.interfaces.Subsystem;
import org.firstinspires.ftc.teamcode.cnapsys.core.utils.PIDF;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.Alliance;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.SubsystemData;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagMetadata;

public class Turret implements Subsystem {

    private final DcMotorEx motor;
    private final PIDF pidf = new PIDF(TurretConfig.kP, TurretConfig.kI, TurretConfig.kD, TurretConfig.kF);
    private double currentAngleDeg, targetAngleDeg, ticksPerDeg;
    private double targetX, targetY;
    private boolean enabled = true;

    public Turret(DcMotorEx motor, boolean reset) {
        this.motor = motor;

        if (reset) this.motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.motor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void reset() {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public boolean isBusy() {
        return Math.abs(targetAngleDeg - currentAngleDeg) > TurretConfig.THRESHOLD;
    }

    @Override
    public void update(double deltaTime, TelemetryManager tm, SubsystemData data) {
        if (!RobotConfig.COMPENSATE_FOR_VELOCITY) {
            targetX = data.goalPose.getX();
            targetY = data.goalPose.getY();
        }
        else {
            targetX = data.goalPoseAdjusted.getX();
            targetY = data.goalPoseAdjusted.getY();
        }
        ticksPerDeg = (TurretConfig.TPR * TurretConfig.GEAR_RATIO) / 360.0;
        pidf.setParams(TurretConfig.kP, TurretConfig.kI, TurretConfig.kD, TurretConfig.kF);
        currentAngleDeg = motor.getCurrentPosition() / ticksPerDeg;

        double heading = data.follower.getPose().getHeading();
        double robotX   = data.follower.getPose().getX();
        double robotY   = data.follower.getPose().getY();

        double turretWorldX = robotX + TurretConfig.MOUNT_OFFSET_X * Math.cos(heading) - TurretConfig.MOUNT_OFFSET_Y * Math.sin(heading);
        double turretWorldY = robotY + TurretConfig.MOUNT_OFFSET_X * Math.sin(heading) + TurretConfig.MOUNT_OFFSET_Y * Math.cos(heading);

// --- Now compute angle FROM the turret's actual position ---
        double angleToTarget = Math.atan2(targetY - turretWorldY, targetX - turretWorldX);

        double robotRelativeAngle = -angleToTarget + heading;
        double turretMountOffset  = Math.toRadians(TurretConfig.ROTATION_OFFSET);
        targetAngleDeg = Math.toDegrees(robotRelativeAngle - turretMountOffset);

//        double angleToTarget = Math.atan2(targetY - data.follower.getPose().getY(), targetX - data.follower.getPose().getX());
//
//        double robotRelativeAngle = -angleToTarget + data.follower.getPose().getHeading();
//
//        double turretMountOffset = Math.toRadians(TurretConfig.ROTATION_OFFSET);
//
//        targetAngleDeg = Math.toDegrees(robotRelativeAngle - turretMountOffset);

        if (!enabled) targetAngleDeg = -TurretConfig.ROTATION_OFFSET;

        if(targetAngleDeg >= 400)targetAngleDeg=targetAngleDeg-360;
        if(targetAngleDeg < 0)targetAngleDeg=targetAngleDeg+360;
        double error = targetAngleDeg - currentAngleDeg;

        double power = pidf.update(error);

        motor.setPower(power);

        if (TurretConfig.DEBUG_MODE) {
            tm.addData("Turret/Power", power);
            tm.addData("Turret/Error", error);
            tm.addData("Turret/Current",    String.format("%.1f°", currentAngleDeg));
            tm.addData("Turret/Target",     String.format("%.1f°", targetAngleDeg));
         }
    }
}