package lab;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Log4j2
public class App extends Application {

    private GameController gameController;
    private Stage primaryStage;
    @Getter
    private final List<String> academicWorkers = new ArrayList<>();
    private MediaPlayer musicPlayer;
    private final List<String> oneTimePlay = List.of("audio/gameOverSound.mp3");
    private final URL cssUrl = getClass().getResource("application.css");

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        new Thread(this::loadAcademicWorkers).start();
        preloadResources();

        this.primaryStage = primaryStage;
        try {
            switchToMenu();
            primaryStage.show();
            primaryStage.setOnCloseRequest(this::exitProgram);
        } catch (Exception e) {
            log.error("Failed to start application", e);
        }
    }
    @Override
    public void stop() throws Exception {
        gameController.stop();
        super.stop();
    }
    private void exitProgram(WindowEvent evt) {
        System.exit(0);
    }

    public void switchToGame(GameScene loadedScene) throws IOException {
        if (musicPlayer != null) {
            musicPlayer.stop();
        }

        AudioClip countdownInfo = ResourceManager.getAudioClip(getClass(), "audio/gameStartCountdown.mp3");
        if (countdownInfo != null) {
            countdownInfo.play();
        }
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("gameWindow.fxml"));
        Parent root = gameLoader.load();

        gameController = gameLoader.getController();
        gameController.setApp(this);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(cssUrl).toString());
        primaryStage.setScene(scene);

        gameController.startGame(Objects.requireNonNullElseGet(loadedScene,
            () -> new GameScene(800, 600)));

        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> playMusic("audio/gameMusic.mp3"));
        delay.play();
    }

    public void switchToMenu() throws IOException {
        playMusic("audio/mainMenuMusic.mp3");
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("gameMainMenu.fxml"));
        Parent root = menuLoader.load();
        MainMenuController menuController = menuLoader.getController();
        menuController.setApp(this);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(cssUrl).toString());
        primaryStage.setScene(scene);
    }
    public void switchToGameOver(int score) throws IOException {
        playMusic(oneTimePlay.getFirst());
        FXMLLoader gameOverLoader = new FXMLLoader(getClass().getResource("gameOver.fxml"));
        Parent root = gameOverLoader.load();
       GameOverController gameOverController = gameOverLoader.getController();
        gameOverController.setApp(this);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(cssUrl).toString());
        primaryStage.setScene(scene);
        gameOverController.setFinalScore(score);
    }

    public void switchToScoreboard() throws IOException {
        playMusic("audio/scoreboardMusic.mp3");
        FXMLLoader scoreboardLoader = new FXMLLoader(getClass().getResource("scoreboard.fxml"));
        Parent root = scoreboardLoader.load();
        ScoreboardController scoreboardController = scoreboardLoader.getController();
        scoreboardController.setApp(this);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(cssUrl).toString());
        primaryStage.setScene(scene);
        scoreboardController.loadScores();
    }

    public void switchToSaveWindow(GameScene gameScene) throws IOException {
        playMusic("audio/mainMenuMusic.mp3");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("saveWindow.fxml"));
        Parent root = loader.load();

        SaveWindowController controller = loader.getController();
        controller.setApp(this, gameScene);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(cssUrl).toString());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void switchToSaveBrowser() throws IOException {
        playMusic("audio/scoreboardMusic.mp3");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("saveBrowser.fxml"));
        Parent root = loader.load();

        SaveBrowserController controller = loader.getController();
        controller.setApp(this);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(cssUrl).toString());
        primaryStage.setScene(scene);
    }

    public void startGameFromSave(GameSaveDto save) {
        try {
            GameScene loadedScene = new GameScene(800, 600);

            Player player = loadedScene.getPlayer();

            player.setLives(save.lives());
            player.setScore(save.currentScore());

            player.setPosition(new Point2D(save.positionX(), save.positionY()));

            if (save.hasShield()) {
                player.activateShield();
            }
            player.setRapidFireTimer(save.rapidFireTimer());
            player.setTripleShotTimer(save.tripleShotTimer());
            player.setDoublePointTimer(save.doublePointTimer());

            loadedScene.setAsteroidSpawnInterval(save.asteroidSpawnInterval());
            loadedScene.setDifficultyTimer(save.difficultyTimer());

            switchToGame(loadedScene);

        } catch (Exception e) {
            log.error("Failed to start game from save", e);
        }
    }

    private void loadAcademicWorkers() {
            String url = "https://www.fei.vsb.cz/460/cs/kontakt/lide/";
            try (BufferedReader in = new BufferedReader(new InputStreamReader(new URI(url).toURL().openStream()))) {
                String fileContent = in.lines().collect(Collectors.joining("\n"));

                Pattern p = Pattern.compile("<a[^>]*>([^<]+)</a>");
                Matcher m = p.matcher(fileContent);

                while (m.find()) {
                    String name = m.group(1).trim();
                    String[] checker = name.split(" ");

                    if (checker.length >= 2 && Character.isUpperCase(checker[1].charAt(0))) {
                        synchronized (academicWorkers) {
                            academicWorkers.add(name);
                            log.trace("Added academic worker: {}", name);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("Failed to load academic workers", e);
            }
    }

    private void preloadResources() {
        log.info("Preloading resources...");

        Font.loadFont(getClass().getResourceAsStream("fonts/ARCADE_FULL.ttf"), 43);

        ResourceManager.getImage(getClass(), "images/explosion.gif");
        ResourceManager.getImage(getClass(), "images/thrustFlame.gif");

        ResourceManager.getMedia(getClass(), "audio/mainMenuMusic.mp3");
        ResourceManager.getMedia(getClass(), "audio/gameMusic.mp3");
        ResourceManager.getMedia(getClass(), "audio/scoreboardMusic.mp3");
        ResourceManager.getMedia(getClass(), oneTimePlay.getFirst());

        ResourceManager.getAudioClip(getClass(), "audio/buttonHover.mp3");
        ResourceManager.getAudioClip(getClass(), "audio/buttonClick.mp3");
        ResourceManager.getAudioClip(getClass(), "audio/explosionSmall.wav");
        ResourceManager.getAudioClip(getClass(), "audio/explosionMedium.wav");
        ResourceManager.getAudioClip(getClass(), "audio/explosionLarge.wav");
        ResourceManager.getAudioClip(getClass(), "audio/gameStartCountdown.mp3");
        ResourceManager.getAudioClip(getClass(), "audio/playerHeal.mp3");
        ResourceManager.getAudioClip(getClass(), "audio/playerHit.mp3");
        ResourceManager.getAudioClip(getClass(), "audio/playerPowerUpPick.mp3");
        ResourceManager.getAudioClip(getClass(), "audio/playerShieldHit.mp3");
        ResourceManager.getAudioClip(getClass(), "audio/playerThrust.wav");
        ResourceManager.getAudioClip(getClass(), "audio/singleShot.wav");
        ResourceManager.getAudioClip(getClass(), "audio/TripleShot.mp3");

        log.info("Resources preloaded successfully.");
    }

    private void playMusic(String name) {
        log.debug("Attempting to play music track: {}", name);
        Media media = ResourceManager.getMedia(getClass(), name);
        if (media == null){
            return;
        }

        if (musicPlayer != null) {
            Media current = musicPlayer.getMedia();
            if (current != null && current.getSource().equals(media.getSource())) {
                return;
            }
            musicPlayer.stop();
        }

        musicPlayer = new MediaPlayer(media);
        musicPlayer.setVolume(0.2);

        if (name.contains(oneTimePlay.getFirst())) {
            musicPlayer.setCycleCount(1);
        } else {
            musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        }

        musicPlayer.play();
    }

    public void buttonHoverSound(Button btn) {
        btn.setOnMouseEntered(event -> {
            AudioClip hoverSound = ResourceManager.getAudioClip(getClass(), "audio/buttonHover.mp3");
            if (hoverSound != null) {
                hoverSound.play();
            }
        });
    }

    public void buttonClickSound() {
        AudioClip clickSound = ResourceManager.getAudioClip(getClass(), "audio/buttonClick.mp3");
        if (clickSound != null) {
            clickSound.play();
        }
    }
}
