package org.firstinspires.ftc.teamcode.cnapsys.core.utils;


import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.configurables.annotations.Sorter;

import org.firstinspires.ftc.teamcode.cnapsys.core.vars.RGB;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.hueRange;

@Configurable
public class colorSensorConfig {
    @Sorter(sort = 1)
    public static hueRange greenRange = new hueRange(80, 138);
    @Sorter(sort = 2)
    public static hueRange purpleRange = new hueRange(240, 270);
    @Sorter(sort = 3)
    public static double distanceThreshold = 8;
}
