package org.firstinspires.ftc.teamcode.cnapsys.subsystems.Shooter;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.configurables.annotations.Sorter;

import org.firstinspires.ftc.teamcode.cnapsys.core.vars.ShooterPoint;

import java.util.ArrayList;
import java.util.List;

@Configurable
public class ShooterConfig {
    @Sorter(sort=1)
    public static double KP = 0.01;
    @Sorter(sort=2)
    public static double KV = 0.00033;
    @Sorter(sort=3)
    public static double KS = 0.09;
    @Sorter(sort=5)
    public static double GEAR_RATIO = (double) 365 / 40;
    @Sorter(sort=6)
    public static double THRESHOLD = 40;
    @Sorter(sort=7)
    public static double IDLE_TPS = 1600;
    @Sorter(sort=7)
    public static double HOOD_ANGLE_MIN = 33;
    @Sorter(sort=8)
    public static double HOOD_ANGLE_MAX = 46.8;
    @Sorter(sort=9)
    public static double EFFICIENCY = 0.17;
    @Sorter(sort=10)
    public static double HOOD_COMPENSATION_AMOUNT = 0.012;
    @Sorter(sort=11)
    public static List<ShooterPoint> INTERPOLATION_POINTS = new ArrayList<>();

    static {
        INTERPOLATION_POINTS.add(new ShooterPoint(33, 33, 1310, 30));
        INTERPOLATION_POINTS.add(new ShooterPoint(33, 37, 1360, 40));
        INTERPOLATION_POINTS.add(new ShooterPoint(33, 38, 1360, 50));
        INTERPOLATION_POINTS.add(new ShooterPoint(33, 40, 1380, 60));
        INTERPOLATION_POINTS.add(new ShooterPoint(33, 41, 1380, 70));
        INTERPOLATION_POINTS.add(new ShooterPoint(33, 41, 1420, 80));
        INTERPOLATION_POINTS.add(new ShooterPoint(33, 41, 1480, 90));
        INTERPOLATION_POINTS.add(new ShooterPoint(33, 41, 1560, 100));
        INTERPOLATION_POINTS.add(new ShooterPoint(33, 42, 1620, 110));
        INTERPOLATION_POINTS.add(new ShooterPoint(33, 44, 1740, 120));
        INTERPOLATION_POINTS.add(new ShooterPoint(33, 45.5, 1850, 130));
        INTERPOLATION_POINTS.add(new ShooterPoint(33, 45.7, 1900, 140));
        INTERPOLATION_POINTS.add(new ShooterPoint(33, 46.8, 1900, 150));
        INTERPOLATION_POINTS.add(new ShooterPoint(33, 46.8, 1900, 160));
    }
    @Sorter(sort=12)
    public static boolean DEBUG_MODE = false;

}
