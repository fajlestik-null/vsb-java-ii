package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.util.LinkedList;
import java.util.List;

@Log4j2
public class Player extends WorldEntity {

    @Getter
    @Setter
    private int lives = 3;
    @Getter
    @Setter
    private double score = 0;
    private double timeSinceLastShot = 0;
    @Getter
    @Setter
    private boolean hasShield = false;
    @Getter
    @Setter
    private double rapidFireTimer = 0;
    @Getter
    @Setter
    private double doublePointTimer = 0;
    @Getter
    @Setter
    private double tripleShotTimer = 0;
    private double thrustSoundTimer = 0;
    private WeaponStrategy weapon = new SingleShotWeapon();
    private Image flameImage;
    private boolean isThrusting;
    private List<ScoreListener> scoreListeners = new LinkedList<>();
    private List<LivesListener> livesListeners = new LinkedList<>();
    private List<DoublePointsListener> doublePointsListeners = new LinkedList<>();


    public Player(GameScene gameScene,  Point2D position) {
        super(gameScene, position, new Polygon(-10,-10,20,0,-10,10));
        loadImage();
    }

    @Override
    public Color getColor(){
        if (hasShield) {
            return Color.CYAN;
        }
        if (tripleShotTimer > 0) {
            return Color.MAGENTA;
        }
        if (rapidFireTimer > 0) {
            return Color.ROSYBROWN;
        }
        return Color.WHITE;
    }
    @Override
    public void drawInternal(GraphicsContext gc) {
        super.drawInternal(gc);

        if (isThrusting && flameImage != null) {
            gc.save();

            gc.translate(17, 0);

            gc.rotate(90);

            double flameWidth = 50;
            double flameHeight = 50;

            gc.drawImage(flameImage, -flameWidth / 2, 0, flameWidth, flameHeight);

            gc.restore();
        }

        gc.setFill(Color.BLUE);

        double[] xPoints = new double[]{ 5, 20, 5 };
        double[] yPoints = new double[]{ -4, 0,  4 };

        gc.fillPolygon(xPoints, yPoints, 3);
    }

    @Override
    public void simulate(double deltaTime) {
        timeSinceLastShot += deltaTime;

        isThrusting = gameScene.isKeyPressed(KeyCode.W);
        if (isThrusting) {
            accelerate(deltaTime);

            thrustSoundTimer -= deltaTime;
            if (thrustSoundTimer <= 0) {
                playSound("audio/playerThrust.wav", 1.5);
                thrustSoundTimer = 0.2;
            }
        } else {
            thrustSoundTimer = 0;
        }

        if (gameScene.isKeyPressed(KeyCode.A)) {
            rotateLeft(deltaTime);
        }
        if (gameScene.isKeyPressed(KeyCode.D)) {
            rotateRight(deltaTime);
        }

        if (rapidFireTimer > 0) {
            rapidFireTimer -= deltaTime;
        }
        if (doublePointTimer > 0) {
            doublePointTimer -= deltaTime;

            if (doublePointTimer <= 0) {
                doublePointTimer = 0;
                fireDoublePointsChanged(false);
            }
        }

        if (tripleShotTimer > 0) {
            tripleShotTimer -= deltaTime;
            if (tripleShotTimer <= 0) {
                weapon = new SingleShotWeapon();
            }
        }

        double currentDelay = 1.0;
        if (rapidFireTimer > 0) {
            currentDelay = 0.2;
        }

        if (gameScene.isKeyPressed(KeyCode.SPACE) && timeSinceLastShot >= currentDelay) {
                shoot();
                timeSinceLastShot = 0;
        }

        move(deltaTime);
        applyDrag(deltaTime);
    }

    private void loadImage(){
        this.flameImage = ResourceManager.getImage(getClass(), "images/thrustFlame.gif");
    }

    private void shoot() {
        weapon.shoot(gameScene, position, shape.getRotate());
    }

    @Override
    public void hitBy(Collisionable other) {
        /*Nothing*/
    }

    public boolean isDoublePointsActive() {
        return doublePointTimer > 0;
    }

    public void decreaseLives() {
        if (hasShield) {
            hasShield = false;
            playSound("audio/playerShieldHit.mp3");
            return;
        }
        lives--;
        playSound("audio/playerHit.mp3");
        fireLives();
    }

    public void increaseLives() {
        lives++;
        playSound("audio/playerHeal.mp3");
        fireLives();
    }

    public void activateShield() {
        log.debug("Player activated SHIELD.");
        this.hasShield = true;
    }

    public void activateTripleShot() {
        log.debug("Player activated TRIPLE SHOT.");
        this.tripleShotTimer += 10.0;
        this.weapon = new TripleShotWeapon();
    }

    public void activateRapidFire() {
        log.debug("Player activated RAPID FIRE.");
        this.rapidFireTimer += 5.0;
    }

    public void activateDoublePoints() {
        log.debug("Player activated DOUBLE POINTS.");
        boolean wasActive = isDoublePointsActive();
        doublePointTimer += 15.0;
        if (!wasActive) {
            fireDoublePointsChanged(true);
        }
    }

    public void addDoublePointsListener(DoublePointsListener listener) {
        if (doublePointsListeners == null){
            doublePointsListeners = new LinkedList<>();
        }
        doublePointsListeners.add(listener);
    }

    protected void fireDoublePointsChanged(boolean active) {
        if (doublePointsListeners == null) {
            return;
        }
        for (DoublePointsListener listener : doublePointsListeners) {
            listener.onDoublePointsChanged(active);
        }
    }

    public void addScore(double score) {
        if (doublePointTimer > 0) {
            score *= 2;
        }

        this.score += score;
        fireScore();
    }

    public void addScoreListener(ScoreListener scoreListener)
    {
        if (scoreListeners == null) {
            scoreListeners = new LinkedList<>();
        }
        scoreListeners.add(scoreListener);
    }

    protected void fireScore() {
        if (scoreListeners == null) {
            return;
        }
        for (ScoreListener scoreListener : scoreListeners) {
            scoreListener.scoreChanged();
        }
    }

    public void addLivesListener(LivesListener livesListener) {
        if (livesListeners == null) {
            livesListeners = new LinkedList<>();
        }
        livesListeners.add(livesListener);
    }

    protected void fireLives() {
        if (livesListeners == null) {
            return;
        }
        for (LivesListener livesListener : livesListeners) {
            livesListener.livesChanged();
        }
    }
}
