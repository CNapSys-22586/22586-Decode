package org.firstinspires.ftc.teamcode.cnapsys.subsystems.indexer;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.configurables.annotations.Sorter;

@Configurable
public class indexerConfig {
    @Sorter(sort = 0)
    public static double voltageRangeMin = 0.2;
    @Sorter(sort = 1)
    public static double voltageRangeMax = 3.1;
    @Sorter(sort = 2)
    public static int[] green = {0, 0, 0, 0, 0, 0, 0, 0};
    @Sorter(sort = 3)
    public static int[] purple = {0, 0, 0, 0, 0, 0, 0, 0};
    public static double[] intakeRotations = {0, 120, 240};
    public static double[] outakeRotations = {180, 300, 60};
}
