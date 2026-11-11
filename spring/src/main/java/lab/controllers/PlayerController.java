package lab.controllers;

import lab.businesslayer.PlayerTS;
import lab.dto.PlayerDto;
import lab.entities.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerTS playerTS;

    @GetMapping("/{nickname}")
    public PlayerDto getPlayer(@PathVariable String nickname) {
        Player player = playerTS.getOrCreatePlayer(nickname);

        return new PlayerDto(player.getNickname());
    }
}