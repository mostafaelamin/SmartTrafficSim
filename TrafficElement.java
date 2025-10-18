package game;

import java.awt.*;

/*
AUTHORS: Zishan Vahora and Mostafa Elamin
DATE: October 2025
CLASS: TrafficElement (Abstract)
DESCRIPTION: Base class for all moving vehicles. Extends Polygon for shape and
             position, implements Movable for movement logic.
USAGE: Must be subclassed by specific vehicle types (Car, Truck, Ambulance).
*/
abstract class TrafficElement extends Polygon implements Movable {
    protected double stepSize = 3.0;
    protected double rotationRate = 5.0; // degrees per frame
    
    // Booleans for user-controlled movement (only used by Car, but here for easy access)
    public boolean forward = false;
    public boolean backward = false;
    public boolean left = false;
    public boolean right = false;

    public TrafficElement(Point[] inShape, Point inPosition, double inRotation) {
        super(inShape, inPosition, inRotation);
    }

    // Abstract method: forces subclasses to define how they draw themselves.
    abstract public void paint(Graphics brush);

    /*
    METHOD: move
    DESCRIPTION: Updates the element's position and rotation based on boolean flags.
    (Requirement: Must rotate elements, Must have at least one element move using keyboard input)
    */
    @Override
    public void move() {
        if (forward) {
            double radians = Math.toRadians(rotation);
            position.x += stepSize * Math.cos(radians);
            position.y += stepSize * Math.sin(radians);
        } else if (backward) {
            double radians = Math.toRadians(rotation);
            // Move opposite to the direction of rotation
            position.x -= stepSize * Math.cos(radians);
            position.y -= stepSize * Math.sin(radians);
        }

        if (left) {
            rotation = (rotation - rotationRate) % 360;
        } else if (right) {
            rotation = (rotation + rotationRate) % 360;
        }
    }
    
    /*
    METHOD: collides
    DESCRIPTION: Checks if this Polygon is intersecting with another Polygon.
    (Requirement: Must handle collisions between elements)
    */
    public boolean collides(TrafficElement other) {
        // Use the static nested class to handle the core logic
        return CollisionHandler.checkIntersection(this, other);
    }
    
    // Inner Class 2: CollisionHandler (Static Nested Class)
    // Encapsulates the logic for checking polygon intersection.
    protected static class CollisionHandler {
        /*
        METHOD: checkIntersection
        DESCRIPTION: Checks if any point of 'poly2' is contained in 'poly1' OR
                     if any point of 'poly1' is contained in 'poly2'.
        */
        public static boolean checkIntersection(TrafficElement poly1, TrafficElement poly2) {
            // Check if any point of poly2 is inside poly1
            for (Point p : poly2.getPoints()) {
                if (poly1.contains(p)) {
                    return true;
                }
            }
            
            // Check if any point of poly1 is inside poly2
            for (Point p : poly1.getPoints()) {
                if (poly2.contains(p)) {
                    return true;
                }
            }
            
            return false;
        }
    }
}