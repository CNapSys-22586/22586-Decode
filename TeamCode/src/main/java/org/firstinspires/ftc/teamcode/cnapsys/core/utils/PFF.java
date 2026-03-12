package org.firstinspires.ftc.teamcode.cnapsys.core.utils;

public class PFF {
    private double kp, kv, ks;
    public PFF (double kp, double kv, double ks) {
        this.kp = kp;
        this.kv = kv;
        this.ks = ks;
    }

    public double update(double target, double current) {
        return (kv * target) + kp * (target - current) + ks;
    }

    public void setParams(double kp, double kv, double ks) {
        this.kp = kp;
        this.kv = kv;
        this.ks = ks;
    }
}
