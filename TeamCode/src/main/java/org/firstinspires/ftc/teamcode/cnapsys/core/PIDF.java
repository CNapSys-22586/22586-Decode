package org.firstinspires.ftc.teamcode.cnapsys.core;
import com.qualcomm.robotcore.util.ElapsedTime;

public class PIDF {
    private double p, i, d, f;
    private double integralSum = 0;
    private int lastError = 0;
    private ElapsedTime et = new ElapsedTime();

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

    private double sign(double x) {
        return (x < 0) ? -1 : 1;
    }

    public double update(int error) {
        double dt = et.seconds();
        et.reset(); // Reset timer for the next cycle

        // Integral accumulates error over time
        integralSum += error * dt;

        // Derivative (Rate of change of error)
        double derivative = (error - lastError) / dt;
        lastError = error;

        // Compute PIDF output
        return (p * error) + (i * integralSum) + (d * derivative) + (sign(error) * f);
    }

    public double getP() {
        return p;
    }
    public double getI() {
        return i;
    }
    public double getD() {
        return d;
    }
    public double getF() {
        return f;
    }
}

