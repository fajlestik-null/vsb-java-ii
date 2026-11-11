package lab;

import javafx.geometry.Point2D;

public interface Warpable {
    default Point2D warp(GameScene gameScene, Point2D position)
    {
        double x = position.getX();
        double y = position.getY();
        double windowWidth = gameScene.getSize().getWidth();
        double windowHeight = gameScene.getSize().getHeight();

        if (x < 0) {
            x += windowWidth;
        }
        if (x > windowWidth) {
            x -= windowWidth;
        }
        if (y < 0) {
            y += windowHeight;
        }
        if (y > windowHeight) {
            y -= windowHeight;
        }

        return new Point2D(x, y);
    }
}
