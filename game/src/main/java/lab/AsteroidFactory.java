package lab;

import javafx.scene.shape.Polygon;
import java.util.Random;

public class AsteroidFactory {
    private static final Random RANDOM = new Random();

    public Polygon createAsteroid(double size) {

        Polygon polygon = new Polygon();
        double c1 = Math.cos(Math.PI * 2 / 5);
        double c2 = Math.cos(Math.PI / 5);
        double s1 = Math.sin(Math.PI * 2 / 5);
        double s2 = Math.sin(Math.PI * 4 / 5);

        polygon.getPoints().addAll(
            size, 0.0,
            size * c1, -1 * size * s1,
            -1 * size * c2, -1 * size * s2,
            -1 * size * c2, size * s2,
            size * c1, size * s1);

        polygon.getPoints().
            replaceAll(aDouble -> aDouble + RANDOM.nextInt(5) - 2);
        return polygon;
    }

    public Polygon createCrater(double size) {
        Polygon polygon = new Polygon();
        int points = 5 + RANDOM.nextInt(2);

        for (int i = 0; i < points; i++) {
            double angle = i * (Math.PI * 2 / points);
            double radius = size * (0.8 + RANDOM.nextDouble() * 0.4);

            double x = Math.cos(angle) * radius;
            double y = Math.sin(angle) * radius;
            polygon.getPoints().addAll(x, y);
        }
        return polygon;
    }
}
