package lab;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Random;

public class PowerUpAsteroid extends Asteroid{

    private final Random rand = new Random();
    private final List<PowerUpType> types = List.of(PowerUpType.values());

    public PowerUpAsteroid(GameScene gameScene, Point2D position, double size) {
        super(gameScene, position, size);
    }

    @Override
    public Color getColor() {
        return Color.GOLD;
    }

    @Override
    public void hitBy(Collisionable other) {
        if (other instanceof Bullet bullet) {
            gameScene.removeEntity(this);
            gameScene.removeEntity(bullet);
            createExplosion();

            gameScene.getPlayer().addScore(points);
            gameScene.getPlayer().fireScore();

            spawnPowerUp();
        } else if (other instanceof Player) {
            super.hitBy(other);
            createExplosion();
            spawnPowerUp();
        }
    }

    private void spawnPowerUp() {
        PowerUpEntity entity = new PowerUpEntity(gameScene, this.position, types.get(rand.nextInt(types.size())));
        gameScene.addEntity(entity);
    }
}

