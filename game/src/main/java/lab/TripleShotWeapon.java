package lab;

import javafx.geometry.Point2D;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Log4j2
public class TripleShotWeapon implements Soundable, WeaponStrategy {
    @Override
    public void shoot(GameScene scene, Point2D position, double angle) {
        playSound("audio/tripleShot.mp3");

        createBullet(scene, position, angle);
        createBullet(scene, position, angle - 15);
        createBullet(scene, position, angle + 15);
        log.debug("Triple shot activated. Firing 3 bullets.");
    }

    private void createBullet(GameScene scene, Point2D position, double angle) {
        Point2D bulletPos = calculateBullet(position, angle);
        Bullet b = new Bullet(scene, bulletPos, angle);
        scene.addEntity(b);
    }
}
