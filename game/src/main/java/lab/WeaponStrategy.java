package lab;

import javafx.geometry.Point2D;
import java.io.Serializable;

public interface WeaponStrategy {
    void shoot(GameScene gameScene, Point2D position, double angle);

    default Point2D calculateBullet(Point2D shipPosition, double angle) {
        double rad = Math.toRadians(angle);
        double distance = 20;
        return shipPosition.add(Math.cos(rad) * distance, Math.sin(rad) * distance);
    }
}
