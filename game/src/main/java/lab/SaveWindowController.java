package lab;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class SaveWindowController {

    private App app;
    private GameScene gameScene;

    @FXML
    private TextField nameField;

    @FXML
    private TextField descriptionField;

    @FXML
    private Button exit;

    public void setApp(App app, GameScene gameScene) {
        this.app = app;
        this.gameScene = gameScene;

        app.buttonHoverSound(exit);
    }

    @FXML
    private void onButtonSave() {
        app.buttonClickSound();

        String nickname = nameField.getText();
        String description = descriptionField.getText();

        if (nickname == null || nickname.trim().isEmpty()) {
            nickname = "Unknown Player";
        }
        if (description == null || description.trim().isEmpty()) {
            description = "-";
        }

        if (gameScene != null) {
            SaveManager.saveGame(gameScene, nickname, description);
        }

        try {
            app.switchToMenu();
        } catch (IOException e) {
            log.error("Error while switching to menu after saving: {}", e.getMessage(), e);
        }
    }
}