package org.firstinspires.ftc.teamcode.cnapsys.subsystems.indexer;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.configurables.annotations.Sorter;

@Configurable
public class indexerConfig {
    @Sorter(sort = 0)
    public static double[] intakeRotations = {0.02, 0.1999, 0.397};
    @Sorter(sort = 1)
    public static double[] outakeRotations = {0.29, 0.47, 0.658};
    @Sorter(sort = 2)
    public static double threshold = 0.08;
    @Sorter(sort = 3)
    public static double AVG_VOLTAGE = 3.3;
    @Sorter(sort = 4)
    public static double delay = 200;
}
