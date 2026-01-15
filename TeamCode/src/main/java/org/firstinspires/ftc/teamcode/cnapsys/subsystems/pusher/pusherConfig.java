package org.firstinspires.ftc.teamcode.cnapsys.subsystems.pusher;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.configurables.annotations.Sorter;

@Configurable
public class pusherConfig {
    @Sorter(sort = 0)
    public static int engagedPosition;
    @Sorter(sort = 1)
    public static int idlePosition;
    @Sorter(sort = 2)
    public static double wait = 1.0;
    @Sorter(sort = 3)
    public double threshold = 0.01;
}
