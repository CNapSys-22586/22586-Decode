package org.firstinspires.ftc.teamcode.cnapsys.core;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.cnapsys.subsystems.indexer.indexer;
import org.firstinspires.ftc.teamcode.cnapsys.subsystems.intake.intake;
import org.firstinspires.ftc.teamcode.cnapsys.subsystems.pusher.pusher;

public class robot {
    private intake intake;
    private pusher pusher;
    private indexer indexer;
    //long lastTime;

    public robot() {
        //need to add the names of all the motors and servos
        intake = new intake(hardwareMap.get(DcMotor.class, "Intake motor"));
        pusher = new pusher(hardwareMap.get(Servo.class, "Pusher servo"), hardwareMap.get(AnalogInput.class, "Pusher servo feedback"));
        indexer = new indexer(hardwareMap.get(Servo.class, "Indexer servo"), hardwareMap.get(AnalogInput.class, "Indexer servo feedback"), hardwareMap.get(ColorSensor.class, "Color sensor"));
        //lastTime = System.currentTimeMillis();
    }

    void update(long deltaTime) {
        //long currentTime = System.currentTimeMillis();
        //long deltaTime = currentTime - lastTime;
        //lastTime = currentTime;

        intake.update(deltaTime);
        pusher.update(deltaTime);
        indexer.update(deltaTime);
    }
}
