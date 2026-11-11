package lab;

import javafx.geometry.Point2D;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Log4j2
public class SingleShotWeapon implements WeaponStrategy, Soundable{
    @Override
    public void shoot(GameScene scene, Point2D position, double angle) {

        playSound("audio/singleShot.wav");
        Point2D bulletPos = calculateBullet(position, angle);
        Bullet b = new Bullet(scene, bulletPos, angle);
        log.trace("Single shot fired at angle: {}", angle);
        scene.addEntity(b);
    }
}
