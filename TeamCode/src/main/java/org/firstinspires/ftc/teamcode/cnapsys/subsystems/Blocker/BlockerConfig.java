package org.firstinspires.ftc.teamcode.cnapsys.subsystems.Blocker;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.configurables.annotations.Sorter;

@Configurable
public class BlockerConfig {
    @Sorter(sort=1)
    public static double BLOCKER_SERVO_CLOSED_POS = 0.48;
    @Sorter(sort=2)
    public static double BLOCKER_SERVO_OPEN_POS = 0.34;
    @Sorter(sort=3)
    public static double DELAY = 500;
}
