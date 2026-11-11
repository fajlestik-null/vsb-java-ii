package lab;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ToString
public class Background {
    private final List<Star> stars = new ArrayList<>();
    private final double width;
    private final double height;
    private final Random rand = new Random();

    @AllArgsConstructor
    @ToString
    @Getter
    private static class Star {
        double x;
        double y;
        double size;
        double opacity;
    }

    public Background(double width, double height) {
        this.width = width;
        this.height = height;
        generateStars();
    }

    private void generateStars() {
        int starCount = 200;

        for (int i = 0; i < starCount; i++) {
            double x = rand.nextDouble() * width;
            double y = rand.nextDouble() * height;
            double size = 1.0 + rand.nextDouble() * 2.0;
            double opacity = 0.2 + rand.nextDouble() * 0.8;

            stars.add(new Star(x, y, size, opacity));
        }
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.rgb(10, 10, 30));
        gc.fillRect(0, 0, width, height);

        for (Star star : stars) {
            gc.setFill(Color.WHITE.deriveColor(0, 1, 1, star.opacity));
            gc.fillOval(star.x, star.y, star.size, star.size);
        }
    }
}
