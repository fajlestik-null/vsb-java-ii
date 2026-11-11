package lab.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "game_saves")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameSave {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    private LocalDateTime savedAt;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int lives;

    @Column(nullable = false)
    private int currentScore;

    private double positionX;
    private double positionY;

    private boolean hasShield;
    private double rapidFireTimer;
    private double tripleShotTimer;
    private double doublePointTimer;


    @Column(nullable = false)
    private double asteroidSpawnInterval;
    private double difficultyTimer;
}