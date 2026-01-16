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
    public static double wait = 500; //miliseconds
    @Sorter(sort = 3)
    public static double threshold = 0.02;
    //voltages need calibrating for the specific servo
    @Sorter(sort = 4)
    public static double voltageRangeMin = 0.2;
    @Sorter(sort = 5)
    public static double voltageRangeMax = 3.1;
}
