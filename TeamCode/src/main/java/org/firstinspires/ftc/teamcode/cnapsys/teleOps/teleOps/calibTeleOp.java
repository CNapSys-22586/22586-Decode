package org.firstinspires.ftc.teamcode.cnapsys.teleOps.teleOps;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.cnapsys.core.utils.colorSensor;
import org.firstinspires.ftc.teamcode.cnapsys.subsystems.indexer.indexerConfig;
import org.firstinspires.ftc.teamcode.cnapsys.subsystems.intake.intake;
import org.firstinspires.ftc.teamcode.cnapsys.subsystems.pusher.pusherConfig;

@TeleOp(name="ColorCalib")
public class calibTeleOp extends OpMode {

    public colorSensor sensor;
    public static TelemetryManager telemetryM;
    public static AnalogInput pusherInput, indexerInput;
    public static Servo servo, pusherServo;

    @Override
    public void init() {
        pusherServo = hardwareMap.get(Servo.class, "pusherServo");
        sensor = new colorSensor(hardwareMap.get(RevColorSensorV3.class, "colorSensor"), 5);
        servo = hardwareMap.get(Servo.class, "indexerServo");
        pusherInput = hardwareMap.get(AnalogInput.class, "pusherServoAnalog");
        indexerInput = hardwareMap.get(AnalogInput.class, "indexerServoAnalog");
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
    }

    @Override
    public void loop() {
        if (gamepad1.rightBumperWasPressed()) pusherServo.setPosition(pusherConfig.idlePosition);
        if (gamepad1.leftBumperWasPressed()) pusherServo.setPosition(pusherConfig.engagedPosition);
        if (gamepad1.squareWasPressed()) servo.setPosition(indexerConfig.intakeRotations[0]);
        if (gamepad1.triangleWasPressed()) servo.setPosition(indexerConfig.intakeRotations[1]);
        if (gamepad1.circleWasPressed()) servo.setPosition(indexerConfig.intakeRotations[2]);
        if (gamepad1.dpadUpWasPressed()) servo.setPosition(indexerConfig.outakeRotations[0]);
        if (gamepad1.dpadRightWasPressed()) servo.setPosition(indexerConfig.outakeRotations[1]);
        if (gamepad1.dpadDownWasPressed()) servo.setPosition(indexerConfig.outakeRotations[2]);
        telemetryM.debug("R:", sensor.getRawColor().R);
        telemetryM.debug("G:", sensor.getRawColor().G);
        telemetryM.debug("B:", sensor.getRawColor().B);
        telemetryM.debug("Color: ", sensor.getColor());
        telemetryM.debug("Object Detected: ", sensor.isObjectDetected());
        telemetryM.debug("Object Distance: ", sensor.getDistance());
        telemetryM.debug("pusher input: ", pusherInput.getVoltage());
        telemetryM.debug("pusher pos: ",  1 - (pusherInput.getVoltage() / 3.3));
        telemetryM.debug("pusher pos inverted: ",  (pusherInput.getVoltage() / 3.3));
        telemetryM.debug("indexer input: ", indexerInput.getVoltage());
        telemetryM.update(telemetry);
    }
}
