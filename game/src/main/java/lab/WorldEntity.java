package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public abstract class WorldEntity implements DrawableSimulable, Warpable, Collisionable, Soundable {

	protected GameScene gameScene;

    @Getter
    @Setter
    protected Point2D position;
    protected Point2D velocity;
    @Getter
    protected Polygon shape;

    @Setter
    protected double angularVelocity = 0;
    @Setter
    protected double rotationPower = 200;

	protected WorldEntity(GameScene gameScene, Point2D position, Polygon shape) {
		this.gameScene = gameScene;
		this.position = position;
        this.velocity = new Point2D(0, 0);
        this.shape = shape;
        this.shape.setTranslateX(position.getX());
        this.shape.setTranslateY(position.getY());
	}

    public void rotateLeft(double deltaTime) {
        this.angularVelocity -= rotationPower * deltaTime;
    }

    public void rotateRight(double deltaTime) {
        this.angularVelocity += rotationPower * deltaTime;
    }

    public void applyRotationPhysics(double deltaTime) {
        this.shape.setRotate(this.shape.getRotate() + angularVelocity * deltaTime);

        double dragFactor = Math.pow(0.95, deltaTime * 60);
        this.angularVelocity *= dragFactor;

        if (Math.abs(angularVelocity) < 0.5) {
            angularVelocity = 0;
        }
    }

    public void move(double deltaTime){
        double maxDelta = 0.05;
        deltaTime = Math.min(deltaTime, maxDelta);

        position = position.add(velocity.multiply(deltaTime));
        position = warp(gameScene, position);

        applyRotationPhysics(deltaTime);

        shape.setTranslateX(position.getX());
        shape.setTranslateY(position.getY());
    }

    public void accelerate(double deltaTime) {
        double angle = Math.toRadians(shape.getRotate());
        double thrustPower = 200;

        Point2D thrust = new Point2D(
            Math.cos(angle) * thrustPower * deltaTime,
            Math.sin(angle) * thrustPower * deltaTime
        );

        velocity = velocity.add(thrust);
    }

    public void applyDrag(double deltaTime) {
        double dragFactor = Math.pow(0.995, deltaTime * 60); // scale drag by frame
        velocity = velocity.multiply(dragFactor);

        if (velocity.magnitude() < 0.5) {
            velocity = new Point2D(0, 0);
        }
    }

    public Polygon getBoundingBox() {
        Polygon bounds = new Polygon();
        double angle = Math.toRadians(shape.getRotate());

        for (int i = 0; i < shape.getPoints().size(); i += 2) {
            double x = shape.getPoints().get(i);
            double y = shape.getPoints().get(i + 1);

            double rotatedX = x * Math.cos(angle) - y * Math.sin(angle);
            double rotatedY = x * Math.sin(angle) + y * Math.cos(angle);

            rotatedX += position.getX();
            rotatedY += position.getY();

            bounds.getPoints().addAll(rotatedX, rotatedY);
        }

        return bounds;
    }

    @Override
    public boolean intersect(Polygon other) {
        return (Shape.intersect(this.getShape(), other)).getBoundsInLocal().getWidth() != -1;
    }

	@Override
	public final void draw(GraphicsContext gc) {
		gc.save();
		drawInternal(gc);
		gc.restore();
	}

    public void drawInternal(GraphicsContext gc) {
        gc.translate(position.getX(), position.getY());

        gc.rotate(shape.getRotate());

        double[] xPoints = new double[shape.getPoints().size() / 2];
        double[] yPoints = new double[shape.getPoints().size() / 2];

        for (int i = 0; i < shape.getPoints().size(); i += 2) {
            xPoints[i / 2] = shape.getPoints().get(i);
            yPoints[i / 2] = shape.getPoints().get(i + 1);
        }

        gc.setStroke(Color.BLACK);
        gc.strokePolygon(xPoints, yPoints, xPoints.length);
        gc.setFill(getColor());
        gc.fillPolygon(xPoints, yPoints, xPoints.length);
    }

    public abstract Color getColor();

}
