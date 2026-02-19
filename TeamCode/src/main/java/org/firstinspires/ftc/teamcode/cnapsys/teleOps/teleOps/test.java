package org.firstinspires.ftc.teamcode.cnapsys.teleOps.teleOps;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="Manual Test")
public class test extends OpMode {
    private DcMotor motor, shooterMotorA, shooterMotorB;

    @Override
    public void init() {
        motor = hardwareMap.get(DcMotor.class, "motor");
        shooterMotorA = hardwareMap.get(DcMotor.class, "shooterMotorA");
        shooterMotorA.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterMotorB = hardwareMap.get(DcMotor.class, "shooterMotorB");
        shooterMotorB.setDirection(DcMotorSimple.Direction.FORWARD);
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
        motor.setPower(1);
        shooterMotorA.setPower(1);
        shooterMotorB.setPower(1);
    }
}
