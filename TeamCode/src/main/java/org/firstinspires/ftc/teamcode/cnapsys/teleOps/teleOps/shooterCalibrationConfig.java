package org.firstinspires.ftc.teamcode.cnapsys.teleOps.teleOps;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.configurables.annotations.Sorter;

@Configurable
public class shooterCalibrationConfig {
    @Sorter(sort=1)
    public static double TARGET_RPM = 0;
    @Sorter(sort=2)
    public static double TARGET_ANGLE = 40;
    @Sorter(sort=2)
    public static boolean enabled = false;
}
