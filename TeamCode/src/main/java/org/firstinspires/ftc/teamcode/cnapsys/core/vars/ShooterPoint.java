package org.firstinspires.ftc.teamcode.cnapsys.core.vars;

public class ShooterPoint {
    public double maxHoodAngle, minHoodAngle;
    public double TPS;

    public double distanceFromTarget;

    public ShooterPoint(double minHoodAngle, double maxHoodAngle, double TPS, double distanceFromTarget) {
        this.minHoodAngle = minHoodAngle;
        this.maxHoodAngle = maxHoodAngle;
        this.TPS = TPS;
        this.distanceFromTarget = distanceFromTarget;
    }
}
