package lab;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Log4j2
public class ScoreboardController {

    private App app;

    @FXML
    private Button exit;

    @FXML
    private TableView<ScoreDto> scoreTable;

    @FXML
    private TableColumn<ScoreDto, String> nickColumn;

    @FXML
    private TableColumn<ScoreDto, Integer> scoreColumn;


    public void setApp(App app) {
        this.app = app;
        app.buttonHoverSound(exit);
        loadScores();
    }

    public void loadScores() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/scores"))
                .GET()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    if (response.statusCode() == 200) {
                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            List<ScoreDto> scores = mapper.readValue(response.body(), new TypeReference<List<ScoreDto>>() {
                            });

                            Platform.runLater(() -> {
                                scoreTable.getItems().clear();
                                scoreTable.getItems().addAll(scores);
                                scoreTable.getSortOrder().clear();
                                scoreTable.getSortOrder().add(scoreColumn);
                                scoreColumn.setSortType(TableColumn.SortType.DESCENDING);
                                scoreTable.sort();
                            });
                        } catch (Exception e) {
                            log.error("ERROR while parsing JSON", e);
                        }
                    } else {
                        log.error("Server ERROR code: " + response.statusCode());
                    }
                }).exceptionally(e -> {
                    log.error("Server connection ERROR", e);
                    return null;
                });
    }

    @FXML
    private void onBackToMenu() {
        app.buttonClickSound();
        try {
            app.switchToMenu();
        } catch (IOException e) {
            log.error("Error while switching to menu: {}", e.getMessage(), e);
        }
    }

    @FXML
    void initialize() {
        nickColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().nickname()));
        scoreColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().scorePoints()));
        scoreTable.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(ScoreDto item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setStyle("");
                } else {
                    int index = getIndex();
                    switch (index) {
                        case 0 -> setStyle("-fx-background-color: rgba(255, 255, 0, 0.5); -fx-font-weight: bold;");
                        case 1 -> setStyle("-fx-background-color: rgba(192, 192, 192, 0.5); -fx-font-weight: bold;");
                        case 2 -> setStyle("-fx-background-color: rgba(205, 133, 63, 0.5); -fx-font-weight: bold;");
                        default -> setStyle("");
                    }
                }
            }
        });
    }
}
