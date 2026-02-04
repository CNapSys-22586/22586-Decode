package org.firstinspires.ftc.teamcode.cnapsys.core.utils;

import com.qualcomm.hardware.rev.RevColorSensorV3;

import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.Colors;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.RGB;

public class colorSensor {
    private final RevColorSensorV3 sensor;

    public colorSensor(RevColorSensorV3 colorSensor) {
        this.sensor = colorSensor;
    }

    public colorSensor(RevColorSensorV3 colorSensor, float gain) {
        this.sensor = colorSensor;
        sensor.setGain(gain);
    }

    public boolean isObjectDetected() {
        double distance = sensor.getDistance(DistanceUnit.CM);
        return distance <= colorSensorConfig.distanceThreshold;
    }

    public double getDistance() {
        return sensor.getDistance(DistanceUnit.CM);
    }

    public void setGain(float gain) {
        sensor.setGain(gain);
    }

    public RGB getRawColor() {
        double R = sensor.getNormalizedColors().red / sensor.getNormalizedColors().alpha;
        double G = sensor.getNormalizedColors().green / sensor.getNormalizedColors().alpha;
        double B = sensor.getNormalizedColors().blue / sensor.getNormalizedColors().alpha;
        return new RGB(R, G, B);
    }

    public Colors getColor() {
        double R = sensor.getNormalizedColors().red / sensor.getNormalizedColors().alpha;
        double G = sensor.getNormalizedColors().green / sensor.getNormalizedColors().alpha;
        double B = sensor.getNormalizedColors().blue / sensor.getNormalizedColors().alpha;

        if (G / R > 3) return Colors.GREEN;
        else if ((B + R) / G > 1.5) return Colors.PURPLE;
        return Colors.GREEN;
    }
}
