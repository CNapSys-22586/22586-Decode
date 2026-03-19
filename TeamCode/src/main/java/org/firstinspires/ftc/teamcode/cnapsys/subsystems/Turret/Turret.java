package org.firstinspires.ftc.teamcode.cnapsys.subsystems.Turret;

import android.annotation.SuppressLint;

import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.cnapsys.core.RobotConfig;
import org.firstinspires.ftc.teamcode.cnapsys.core.interfaces.Subsystem;
import org.firstinspires.ftc.teamcode.cnapsys.core.utils.PIDF;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.SubsystemData;

public class Turret implements Subsystem {

    private final DcMotorEx motor;
    private final PIDF pidf = new PIDF(TurretConfig.kP, TurretConfig.kI, TurretConfig.kD, TurretConfig.kF);
    private double currentAngleDeg, turretWorldX, turretWorldY;
    private double targetAngleDeg;
    private boolean enabled = true;
    private Pose turretWorldPos = new Pose(0, 0, 0);

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

    public Pose getTurretWordPose() {
        return turretWorldPos;
    }

    @Override
    public boolean isBusy() {
        return Math.abs(targetAngleDeg - currentAngleDeg) > TurretConfig.THRESHOLD;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void update(double deltaTime, TelemetryManager tm, SubsystemData data) {
        //GET TARGET POSE
        double targetY;
        double targetX;
        if (!RobotConfig.COMPENSATE_FOR_VELOCITY) {
            targetX = data.goalPose.getX();
            targetY = data.goalPose.getY();
        }
        else {
            targetX = data.goalPoseAdjusted.getX();
            targetY = data.goalPoseAdjusted.getY();
        }

        //GET CURRENT HEADING
        double ticksPerDeg = (TurretConfig.TPR * TurretConfig.GEAR_RATIO) / 360.0;
        pidf.setParams(TurretConfig.kP, TurretConfig.kI, TurretConfig.kD, TurretConfig.kF);
        currentAngleDeg = motor.getCurrentPosition() / ticksPerDeg;

        //GET ROBOT COORDS
        double heading = data.follower.getPose().getHeading();
        double robotX   = data.follower.getPose().getX();
        double robotY   = data.follower.getPose().getY();

        //GET TURRET COORDS
        turretWorldX = robotX + TurretConfig.MOUNT_OFFSET_X * Math.cos(heading) - TurretConfig.MOUNT_OFFSET_Y * Math.sin(heading);
        turretWorldY = robotY + TurretConfig.MOUNT_OFFSET_X * Math.sin(heading) + TurretConfig.MOUNT_OFFSET_Y * Math.cos(heading);
        turretWorldPos = new Pose(turretWorldX, turretWorldY, -Math.toRadians(currentAngleDeg + TurretConfig.ROTATION_OFFSET) + heading);

        //COMPUTE NEW HEADING
        double angleToTarget = Math.atan2(targetY - turretWorldY, targetX - turretWorldX);

        double robotRelativeAngle = -angleToTarget + heading;
        double turretMountOffset  = Math.toRadians(TurretConfig.ROTATION_OFFSET);
        targetAngleDeg = Math.toDegrees(robotRelativeAngle - turretMountOffset);

        if (!enabled) targetAngleDeg = -TurretConfig.ROTATION_OFFSET;

        //WRAP AROUND LOGIC
        if(targetAngleDeg > 400) targetAngleDeg = targetAngleDeg - 360;
        if(targetAngleDeg < 0) targetAngleDeg = targetAngleDeg + 360;

        double error = targetAngleDeg - currentAngleDeg;

        double power = pidf.update(error);

        motor.setPower(power);

        //DEBUG DATA
        if (TurretConfig.DEBUG_MODE) {
            tm.addData("Turret/Power", power);
            tm.addData("Turret/Error", error);
            tm.addData("Turret/Current",    String.format("%.1f°", currentAngleDeg));
            tm.addData("Turret/Target",     String.format("%.1f°", targetAngleDeg));
         }
    }
}