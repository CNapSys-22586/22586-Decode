package org.firstinspires.ftc.teamcode.cnapsys.subsystems.indexer;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.configurables.annotations.Sorter;

@Configurable
public class indexerConfig {
    @Sorter(sort = 0)
    public static int[] rgb = {255, 0, 0, 255};
    @Sorter(sort = 1)
    public static double voltageRangeMin = 0.2;
    @Sorter(sort = 2)
    public static double voltageRangeMax = 3.1;
}
