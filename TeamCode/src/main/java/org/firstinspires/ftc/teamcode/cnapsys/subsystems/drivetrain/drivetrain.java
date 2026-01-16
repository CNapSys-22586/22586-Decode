package org.firstinspires.ftc.teamcode.cnapsys.subsystems.drivetrain;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierPoint;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedroPathing.Alliance;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

public class drivetrain {
    private Follower f;
    private final Alliance a;
    private boolean hold = false, field = true;
    public drivetrain(HardwareMap hardwareMap, Alliance a, Pose start) {
        f = Constants.createFollower(hardwareMap);
        f.setStartingPose(start);
        this.a = a;
    }

    public void startDrive() {
        f.startTeleopDrive();
    }

    public void resetDrive() {
        if (a.equals(Alliance.BLUE)) {
            f.setPose(new Pose(8, 6.25, Math.toRadians(0)).mirror());
        } else {
            f.setPose(new Pose(8, 6.25, Math.toRadians(0)));
        }
    }

    public void periodic() {
        f.update();
    }

    public void drive(Gamepad g) {
        if (!hold)
            if (field)
                f.setTeleOpDrive(-g.left_stick_y, -g.left_stick_x, -g.right_stick_x, false, a == Alliance.BLUE ? Math.toRadians(180) : 0);
            else
                f.setTeleOpDrive(-g.left_stick_y, -g.left_stick_x, -g.right_stick_x, true);
    }

    public void holdCurrent() {
        f.holdPoint(new BezierPoint(f.getPose()), f.getHeading(), true);
        hold = true;
    }

    public void releaseHold() {
        hold = false;
    }

    public void teleToggleCentric() {
        field = !field;
    }

    public void cornerReset() {
        if (a.equals(Alliance.BLUE))
            f.setPose(new Pose(8, 6.25, Math.toRadians(0)).mirror());
        else
            f.setPose(new Pose(8, 6.25, Math.toRadians(0)));
    }

    public void setStart(Pose start) {
        f.setStartingPose(start);
    }

    public Pose getPose() {
        return f.getPose();
    }

    public Pose isBusy() {
        return f.getPose();
    }

    public double getT() {
        return f.getCurrentTValue();
    }

    public Follower getFollower() { return f;}
}
