package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.util.List;

@Log4j2
public class Explosion extends WorldEntity {

    private Image explosionImage;
    private static final double MAX_LIFE_TIME = 0.5;
    private double currentLifeTime = 0;
    private final double size;
    private static final List<String> EXPLOSION_SOUNDS = List.of(
        "audio/explosionSmall.wav",
        "audio/explosionMedium.wav",
        "audio/explosionLarge.wav"
    );

    public Explosion(GameScene gameScene, Point2D position, double size) {
        super(gameScene, position, new Polygon(0, 0, 1, 0, 0, 1));
        this.size = size * 4;

        loadImage();
        playRandomExplosionSound();
    }

    private void loadImage() {
        this.explosionImage = ResourceManager.getImage(getClass(), "images/explosion.gif");

    }

    private void playRandomExplosionSound() {
        String randomSoundName = ResourceManager.getRandomElement(EXPLOSION_SOUNDS);

        double randomRate = 0.8 + Math.random() * 0.4;
        double volume = Math.clamp(this.size / 200.0, 0.3, 1.0);

        log.trace("Playing explosion sound: {} at volume: {}", randomSoundName, volume);

        playSound(randomSoundName, randomRate, volume);
    }

    @Override
    public boolean intersect(Polygon other) {
        return false;
    }

    @Override
    public Polygon getBoundingBox() {
        return this.shape;
    }

    @Override
    public void simulate(double deltaTime) {
        currentLifeTime += deltaTime;

        if (currentLifeTime >= MAX_LIFE_TIME) {
            gameScene.removeEntity(this);
        }
    }

    @Override
    public void drawInternal(GraphicsContext gc) {
        gc.translate(position.getX(), position.getY());

        if (explosionImage != null) {
            double opacity = 1.0 - (currentLifeTime / MAX_LIFE_TIME);
            gc.setGlobalAlpha(opacity);

            gc.drawImage(explosionImage, -size / 2, -size / 2, size, size);

            gc.setGlobalAlpha(1.0);
        }
    }

    @Override
    public Color getColor() {
        return Color.ORANGE;
    }

    @Override
    public void hitBy(Collisionable other) {
        /*Nothing*/
    }
}
