package lab;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Log4j2
@ToString(callSuper = true)
public class PowerUpEntity extends WorldEntity {

    private final PowerUpType type;
    private double lifeTime = 10.0;

    public PowerUpEntity(GameScene gameScene, Point2D position, PowerUpType type) {
        super(gameScene, position, new Polygon(-10, 10, 10, 10, 0, -10));
        this.type = type;

        this.angularVelocity = 100;
    }

    @Override
    public Color getColor() {
        return type.getColor();
    }

    @Override
    public void simulate(double deltaTime) {
        applyRotationPhysics(deltaTime);
        move(deltaTime);
        applyDrag(deltaTime);

        lifeTime -= deltaTime;
        if (lifeTime <= 0)
        {
            gameScene.removeEntity(this);
        }
    }

    @Override
    public void hitBy(Collisionable other) {
        if (other instanceof Player player) {
            type.applyTo(player);
            playSound("audio/playerPowerUpPick.mp3");
            gameScene.removeEntity(this);
            log.debug("Player picked up power-up: {}", type);
        }
    }
}

