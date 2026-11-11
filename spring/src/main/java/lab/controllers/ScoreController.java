package lab.controllers;

import lab.dto.ScoreDto;
import lab.entities.Score;
import java.util.List;

import lab.businesslayer.ScoreTS;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scores")
@RequiredArgsConstructor
public class ScoreController {

    private final ScoreTS scoreTS;

    @GetMapping
    public List<ScoreDto> getAllScores() {
        return scoreTS.getAllScores();
    }

    @PostMapping
    public void saveScore(@RequestBody ScoreDto request) {
        scoreTS.addScore(request.nickname(), request.scorePoints());
    }
}