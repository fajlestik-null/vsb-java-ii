package lab.controllers;

import lab.businesslayer.GameSaveTS;
import lab.dto.GameSaveDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saves")
@RequiredArgsConstructor
public class GameSaveController {

    private final GameSaveTS gameSaveTS;

    @GetMapping
    public List<GameSaveDto> getAllSaves() {
        return gameSaveTS.getAllSaves();
    }

    @PostMapping
    public void createSave(@RequestBody GameSaveDto request) {
        gameSaveTS.saveGame(request);
    }

}
