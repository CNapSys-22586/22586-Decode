package org.firstinspires.ftc.teamcode.cnapsys.core.utils;

import com.qualcomm.robotcore.util.ElapsedTime;

public class PIDF {
    private double p, i, d, f;
    private double integralSum = 0;
    private double lastError   = 0;
    private boolean firstRun   = true;
    private final ElapsedTime et = new ElapsedTime();

    public PIDF(double P, double I, double D, double F) {
        this.p = P;
        this.i = I;
        this.d = D;
        this.f = F;
    }

    public void setParams(double P, double I, double D, double F) {
        this.p = P;
        this.i = I;
        this.d = D;
        this.f = F;
    }

    /**
     * Resets integral, derivative seed, and the internal timer.
     * The PIDF class calls this automatically on the first update() after
     * construction, so you only need to call it manually when re-enabling
     * the controller after a pause (e.g. from Turret.enable()).
     */
    public void reset() {
        integralSum = 0;
        lastError   = 0;
        firstRun    = true;
        et.reset();
    }

    private double sign(double x) {
        return (x < 0) ? -1 : 1;
    }

    public double update(double error) {
        double dt = et.seconds();
        et.reset();

        // On the first call ever (or after reset()), dt is meaningless because
        // the timer has been running since construction / last reset with no
        // meaningful error to accumulate. Skip I and D entirely – only P and F
        // act, which is safe and bumpless.
        if (firstRun) {
            firstRun  = false;
            lastError = error;
            return (p * error) + (sign(error) * f);
        }

        // Guard against near-zero dt (two calls in the same scheduler tick)
        // to prevent a divide-by-zero in the derivative calculation.
        if (dt < 1e-6) dt = 1e-6;

        integralSum += error * dt;
        double derivative = (error - lastError) / dt;
        lastError = error;

        return (p * error) + (i * integralSum) + (d * derivative) + (sign(error) * f);
    }

    public double getP() { return p; }
    public double getI() { return i; }
    public double getD() { return d; }
    public double getF() { return f; }
}