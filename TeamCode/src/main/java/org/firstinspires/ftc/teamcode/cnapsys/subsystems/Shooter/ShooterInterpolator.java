package org.firstinspires.ftc.teamcode.cnapsys.subsystems.Shooter;

import org.firstinspires.ftc.teamcode.cnapsys.core.vars.ShooterPoint;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ShooterInterpolator {

    private List<ShooterPoint> shooterPoints;

    public ShooterInterpolator(List<ShooterPoint> shooterPoints) {
        setShooterPoints(shooterPoints);
    }

    public void setShooterPoints (List<ShooterPoint> shooterPoints) {
        if (shooterPoints == null || shooterPoints.size() < 2) {
            throw new IllegalArgumentException("ShooterInterpolator requires at least 2 ShooterPoints.");
        }
        this.shooterPoints = new ArrayList<>(shooterPoints);
        this.shooterPoints.sort(Comparator.comparingDouble(p -> p.distanceFromTarget));
    }
    public ShooterPoint interpolateData(double distance) {
        int[] bracket = findDistanceBracket(distance);
        int lo = bracket[0], hi = bracket[1];

        ShooterPoint p1 = shooterPoints.get(lo);
        ShooterPoint p2 = shooterPoints.get(hi);

        if (lo == hi) return p1; // edge case: distance exactly on a point or out of range

        double t = (distance - p1.distanceFromTarget) / (p2.distanceFromTarget - p1.distanceFromTarget);
        double targetTPS = lerp(p1.TPS, p2.TPS, t);
        double hoodAngle = lerp(p1.minHoodAngle, p2.minHoodAngle, t);
        return new ShooterPoint(hoodAngle, p2.maxHoodAngle, targetTPS, distance);
    }
    private int[] findDistanceBracket(double distance) {
        int n = shooterPoints.size();

        // Below minimum
        if (distance <= shooterPoints.get(0).distanceFromTarget) return new int[]{0, 0};

        // Above maximum
        if (distance >= shooterPoints.get(n - 1).distanceFromTarget) return new int[]{n - 1, n - 1};

        // Binary search for the lower bracket index
        int lo = 0, hi = n - 1;
        while (hi - lo > 1) {
            int mid = (lo + hi) / 2;
            if (shooterPoints.get(mid).distanceFromTarget <= distance) lo = mid;
            else hi = mid;
        }
        return new int[]{lo, hi};
    }

    private static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }
}
