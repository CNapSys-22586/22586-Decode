package org.firstinspires.ftc.teamcode.cnapsys.subsystems.pusher;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.configurables.annotations.Sorter;

@Configurable
public class pusherConfig {
    @Sorter(sort = 0)
    public static double engagedPosition = 0.44;
    @Sorter(sort = 1)
    public static double idlePosition = 0.2;
    @Sorter(sort = 2)
    public static double encVoltage = 3.3;
    @Sorter(sort = 3)
    public static double threshold = 0.05;
    @Sorter(sort = 4)
    public static double delay = 100;
}
