package org.firstinspires.ftc.teamcode.cnapsys.core.interfaces;

import com.bylazar.telemetry.TelemetryManager;

import org.firstinspires.ftc.teamcode.cnapsys.core.vars.SubsystemData;

public interface Subsystem {
    boolean isBusy();
    void update(double deltaTime, TelemetryManager tm, SubsystemData data);
}
