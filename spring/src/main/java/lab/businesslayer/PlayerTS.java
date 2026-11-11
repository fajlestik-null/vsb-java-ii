package lab.businesslayer;

import lab.entities.Player;
import lab.repositories.PlayerRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlayerTS {

  private final PlayerRepository playerRepository;

  @Transactional
  public Player getOrCreatePlayer(String nickname) {
    return playerRepository.findByNickname(nickname)
            .orElseGet(() -> {
              Player newPlayer = Player.builder()
                      .nickname(nickname)
                      .build();
              return playerRepository.save(newPlayer);
            });
  }

}
