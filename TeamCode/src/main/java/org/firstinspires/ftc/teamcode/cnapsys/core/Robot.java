package org.firstinspires.ftc.teamcode.cnapsys.core;


import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.cnapsys.core.utils.Drawing;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.Alliance;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.SubsystemData;
import org.firstinspires.ftc.teamcode.cnapsys.subsystems.Blocker.Blocker;
import org.firstinspires.ftc.teamcode.cnapsys.subsystems.Turret.Turret;
import org.firstinspires.ftc.teamcode.cnapsys.subsystems.Intake.Intake;
import org.firstinspires.ftc.teamcode.cnapsys.subsystems.Shooter.Shooter;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

public class Robot {
    public Pose goalPose;
    private boolean firstUpdate = false;
    public final Intake intake;
    public final VoltageSensor voltageSensor;
    public final Shooter shooter;
    public final Blocker blocker;
    public final Turret turret;
    public final Follower follower;
    public final ElapsedTime timer;
    public final TelemetryManager tm;

    public boolean isFirstUpdateComplete() {
        return firstUpdate;
    }

    public Robot(HardwareMap hwMap, Alliance alliance, boolean resetSystems) {
        if (alliance == Alliance.RED) goalPose = RobotConfig.GOAL_POSE.mirror();
        else  goalPose = RobotConfig.GOAL_POSE;
        voltageSensor = hwMap.get(VoltageSensor.class, "Control Hub");
        turret = new Turret(hwMap.get(DcMotorEx.class, "turretMotor"), resetSystems);
        intake = new Intake(hwMap.get(DcMotorEx.class, "intakeMotor"));
        blocker = new Blocker(hwMap.get(Servo.class, "blockerServo"));
        shooter = new Shooter(hwMap.get(DcMotorEx.class, "shooterMotorA"), hwMap.get(DcMotorEx.class, "shooterMotorB"), hwMap.get(Servo.class, "hoodServo"));
        follower = Constants.createFollower(hwMap);
        follower.setPose(RobotConfig.ROBOT_POSE);


        timer = new ElapsedTime();
        tm = PanelsTelemetry.INSTANCE.getTelemetry();
        Drawing.init();
        intake.enable();
        shooter.enable();
        shooter.setEngaged();
    }

    public void shooterCycle() {
        if (follower.getPose().getY() >= RobotConfig.TOP_Y_THRESHOLD) {shooter.setEngaged();}
        else if (follower.getPose().getY() <= RobotConfig.DOWN_Y_THRESHOLD && follower.getPose().getX() >= RobotConfig.DOWN_X_THRESHOLD_LEFT && follower.getPose().getX() <= RobotConfig.DOWN_X_THRESHOLD_RIGHT) {shooter.setEngaged();}
        else {shooter.setIdle();}
    }

    public void update() {
        SubsystemData data = new SubsystemData();
        if (firstUpdate) data.goalPoseAdjusted = new Pose(goalPose.getX() - follower.getVelocity().getXComponent() * shooter.getTOF(), goalPose.getY() - follower.getVelocity().getYComponent() * shooter.getTOF());
        else data.goalPoseAdjusted = goalPose;
        data.goalPose = goalPose;
        data.follower = follower;

        tm.addData("X", follower.getPose().getX());
        tm.addData("Y", follower.getPose().getY());
        tm.addData("HEADING", Math.toDegrees(follower.getPose().getHeading()));

        double deltaTime = timer.milliseconds();

        shooterCycle();
        shooter.update(deltaTime, tm, data);
        intake.update(deltaTime, tm, data);
        turret.update(deltaTime, tm, data);
        blocker.update(deltaTime, tm, data);
        Drawing.update(follower.getPose(), goalPose, data.goalPoseAdjusted, (!shooter.isBusy()));
        follower.update();
        firstUpdate = true;
    }
}
