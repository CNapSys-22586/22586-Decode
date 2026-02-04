package org.firstinspires.ftc.teamcode.cnapsys.core;


import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.cnapsys.core.utils.Drawing;
import org.firstinspires.ftc.teamcode.cnapsys.core.utils.RGBLight;
import org.firstinspires.ftc.teamcode.cnapsys.core.utils.colorSensor;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.Alliance;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.Colors;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.FieldState;
import org.firstinspires.ftc.teamcode.cnapsys.subsystems.indexer.indexer;
import org.firstinspires.ftc.teamcode.cnapsys.subsystems.intake.intake;
import org.firstinspires.ftc.teamcode.cnapsys.subsystems.pusher.pusher;
import org.firstinspires.ftc.teamcode.cnapsys.subsystems.shooter.shooter;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

public class Robot {
    public static Pose defaultPose = new Pose(38, 33, Math.toRadians(90));
    public Pose shootTarget = new Pose(6, 144, 0);
    public final intake intake;
    public final pusher pusher;
    public final RGBLight indicatorLight;
    public final indexer indexer;
    public final shooter shooter;
    public final Follower follower;
    public final ElapsedTime timer;
    public final TelemetryManager tm;
    private final boolean isTeleop;
    int shooterState = -1;
    private boolean shootAll = false;

    public Robot(HardwareMap hwMap, Alliance alliance, boolean isteleop) {
        this.isTeleop = isteleop;
        if (alliance == Alliance.RED) shootTarget = shootTarget.mirror();
        indicatorLight = new RGBLight(hwMap.get(Servo.class, "indicatorLight"));
        intake = new intake(hwMap.get(DcMotorEx.class, "intakeMotor"));
        pusher = new pusher(hwMap.get(Servo.class, "pusherServo"), hwMap.get(AnalogInput.class, "pusherServoAnalog"));
        indexer = new indexer(hwMap.get(Servo.class, "indexerServo"), hwMap.get(AnalogInput.class, "indexerServoAnalog"), new colorSensor(hwMap.get(RevColorSensorV3.class, "colorSensor"), 5));
        shooter = new shooter(hwMap.get(DcMotorEx.class, "shooterMotorA"), hwMap.get(DcMotorEx.class, "shooterMotorB"), hwMap.get(Servo.class, "hoodServo"));
        follower = Constants.createFollower(hwMap);
        follower.setPose(defaultPose);
        follower.setMaxPower(1);
        timer = new ElapsedTime();
        tm = PanelsTelemetry.INSTANCE.getTelemetry();
        Drawing.init();
    }

    public double getDistance() {
        Pose followerPose = follower.getPose();
        return Math.sqrt(Math.pow(followerPose.getX() - shootTarget.getX(), 2) + Math.pow(followerPose.getY() - shootTarget.getY(), 2));
    }
    public void shootGreen () {
        if (shooterState > -1 || !FieldState.isInit()) return;
        indexer.setManual();
        if(!indexer.selectGreen()) {
            indexer.setAutomatic();
            return;
        }
        shooterState = 0;
    }

    public boolean isBusy() {
        return shooterState > -1;
    }

    public void shootAll () {
        if (shooterState > -1 || !FieldState.isInit()) return;
        FieldState.resetPattern();
        shootAll = true;
        indexer.setManual();
        shooterState = 4;
    }

    public void shootPurple () {
        if (shooterState > -1 || !FieldState.isInit()) return;
        indexer.setManual();
        if (!indexer.selectPurple()) {
            indexer.setAutomatic();
            return;
        }
        shooterState = 0;
    }

    public void cancelShooting() {
        shootAll = false;
        shooterState = 3;
    }

    public double getTurnAngle() {
        return Math.atan2(shootTarget.getY() - follower.getPose().getY(), shootTarget.getX() - follower.getPose().getX());
    }

    public void update() {
        tm.addData("Robot X: ", follower.getPose().getX());
        tm.addData("Robot Y: ", follower.getPose().getY());
        tm.addData("Robot heading: ", Math.toDegrees(follower.getPose().getHeading()));
        tm.addData("Target heading for shooting: ", Math.toDegrees(getTurnAngle()));
        tm.addData("Robot distance from basket:", getDistance());
        tm.addData("ROBOT STATE: ", shooterState);
        if (shooterState <= -1) {
            if (indexer.isFull()) indicatorLight.setColor(Colors.BLUE);
            else indicatorLight.turnOff();
        }
        switch (shooterState) {
            case -2:
                shooterState = -1;
                if (isTeleop) follower.startTeleopDrive();
                break;
            case -1:
                break;
            case 0:
                follower.turnTo(getTurnAngle());
                shooterState = 1;
                break;
            case 1:
                shooter.setShooterTarget(getDistance());
                shooter.enable();
                if (!indexer.isBusy()) {
                    shooterState = 2;
                }
                break;
            case 2:
                if (!shooter.isBusy() && !indexer.isBusy()) {
                    pusher.activate();
                    shooterState = 3;
                }
                break;
            case 3:
                if (pusher.isIdle()) {
                    indexer.emptySlot(indexer.getSelected());
                    if (shootAll) {
                        if (!indexer.isEmpty()) {
                            shooterState = 4;
                            break;
                        }
                        shootAll = false;
                    }
                    shooter.disable();
                    shooterState = -2;
                    indexer.setAutomatic();
                }
                break;
            case 4:
                if (indexer.isEmpty()) {
                    shooterState = -2;
                    indexer.setAutomatic();
                    break;
                }
                if (FieldState.getNext() == Colors.GREEN) {
                    if (!indexer.selectGreen()) {
                        indicatorLight.setColor(Colors.PURPLE);
                        indexer.selectPurple();
                    }
                    else indicatorLight.setColor(Colors.GREEN);
                }
                else {;
                    if(!indexer.selectPurple()) {
                        indicatorLight.setColor(Colors.GREEN);
                        indexer.selectGreen();
                    }
                    else indicatorLight.setColor(Colors.PURPLE);
                }
                shooterState = 0;
                break;

        }
        double deltaTime = timer.milliseconds();
        indexer.update(deltaTime, tm);
        pusher.update(deltaTime, tm);
        shooter.update(deltaTime, tm);
        intake.update(deltaTime, tm);
        Drawing.update(follower.getPose(), shootTarget, false);
        follower.update();
    }
}
