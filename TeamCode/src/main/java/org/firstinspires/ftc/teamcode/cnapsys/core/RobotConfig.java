package org.firstinspires.ftc.teamcode.cnapsys.core;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.configurables.annotations.Sorter;
import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.cnapsys.core.vars.Alliance;

@Configurable
public class RobotConfig {
    @Sorter(sort=1)
    public static Pose GOAL_POSE = new Pose(6, 144, 0);
    @Sorter(sort=2)
    public static Pose ROBOT_POSE = new Pose(38.65, 32.34, Math.toRadians(90)); //RED PARK ZONE
    @Sorter(sort=3)
    public static Pose PARK_POSE = new Pose(38.442, 32.795, Math.toRadians(90)); //RED PARK ZONE
    @Sorter(sort=4)
    public static double MAX_FOLLOWER_SPEED = 1;
    @Sorter(sort=5)
    public static double SLOW_FOLLOWER_SPEED = 0.6;
    @Sorter(sort=6)
    public static double TOP_Y_THRESHOLD = 40;
    @Sorter(sort=7)
    public static double DOWN_Y_THRESHOLD = 34;
    @Sorter(sort=8)
    public static double DOWN_X_THRESHOLD_LEFT = 41;
    @Sorter(sort=9)
    public static double DOWN_X_THRESHOLD_RIGHT = 105;
    @Sorter(sort=10)
    public static boolean COMPENSATE_FOR_VELOCITY = false;
}
