package org.firstinspires.ftc.teamcode.cnapsys.core.utils;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.cnapsys.core.vars.Colors;

import java.util.EnumMap;
import java.util.Map;

public class RGBLight {
    private final Servo lightIndicator;
    private final Map<Colors, Float> colorsFloatMap = new EnumMap<>(Colors.class);

    {
        colorsFloatMap.put(Colors.RED, 0.280f);
        colorsFloatMap.put(Colors.GREEN, 0.500f);
        colorsFloatMap.put(Colors.BLUE, 0.611f);
        colorsFloatMap.put(Colors.PURPLE, 0.722f);
    }


    public RGBLight (Servo lightIndicator) {
        this.lightIndicator = lightIndicator;
    }

    public void setColor(Colors color) {
        Float pos = colorsFloatMap.get(color);
        if (pos != null) lightIndicator.setPosition(pos);
        else lightIndicator.setPosition(0.0f);
    }

    public void turnOff() {
        lightIndicator.setPosition(0.0f);
    }
}
