package org.firstinspires.ftc.teamcode.cnapsys.core.interfaces;

import com.bylazar.telemetry.TelemetryManager;

public interface subsystem {
    boolean isBusy();
    void update(double deltaTime, TelemetryManager tm);
}
