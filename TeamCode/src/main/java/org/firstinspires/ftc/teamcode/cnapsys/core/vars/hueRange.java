package org.firstinspires.ftc.teamcode.cnapsys.core.vars;

public class hueRange {
    public double min, max;

    public hueRange(double min, double max){
        this.min = min;
        this.max = max;
    }

    public boolean isInRange(double hue) {
        return hue >= min && hue <= max;
    }
}
