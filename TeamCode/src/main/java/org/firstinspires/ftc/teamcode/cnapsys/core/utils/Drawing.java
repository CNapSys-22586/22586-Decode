package org.firstinspires.ftc.teamcode.cnapsys.core.utils;

import com.bylazar.field.FieldManager;
import com.bylazar.field.PanelsField;
import com.bylazar.field.Style;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.Vector;

public class Drawing {

    private static final FieldManager panelsField = PanelsField.INSTANCE.getField();

    private static final Style adjustedTargetLine = new Style(
            "", "blue", 0.75
    );

    private static final Style robotLook = new Style(
            "", "#3F51B5", 0.75
    );

    private static final Style turretLookReadyToShoot = new Style(
            "", "green", 0.75
    );

    private static final Style turretLookNotReadyToShoot = new Style(
            "", "red", 0.75
    );

    private static final Style targetLine = new Style(
            "", "purple", 0.75
    );

    public static void init() {
        panelsField.setOffsets(PanelsField.INSTANCE.getPresets().getPEDRO_PATHING());
    }
    public static void drawRobot(Pose pose, Style style) {
        if (pose == null || Double.isNaN(pose.getX()) || Double.isNaN(pose.getY()) || Double.isNaN(pose.getHeading())) {
            return;
        }

        panelsField.setStyle(style);
        panelsField.moveCursor(pose.getX(), pose.getY());
        panelsField.circle(10);

        Vector v = pose.getHeadingAsUnitVector();
        v.setMagnitude(v.getMagnitude() * 10);
        double x1 = pose.getX() + v.getXComponent() / 2, y1 = pose.getY() + v.getYComponent() / 2;
        double x2 = pose.getX() + v.getXComponent(), y2 = pose.getY() + v.getYComponent();

        panelsField.setStyle(style);
        panelsField.moveCursor(x1, y1);
        panelsField.line(x2, y2);
    }

    public static void drawTurret(Pose pose, Style style) {
        if (pose == null || Double.isNaN(pose.getX()) || Double.isNaN(pose.getY()) || Double.isNaN(pose.getHeading())) {
            return;
        }

        panelsField.setStyle(style);
        panelsField.moveCursor(pose.getX(), pose.getY());
        panelsField.circle(4);

        Vector v = pose.getHeadingAsUnitVector();
        v.setMagnitude(v.getMagnitude() * 4);
        double x1 = pose.getX() + v.getXComponent() / 2, y1 = pose.getY() + v.getYComponent() / 2;
        double x2 = pose.getX() + v.getXComponent() * 2, y2 = pose.getY() + v.getYComponent() * 2;

        panelsField.setStyle(style);
        panelsField.moveCursor(x1, y1);
        panelsField.line(x2, y2);
    }

    public static void drawTargetLine(Pose turretPose, Pose targetPose, Pose offsetTargetPose) {
        panelsField.setStyle(targetLine);
        panelsField.moveCursor(turretPose.getX(), turretPose.getY());
        panelsField.line(targetPose.getX(), targetPose.getY());
        panelsField.setStyle(adjustedTargetLine);
        panelsField.moveCursor(turretPose.getX(), turretPose.getY());
        panelsField.line(offsetTargetPose.getX(), offsetTargetPose.getY());
    }

    public static void update(Pose robotPose, Pose targetPose, Pose offsetTargetPose, Pose turretPose, boolean isShooterSystemBusy) {
        drawRobot(robotPose, robotLook);
        drawTargetLine(turretPose, targetPose, offsetTargetPose);
        drawTurret(turretPose, isShooterSystemBusy ? turretLookNotReadyToShoot : turretLookReadyToShoot);
        panelsField.update();
    }
}
