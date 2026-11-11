package lab;

import javafx.scene.paint.Color;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PowerUpType {
    HEAL(Color.GREEN){
        @Override
        public void applyTo(Player player) {
            player.increaseLives();
        }
    },
    SHIELD(Color.DEEPSKYBLUE){
        @Override
        public void applyTo(Player player) {
            player.activateShield();
        }
    },
    DOUBLE_POINTS(Color.YELLOW){
        @Override
        public void applyTo(Player player) {
            player.activateDoublePoints();
        }
    },
    TRIPLE_SHOT(Color.PURPLE){
        @Override
        public void applyTo(Player player) {
            player.activateTripleShot();
        }
    },
    RAPID_FIRE(Color.RED){
        @Override
        public void applyTo(Player player) {
            player.activateRapidFire();
        }
    };

    private final Color color;

    public abstract void applyTo(Player player);
}
