package org.firstinspires.ftc.teamcode.cnapsys.subsystems.indexer;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.configurables.annotations.Sorter;

@Configurable
public class indexerConfig {
    @Sorter(sort = 0)
    public static double[] intakeRotations = {0.026, 0.215, 0.397};
    @Sorter(sort = 1)
    public static double[] outakeRotations = {0.3, 0.484, 0.665};
    @Sorter(sort = 2)
    public static double outakeThreshold = 0.05;
    @Sorter(sort = 3)
    public static double intakeThreshold = 0.08;
    @Sorter(sort = 4)
    public static double AVG_VOLTAGE = 3.3;
    @Sorter(sort = 5)
    public static double delay = 200;
}
