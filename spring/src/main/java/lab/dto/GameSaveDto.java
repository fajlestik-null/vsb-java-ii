package lab.dto;

import java.time.LocalDateTime;

public record GameSaveDto(String nickname,
                          LocalDateTime savedAt,
                          String description,
                          int lives,
                          int currentScore,
                          double positionX,
                          double positionY,
                          boolean hasShield,
                          double rapidFireTimer,
                          double tripleShotTimer,
                          double doublePointTimer,
                          double asteroidSpawnInterval,
                          double difficultyTimer) {
}
