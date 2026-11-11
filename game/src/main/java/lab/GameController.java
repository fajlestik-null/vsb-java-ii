package lab;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.UncheckedIOException;

@Log4j2
public class GameController {

    private DrawingThread timer;
    private GameScene gameScene;
    private App app;
    private boolean gameOverTriggered = false;

    @FXML
    private Canvas canvas;

    @FXML
    private Label lives;

    @FXML
    private Label score;

    @FXML
    private Button save;

    @FXML
    void initialize() {
        canvas.setFocusTraversable(true);

        canvas.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (gameScene != null) {
                gameScene.keyPressed(e.getCode());
            }
        });

        canvas.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
            if (gameScene != null) {
                gameScene.keyReleased(e.getCode());
            }
        });
    }

    @FXML
    private void saveGame() {
        app.buttonClickSound();

        this.stop();

        try {
            app.switchToSaveWindow(gameScene);
        } catch (Exception e) {
            log.error("Failed to open save window", e);
        }
    }

    public void startGame(GameScene scene) {
        this.gameScene = scene;
        this.gameOverTriggered = false;

        Player p = gameScene.getPlayer();

        p.addLivesListener(this::changeLives);
        p.addScoreListener(this::changeScore);
        p.addDoublePointsListener(this::updateScoreColor);

        this.updateScoreColor(p.isDoublePointsActive());

        changeLives();
        changeScore();

        save.setDisable(true);

        PauseTransition btnDelay = new PauseTransition(Duration.seconds(3));
        btnDelay.setOnFinished(e -> save.setDisable(false));
        btnDelay.play();

        timer = new DrawingThread(canvas, gameScene);
        timer.start();
        canvas.requestFocus();
    }

    public void stop() {
        timer.stop();
    }

    public void changeScore() {
        Platform.runLater(() -> {
            if (gameScene != null) {
                score.setText("SCORE: " + (int) gameScene.getPlayer().getScore());
            }
        });
    }

    public void changeLives() {
        Platform.runLater(() -> {
            if (gameScene == null) {
                return;
            }

            lives.setText("LIVES: " + gameScene.getPlayer().getLives());

            if (!gameOverTriggered && gameScene.getPlayer().getLives() <= 0) {
                gameOverTriggered = true;
                log.info("Game Over triggered. Final score: {}", gameScene.getPlayer().getScore());
                stop();
                try {
                    app.switchToGameOver((int) gameScene.getPlayer().getScore());
                } catch (IOException e) {
                    throw new UncheckedIOException("Failed to load game over screen", e);
                }
            }
        });
    }

    private void updateScoreColor(boolean active) {
        Platform.runLater(() -> {
            if (active) {
                score.setTextFill(Color.YELLOW);
            } else {
                score.setTextFill(Color.WHITE);
            }
        });
    }

    public void setApp(App app)
    {
        this.app = app;

        app.buttonHoverSound(save);
    }
}
