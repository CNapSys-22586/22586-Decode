package org.firstinspires.ftc.teamcode.cnapsys.teleOps.teleOps;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.cnapsys.core.RobotConfig;
import org.firstinspires.ftc.teamcode.cnapsys.core.utils.PFF;
import org.firstinspires.ftc.teamcode.cnapsys.core.utils.PIDF;
import org.firstinspires.ftc.teamcode.cnapsys.core.vars.SubsystemData;
import org.firstinspires.ftc.teamcode.cnapsys.subsystems.Intake.Intake;
import org.firstinspires.ftc.teamcode.cnapsys.subsystems.Shooter.ShooterConfig;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;


@TeleOp(name="CALIB SHOOTER")
public class shooterCalibration extends OpMode {
    private DcMotorEx motorA;
    private DcMotorEx motorB;
    private Servo hoodServo;
    private Intake intake;
    private TelemetryManager tm;
    private Follower follower;
    private final PFF pff = new PFF(ShooterConfig.KP, ShooterConfig.KV, ShooterConfig.KS);

    @Override
    public void init() {
        intake = new Intake(hardwareMap.get(DcMotorEx.class, "intakeMotor"));
        motorA = hardwareMap.get(DcMotorEx.class, "shooterMotorA");
        motorB = hardwareMap.get(DcMotorEx.class, "shooterMotorB");
        hoodServo = hardwareMap.get(Servo.class, "hoodServo");
        motorA.setDirection(DcMotorSimple.Direction.FORWARD);
        motorB.setDirection(DcMotorSimple.Direction.REVERSE);
        motorA.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        motorB.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        tm = PanelsTelemetry.INSTANCE.getTelemetry();
        follower = Constants.createFollower(hardwareMap);
        follower.setPose(RobotConfig.ROBOT_POSE);
        follower.setMaxPower(RobotConfig.MAX_FOLLOWER_SPEED);
        follower.startTeleopDrive();
    }

    @Override
    public void loop() {
        follower.setTeleOpDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
        follower.update();
        if (gamepad1.dpadDownWasPressed()) intake.disable();
        else if (gamepad1.dpadUpWasPressed()) intake.enable();
        if (gamepad1.squareWasPressed()) shooterCalibrationConfig.enabled = false;
        else if (gamepad1.triangleWasPressed()) shooterCalibrationConfig.enabled = true;
        pff.setParams(ShooterConfig.KP, ShooterConfig.KV, ShooterConfig.KS);
        hoodServo.setPosition((shooterCalibrationConfig.TARGET_ANGLE * ShooterConfig.GEAR_RATIO) / 255 - 1.180);
        double power = pff.update(shooterCalibrationConfig.TARGET_RPM, motorA.getVelocity());
        if (shooterCalibrationConfig.enabled) {
            motorA.setPower(power);
            motorB.setPower(power);
        }
        else {
            motorA.setPower(0);
            motorB.setPower(0);
        }
        intake.update(0, tm, new SubsystemData());
        tm.addData("TARGET TPS:", shooterCalibrationConfig.TARGET_RPM);
        tm.addData("CURRENT TPS:", motorA.getVelocity());
        tm.addData("DISTANCE:", follower.getPose().distanceFrom(RobotConfig.GOAL_POSE));
        tm.update(telemetry);
    }
}
