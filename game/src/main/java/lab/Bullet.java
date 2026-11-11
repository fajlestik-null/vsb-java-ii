package lab;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import lombok.ToString;

@ToString(callSuper = true)
public class Bullet extends WorldEntity{
    private double lifetime = 2.0;

    public Bullet(GameScene gameScene, Point2D position, double angle) {
        super(gameScene, position, new Polygon(2, -2, 2, 2, -2, 2, -2, -2));
        this.shape.setRotate(angle);

        double speed = 500;
        this.velocity = new Point2D(Math.cos(Math.toRadians(angle)) * speed,Math.sin(Math.toRadians(angle)) * speed);
    }

    @Override
    public Color getColor() {
        return Color.PURPLE;
    }

    @Override
    public void hitBy(Collisionable other) {
        if (other instanceof Scorable target) {
            gameScene.getPlayer().addScore(target.getScore());
        }
    }

    @Override
    public void simulate(double deltaTime) {
        move(deltaTime);
        applyDrag(deltaTime);

        lifetime -= deltaTime;
        if (lifetime <= 0) {
            gameScene.removeEntity(this);
        }
    }
}
