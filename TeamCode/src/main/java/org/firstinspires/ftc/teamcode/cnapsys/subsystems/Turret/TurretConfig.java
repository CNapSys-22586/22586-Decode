package org.firstinspires.ftc.teamcode.cnapsys.subsystems.Turret;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.configurables.annotations.Sorter;

@Configurable
public class TurretConfig {
    // ── Motor / Encoder ──────────────────────────────────────────────────────
    @Sorter(sort = 1)
    public static double TPR = 145.1;
    @Sorter(sort = 3)
    public static double GEAR_RATIO = (double) 218 / 38;
    @Sorter(sort = 4)
    public static double THRESHOLD = 5;

    // ── PID ──────────────────────────────────────────────────────────────────
    @Sorter(sort = 5)
    public static double kP = 0.03;
    @Sorter(sort = 6)
    public static double kI = 0.000;
    @Sorter(sort = 7)
    public static double kD = 0.002;
    @Sorter(sort = 8)
    public static double kF = 0.000;
    @Sorter(sort = 9)
    public static double ROTATION_OFFSET = -90;
    @Sorter(sort = 10)
    public static double MOUNT_OFFSET_X = 0;
    @Sorter(sort = 11)
    public static double MOUNT_OFFSET_Y = -5.31;
    @Sorter(sort = 12)
    public static boolean DEBUG_MODE = false;
}