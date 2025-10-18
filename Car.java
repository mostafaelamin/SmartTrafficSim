package game;

import java.awt.*;

/*
AUTHORS: Zishan Vahora and Mostafa Elamin
DATE: October 2025
CLASS: Car
DESCRIPTION: The user's controllable traffic element.
*/
class Car extends TrafficElement {
    
    public Car(Point[] inShape, Point inPosition, double inRotation) {
        super(inShape, inPosition, inRotation);
        this.stepSize = 4.0; // Slightly faster base speed
    }

    @Override
    public void paint(Graphics brush) {
        brush.setColor(Color.BLUE);
        // Convert Point[] (double coords) to int[] for drawing methods
        Point[] points = getPoints();
        int[] xCoords = new int[points.length];
        int[] yCoords = new int[points.length];
        
        for (int i = 0; i < points.length; i++) {
            xCoords[i] = (int) points[i].x;
            yCoords[i] = (int) points[i].y;
        }
        
        brush.fillPolygon(xCoords, yCoords, points.length);
    }
    
    // Overriding the move method to add some extra logic if needed (e.g., braking, skidding)
    @Override
    public void move() {
        super.move();
        // Example: logic to wrap the car around the screen
        if (position.x > 800) position.x = 0;
        if (position.x < 0) position.x = 800;
        if (position.y > 600) position.y = 0;
        if (position.y < 0) position.y = 600;
    }
}