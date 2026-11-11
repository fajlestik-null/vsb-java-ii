package lab;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Log4j2
public class GameScene implements Serializable {

    private final double width;
    private final double height;
    private Dimension2D size;
    private final List<DrawableSimulable> entities = new ArrayList<>();
    private final List<DrawableSimulable> entitiesToRemove = new ArrayList<>();
    private List<DrawableSimulable> entitiesToAdd = new ArrayList<>();
    @Getter
    private final Player player;
    private  Set<KeyCode> pressedKeys = new HashSet<>();
    private static final Random RANDOM = new Random();
    private double asteroidSpawnTimer = 0;
    @Getter
    @Setter
    private double asteroidSpawnInterval = 2.5;
    @Getter
    @Setter
    private double difficultyTimer = 0;
    @Setter
    private double startDelayTimer = 0;
    private Comparator<DrawableSimulable> comparator;
    private Background background;


    public GameScene(double width, double height){
        this.width = width;
        this.height = height;

        size = new Dimension2D(width, height);
        this.background = new Background(width, height);

        setStartDelayTimer(3.0);

        player = new Player(this, new Point2D(400,300));
        entities.add(player);

        for(int i = 0; i < 10; i++) {
            spawnAsteroid();
        }

        initComparator();
        entities.sort(comparator);

        entities.addAll(entitiesToAdd);
        entitiesToAdd.clear();
        entities.sort(comparator);
    }

    private void initComparator() {
        comparator = ((Comparator<DrawableSimulable>) (o1, o2) -> {
            if (o1 == o2) {
                return 0;
            }
            if (o1 instanceof Explosion || o2 instanceof Player || o1 instanceof Bullet) {
                return -1;
            }
            if (o2 instanceof Explosion || o1 instanceof Player || o2 instanceof Bullet) {
                return 1;
            }
            if (o1 instanceof Asteroid a1 && o2 instanceof Asteroid a2) {
                return Double.compare(a1.getSize(), a2.getSize());
            }
            return 0;
        }).reversed();
    }

    public Dimension2D getSize() {
        if (size == null) {
            size = new Dimension2D(width, height);
        }
        return size;
    }

    public void draw(GraphicsContext gc) {
        if (background != null) {
            background.draw(gc);
        }
        entities.stream().forEach(entity -> entity.draw(gc));

        if (startDelayTimer > 0) {
            gc.save();
            gc.setFill(Color.GRAY);
            gc.setFont(new Font("8-bit Arcade In", 200));
            gc.setTextAlign(TextAlignment.CENTER);

            String text = String.valueOf((int) Math.ceil(startDelayTimer));
            gc.fillText(text, width / 2, height / 2);
            gc.restore();
        }
    }

    public void simulate(double deltaTime) {
        if (startDelayTimer > 0) {
            startDelayTimer -= deltaTime;
            return;
        }

        entities.stream().forEach(
            entity -> entity.simulate(deltaTime)
        );

        checkCollisions();

        asteroidSpawnTimer += deltaTime;
        if (asteroidSpawnTimer >= asteroidSpawnInterval) {
            asteroidSpawnTimer = 0;
            spawnAsteroid();
        }

        double difficultyIncreaseInterval = 10;
        difficultyTimer += deltaTime;
        if (difficultyTimer >= difficultyIncreaseInterval) {
            difficultyTimer = 0;
            asteroidSpawnInterval = Math.max(0.8, asteroidSpawnInterval - 0.1);
            log.info("Difficulty increased! New spawn interval: {}", asteroidSpawnInterval);
        }

        if (!entitiesToAdd.isEmpty()) {
            entities.addAll(entitiesToAdd);
            entitiesToAdd.clear();
            entities.sort(comparator);
        }
    }

    private void spawnAsteroid() {
        double asteroidSize = 10 + RANDOM.nextDouble(50);
        double x = RANDOM.nextDouble() * getSize().getWidth();
        double y = RANDOM.nextDouble() * getSize().getHeight();

        Asteroid asteroid;
        if (RANDOM.nextDouble() < 0.05) {
            asteroid = new PowerUpAsteroid(this, new Point2D(x, y), asteroidSize);
        } else {
            asteroid = new Asteroid(this, new Point2D(x, y), asteroidSize);
        }

        if (asteroid.getPosition().distance(player.getPosition()) > 150) {
            this.addEntity(asteroid);
        }

        log.debug("Spawning asteroid at [{}, {}] with size {}", x, y, asteroidSize);
    }

    private void checkCollisions() {
        for (int i = 0; i < entities.size(); i++) {
            DrawableSimulable e1 = entities.get(i);

            for (int j = i + 1; j < entities.size(); j++) {
                DrawableSimulable e2 = entities.get(j);

                if (e1 instanceof Collisionable c1 && e2 instanceof Collisionable c2 && c1.intersect(c2.getBoundingBox())) {
                        c1.hitBy(c2);
                        c2.hitBy(c1);
                }
            }
        }

        entities.removeAll(entitiesToRemove);
        entitiesToRemove.clear();
    }

    public void addEntity(DrawableSimulable entity) {
        entitiesToAdd.add(entity);
    }

    public void keyPressed(KeyCode key) {
        pressedKeys.add(key);
    }

    public void keyReleased(KeyCode key) {
        pressedKeys.remove(key);
    }

    public boolean isKeyPressed(KeyCode key) {
        return pressedKeys.contains(key);
    }

    public void removeEntity(WorldEntity worldEntity) {
        entitiesToRemove.add(worldEntity);
    }
}
