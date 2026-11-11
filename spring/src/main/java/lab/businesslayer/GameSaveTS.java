package lab.businesslayer;

import lab.dto.GameSaveDto;
import lab.entities.GameSave;
import lab.entities.Player;
import lab.repositories.GameSaveRepository;
import lab.repositories.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameSaveTS {

    private final GameSaveRepository gameSaveRepository;
    private final PlayerTS playerTS;

    @Transactional
    public void saveGame(GameSaveDto request) {
        Player player = playerTS.getOrCreatePlayer(request.nickname());

        GameSave newSave = GameSave.builder()
                .player(player)
                .savedAt(request.savedAt())
                .description(request.description())
                .lives(request.lives())
                .currentScore(request.currentScore())
                .positionX(request.positionX())
                .positionY(request.positionY())
                .hasShield(request.hasShield())
                .rapidFireTimer(request.rapidFireTimer())
                .tripleShotTimer(request.tripleShotTimer())
                .doublePointTimer(request.doublePointTimer())
                .asteroidSpawnInterval(request.asteroidSpawnInterval())
                .difficultyTimer(request.difficultyTimer())
                .build();

        gameSaveRepository.save(newSave);
    }

    public List<GameSaveDto> getAllSaves() {
        List<GameSave> allSaves = gameSaveRepository.findAll();

        return allSaves.stream()
                .map(save -> new GameSaveDto(
                        save.getPlayer().getNickname(),
                        save.getSavedAt(),
                        save.getDescription(),
                        save.getLives(),
                        save.getCurrentScore(),
                        save.getPositionX(),
                        save.getPositionY(),
                        save.isHasShield(),
                        save.getRapidFireTimer(),
                        save.getTripleShotTimer(),
                        save.getDoublePointTimer(),
                        save.getAsteroidSpawnInterval(),
                        save.getDifficultyTimer()
                ))
                .toList();
    }
}