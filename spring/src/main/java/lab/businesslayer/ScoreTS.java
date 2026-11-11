package lab.businesslayer;

import lab.dto.ScoreDto;
import lab.entities.Player;
import lab.entities.Score;
import lab.repositories.PlayerRepository;
import lab.repositories.ScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreTS {

    private final ScoreRepository scoreRepository;
    private final PlayerTS playerTS;

    @Transactional
    public void addScore(String nickname, int scorePoints) {
        Player player = playerTS.getOrCreatePlayer(nickname);

        Score newScore = Score.builder()
                .player(player)
                .scorePoints(scorePoints)
                .build();

        scoreRepository.save(newScore);
    }

    public List<ScoreDto> getAllScores() {
        List<Score> scores = scoreRepository.findAll();

        return scores.stream()
                .map(score -> new ScoreDto(
                        score.getPlayer().getNickname(),
                        score.getScorePoints()
                ))
                .toList();
    }
}
