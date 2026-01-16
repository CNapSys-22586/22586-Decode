package org.firstinspires.ftc.teamcode.cnapsys.subsystems.intake;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.configurables.annotations.Sorter;

@Configurable
public class intakeConfig {
    //this value needs calibrating
    @Sorter(sort  = 0)
    public static int RPM = 15;
    @Sorter(sort = 1)
    public static double kP = 0.0;
    @Sorter(sort = 2)
    public static double kI = 0.0;
    @Sorter(sort = 3)
    public static double kD = 0.0;
    @Sorter(sort = 4)
    public static double kF = 0.0;
    //this value probably needs calibrating
    @Sorter(sort = 5)
    public static int threshold = 10;
}
