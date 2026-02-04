package org.firstinspires.ftc.teamcode.cnapsys.core.vars;

public class ShooterPoint {
    public double hoodAngle;
    public double TPS;

    public double distanceFromTarget;

    public ShooterPoint(double hoodAngle, double TPS, double distanceFromTarget) {
        this.hoodAngle = hoodAngle;
        this.TPS = TPS;
        this.distanceFromTarget = distanceFromTarget;
    }
}
