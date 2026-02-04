package org.firstinspires.ftc.teamcode.cnapsys.core.utils;


import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.configurables.annotations.Sorter;

@Configurable
public class colorSensorConfig {
    @Sorter(sort = 1)
    public static double distanceThreshold = 8;
}
