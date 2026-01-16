package org.firstinspires.ftc.teamcode.cnapsys.core;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.cnapsys.subsystems.indexer.indexer;
import org.firstinspires.ftc.teamcode.cnapsys.subsystems.intake.intake;
import org.firstinspires.ftc.teamcode.cnapsys.subsystems.pusher.pusher;
import org.firstinspires.ftc.teamcode.cnapsys.subsystems.turret.turret;

enum ShootingState {
    IDLE,
    SELECTING_BALL,
    PUSHING,
    SHOOTING,
    MOVING_TO_EMPTY
}

public class robot {
    private intake intake;
    private pusher pusher;
    private indexer indexer;
    private turret turret;

    private ShootingState shootingState;

    public robot() {
        //need to add the names of all the motors and servos
        intake = new intake(hardwareMap.get(DcMotor.class, "Intake motor"));
        pusher = new pusher(hardwareMap.get(Servo.class, "Pusher servo"), hardwareMap.get(AnalogInput.class, "Pusher servo feedback"));
        indexer = new indexer(hardwareMap.get(Servo.class, "Indexer servo"), hardwareMap.get(AnalogInput.class, "Indexer servo feedback"), hardwareMap.get(ColorSensor.class, "Color sensor"));
        turret = new turret();
    }

    public void shootPurple() {
        shootingState = ShootingState.SELECTING_BALL;
        indexer.selectPurple();
        intake.toggleOff();
    }

    public void shootGreen() {
        shootingState = ShootingState.SELECTING_BALL;
        indexer.selectGreen();
        intake.toggleOff();
    }

    void update(long deltaTime) {
        intake.update(deltaTime);
        pusher.update(deltaTime);
        indexer.update(deltaTime);
        turret.update(deltaTime);

        switch (shootingState) {
            case IDLE:
                if (indexer.isFull()) intake.toggleOff();
                else intake.toggleOn();
                break;
            case SELECTING_BALL:
                if (!indexer.isBusy()) {
                    shootingState = ShootingState.PUSHING;
                    pusher.activate();
                }
                break;
            case PUSHING:
                if (!pusher.isBusy()) {
                    shootingState = ShootingState.SHOOTING;
                    turret.shoot();
                }
                break;
            case SHOOTING:
                if (!turret.isBusy()) {
                    shootingState = ShootingState.MOVING_TO_EMPTY;
                    indexer.selectNone();
                }
                break;
            case MOVING_TO_EMPTY:
                if (!indexer.isBusy()) {
                    shootingState = ShootingState.IDLE;
                    intake.toggleOn();
                }
                break;
        }
    }
}
