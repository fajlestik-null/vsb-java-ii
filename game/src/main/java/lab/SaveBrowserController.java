package lab;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.application.Platform;
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
import java.time.format.DateTimeFormatter;
import java.util.List;

@Log4j2
public class SaveBrowserController {

    private App app;

    @FXML
    private Button exit;

    @FXML
    private TableView<GameSaveDto> saveTable;

    @FXML
    private TableColumn<GameSaveDto, String> nickColumn;

    @FXML
    private TableColumn<GameSaveDto, String> descColumn;

    @FXML
    private TableColumn<GameSaveDto, String> dateColumn;

    public void setApp(App app) {
        this.app = app;
        app.buttonHoverSound(exit);
        loadSaves();
    }

    @FXML
    void initialize() {
        nickColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().nickname()));
        descColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().description()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        dateColumn.setCellValueFactory(data -> {
            if (data.getValue().savedAt() != null) {
                return new SimpleStringProperty(data.getValue().savedAt().format(formatter));
            }
            return new SimpleStringProperty("");
        });

        saveTable.setRowFactory(tv -> {
            TableRow<GameSaveDto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    GameSaveDto selectedSave = row.getItem();
                    app.buttonClickSound();

                    app.startGameFromSave(selectedSave);
                }
            });
            return row;
        });
    }

    public void loadSaves() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/saves")) // Voláme nový endpoint pro všechny savy
                .GET()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    if (response.statusCode() == 200) {
                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            mapper.registerModule(new JavaTimeModule());

                            List<GameSaveDto> saves = mapper.readValue(response.body(), new TypeReference<List<GameSaveDto>>() {});

                            Platform.runLater(() -> {
                                saveTable.getItems().clear();
                                saveTable.getItems().addAll(saves);
                            });
                        } catch (Exception e) {
                            log.error("ERROR while parsing JSON saves", e);
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
}