package lab;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.extern.log4j.Log4j2;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Random;

@Log4j2
public class GameOverController {

    private App app;
    private int scoreToSave;
    private static final Random RANDOM = new Random();

    @FXML
    private Label finalScore;

    @FXML
    private Button exit;

    @FXML
    private TextField nameField;

    public void setFinalScore(int score) {
        scoreToSave = score;
        finalScore.setText("Your score: " + score);
    }

    private String normalizeName(String name) {
        name = name.replaceAll("(?i)(doc\\.|ing\\.|mgr\\.|ph\\.d\\.|phd|prof\\.|bc\\.)", "");

        name = name.replace(",", "");

        name = name.trim().replaceAll("\\s+", " ");

        return name.toLowerCase();
    }

    private boolean isWorker(String name) {
        String normalizedName = normalizeName(name);
        String[] nameSectioned = normalizedName.split(" ");

        if (nameSectioned.length < 2) {
            return false;
        }

        List<String> workers = app.getAcademicWorkers();

        return workers.stream()
                .map(this::normalizeName)
                .map(n -> n.split(" "))
                .anyMatch(workerSectioned ->
                        // Check if names match (normal or swapped)
                        (nameSectioned[0].equals(workerSectioned[0]) && nameSectioned[1].equals(workerSectioned[1])) ||
                                (nameSectioned[1].equals(workerSectioned[0]) && nameSectioned[0].equals(workerSectioned[1]))
                );
    }

    public void setApp(App app) {
        this.app = app;

        app.buttonHoverSound(exit);
    }

    @FXML
    private void onBackToMenu() {
        app.buttonClickSound();

        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            name = NameGenerator.getRandomNick();
        }

        ScoreDto score;

        if (isWorker(name)) {
            log.info("Academic worker detected: {}. Adding random bonus to score.", name);
            scoreToSave = scoreToSave + RANDOM.nextInt(10000);
        }

        try {
            String jsonPayload = String.format(
                    "{\"nickname\":\"%s\", \"scorePoints\":%d}",
                    name, scoreToSave
            );

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(java.net.URI.create("http://localhost:8080/api/scores"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        if (response.statusCode() == 200) {
                            log.info("Score has been successfully added to server.");
                        } else {
                            log.error("ERROR while saving to the server: {}", response.body());
                        }
                    });

            app.switchToMenu();

        } catch (Exception e) {
            log.error("Switch to menu or data transfer were not successful.", e);
        }
    }
}