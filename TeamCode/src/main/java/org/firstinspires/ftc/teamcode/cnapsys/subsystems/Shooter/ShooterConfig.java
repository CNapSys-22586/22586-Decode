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
    public static double KS = 0;
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
    public static double TPR = 28;
    @Sorter(sort=11)
    public static double HOOD_COMPENSATION_AMOUNT = 0.010;
    @Sorter(sort=12)
    public static List<ShooterPoint> INTERPOLATION_POINTS = new ArrayList<>();
//
//    static {
//        INTERPOLATION_POINTS.add(new ShooterPoint(30, 36.5, 1420, 39));
//        INTERPOLATION_POINTS.add(new ShooterPoint(30, 45, 1600, 69));
//        INTERPOLATION_POINTS.add(new ShooterPoint(30, 46, 1660, 80));
//        INTERPOLATION_POINTS.add(new ShooterPoint(33, 46.5, 1750, 90));
//        INTERPOLATION_POINTS.add(new ShooterPoint(36, 46.5, 1760, 100));
//        INTERPOLATION_POINTS.add(new ShooterPoint(42, 46.5, 1840, 110));
//        INTERPOLATION_POINTS.add(new ShooterPoint(44, 46.5, 1900, 120));
//        INTERPOLATION_POINTS.add(new ShooterPoint(46, 46.5, 2000, 130));
//    }

//    static {
//        INTERPOLATION_POINTS.add(new ShooterPoint(33, 33, 1454, 30));
//        INTERPOLATION_POINTS.add(new ShooterPoint(33, 36.5, 1454, 40));
//        INTERPOLATION_POINTS.add(new ShooterPoint(33, 41, 1550, 50));
//        INTERPOLATION_POINTS.add(new ShooterPoint(33, 42, 1600, 60));
//        INTERPOLATION_POINTS.add(new ShooterPoint(34, 43, 1660, 70));
//        INTERPOLATION_POINTS.add(new ShooterPoint(37, 44, 1900, 80));
//        INTERPOLATION_POINTS.add(new ShooterPoint(37, 43, 1920, 90));
//        INTERPOLATION_POINTS.add(new ShooterPoint(33, 46, 2000, 100));
//        INTERPOLATION_POINTS.add(new ShooterPoint(37, 45, 2000, 110));
//        INTERPOLATION_POINTS.add(new ShooterPoint(38, 45.5, 2100, 120));
//        INTERPOLATION_POINTS.add(new ShooterPoint(38, 46, 2100, 130));
//        INTERPOLATION_POINTS.add(new ShooterPoint(37, 46, 2200, 140));
//        INTERPOLATION_POINTS.add(new ShooterPoint(30, 46.8, 2300, 150));
//        INTERPOLATION_POINTS.add(new ShooterPoint(39, 46.8, 2300, 160));
//
//    }

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

//    static {
//        INTERPOLATION_POINTS.add(new ShooterPoint(30, 30, 1400, 30));
//        INTERPOLATION_POINTS.add(new ShooterPoint(35, 35, 1400, 40));
//        INTERPOLATION_POINTS.add(new ShooterPoint(36, 36, 1470, 50));
//        INTERPOLATION_POINTS.add(new ShooterPoint(37, 37, 1470, 60));
//        INTERPOLATION_POINTS.add(new ShooterPoint(37, 37, 1520, 70));
//        INTERPOLATION_POINTS.add(new ShooterPoint(37, 37, 1600, 80));
//        INTERPOLATION_POINTS.add(new ShooterPoint(37, 37, 1670, 90));
//        INTERPOLATION_POINTS.add(new ShooterPoint(40, 40, 1770, 100));
//        INTERPOLATION_POINTS.add(new ShooterPoint(42, 42, 1860, 110));
//        INTERPOLATION_POINTS.add(new ShooterPoint(45, 45, 1900, 120));
//        INTERPOLATION_POINTS.add(new ShooterPoint(46.5, 46.5, 1920, 130));
//    }

//    static {
//        INTERPOLATION_POINTS.add(new ShooterPoint(30, 30, 1450, 30));
//        INTERPOLATION_POINTS.add(new ShooterPoint(30, 30, 1450, 40));
//        INTERPOLATION_POINTS.add(new ShooterPoint(30, 30, 1500, 50));
//        INTERPOLATION_POINTS.add(new ShooterPoint(36, 30, 1666, 60));
//        INTERPOLATION_POINTS.add(new ShooterPoint(42, 30, 1660, 70));
//        INTERPOLATION_POINTS.add(new ShooterPoint(42, 30, 1730, 80));
//        INTERPOLATION_POINTS.add(new ShooterPoint(43, 30, 1820, 90));
//        INTERPOLATION_POINTS.add(new ShooterPoint(40, 40, 1770, 100));
//        INTERPOLATION_POINTS.add(new ShooterPoint(42, 42, 1860, 110));
//        INTERPOLATION_POINTS.add(new ShooterPoint(45, 45, 1900, 120));
//        INTERPOLATION_POINTS.add(new ShooterPoint(46.5, 46.5, 1920, 130));
//    }
    @Sorter(sort=11)
    public static boolean DEBUG_MODE = false;

}
