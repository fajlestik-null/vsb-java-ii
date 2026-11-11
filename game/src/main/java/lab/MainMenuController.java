package lab;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.UncheckedIOException;

@Log4j2
public class MainMenuController {

    private App app;

    @FXML
    private Button play;

    @FXML
    private Button load;

    @FXML
    private Button scoreboard;

    @FXML
    private void startGame() {
        app.buttonClickSound();
        try {
            app.switchToGame(null);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load game screen", e);
        }
    }

    @FXML
    private void onLoadGame() {
        app.buttonClickSound();

        try {
            app.switchToSaveBrowser();
        } catch (Exception e) {
            log.error("Save can't be loaded correctly", e);
        }
    }

    @FXML
    private void openScoreboard()
    {
        app.buttonClickSound();
       try {
            app.switchToScoreboard();
       } catch (IOException e) {
           throw new UncheckedIOException("Failed to load scoreboard", e);
       }
    }

    public void setApp(App app) {
        this.app = app;

        app.buttonHoverSound(play);
        app.buttonHoverSound(load);
        app.buttonHoverSound(scoreboard);
    }
}
