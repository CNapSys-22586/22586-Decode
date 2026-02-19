package org.firstinspires.ftc.teamcode.cnapsys.subsystems.shooter;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.configurables.annotations.Sorter;

import org.firstinspires.ftc.teamcode.cnapsys.core.vars.ShooterPoint;

@Configurable
public class shooterConfig {
    @Sorter(sort=1)
    public static double kp = 0.012;
    @Sorter(sort=2)
    public static double ki= 0.0;
    @Sorter(sort=3)
    public static double kd = 0.00009;
    @Sorter(sort=4)
    public static double kf = 0.57;
    @Sorter(sort=5)
    public static double GEAR_RATIO = (double) 344 / 60;
    @Sorter(sort=6)
    public static double threshold = 10;
    @Sorter(sort=7)
    public static ShooterPoint[] table = {new ShooterPoint(30, 1100, 42), new ShooterPoint(65, 1900, 133)};
}
