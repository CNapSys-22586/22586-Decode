package org.firstinspires.ftc.teamcode.cnapsys.core.utils;

public class MathUtils {
    public static double linearInterpolation(double x, double x1, double x2, double y1, double y2) {
        return y1 + (x - x1) * (y2 - y1) / (x2 - x1);
    }

}
