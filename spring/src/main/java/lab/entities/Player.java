package lab.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "players")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nickname;

    @OneToMany(mappedBy = Score_.PLAYER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Score> scores = new ArrayList<>();

    @OneToMany(mappedBy = GameSave_.PLAYER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<GameSave> gameSaves = new ArrayList<>();
}