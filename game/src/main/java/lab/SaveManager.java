package lab;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.geometry.Point2D;
import lombok.extern.log4j.Log4j2;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

@Log4j2
public class SaveManager {
    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private static final HttpClient client = HttpClient.newHttpClient();

    private SaveManager() {
        /*Nothing*/
    }

    public static void saveGame(GameScene scene, String nickname, String description) {
        try {
            Player p = scene.getPlayer();

            GameSaveDto dto = new GameSaveDto(
                    nickname,
                    LocalDateTime.now(),
                    description,
                    p.getLives(),
                    (int) p.getScore(),
                    p.getPosition().getX(),
                    p.getPosition().getY(),
                    p.isHasShield(),
                    p.getRapidFireTimer(),
                    p.getTripleShotTimer(),
                    p.getDoublePointTimer(),
                    scene.getAsteroidSpawnInterval(),
                    scene.getDifficultyTimer()
            );

            mapper.registerModule(new JavaTimeModule());

            String jsonPayload = mapper.writeValueAsString(dto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/saves"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        if (response.statusCode() == 200) {
                            log.info("Game successfully saved on server for: {}", nickname);
                        } else {
                            log.error("Failed to save. Status: {}", response.statusCode());
                        }
                    });

        } catch (Exception e) {
            log.error("Error while saving your game: {}", e.getMessage(), e);
        }
    }

    public static GameScene loadGame(String nickname) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/saves/" + nickname))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<GameSaveDto> saves = mapper.readValue(response.body(), new TypeReference<List<GameSaveDto>>() {});

                if (saves.isEmpty()) {
                    log.info("No save for player {} found.", nickname);
                    return null;
                }

                GameSaveDto latestSave = saves.get(saves.size() - 1);

                GameScene loadedScene = new GameScene(800, 600);

                Player p = loadedScene.getPlayer();
                p.setLives(latestSave.lives());
                p.setScore(latestSave.currentScore());
                p.setPosition(new Point2D(latestSave.positionX(), latestSave.positionY()));
                p.setHasShield(latestSave.hasShield());
                p.setRapidFireTimer(latestSave.rapidFireTimer());
                p.setTripleShotTimer(latestSave.tripleShotTimer());
                p.setDoublePointTimer(latestSave.doublePointTimer());
                loadedScene.setAsteroidSpawnInterval(latestSave.asteroidSpawnInterval());
                loadedScene.setDifficultyTimer(latestSave.difficultyTimer());

                log.info("Game was successfully loaded from server!");
                return loadedScene;
            } else {
                log.error("Server vratil chybu: {}", response.statusCode());
            }
        } catch (Exception e) {
            log.error("Error while loading game from server: {}", e.getMessage(), e);
        }
        return null;
    }
}
