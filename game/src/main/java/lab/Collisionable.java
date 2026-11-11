package lab;

import javafx.scene.shape.Polygon;

public interface Collisionable {

	Polygon getBoundingBox();

	boolean intersect(Polygon other);

	void hitBy(Collisionable other);

}
