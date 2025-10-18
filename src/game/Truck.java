package game;

import java.awt.*;

/*
AUTHORS: Zishan Vahora and Mostafa Elamin
DATE: October 2025
CLASS: Truck
DESCRIPTION: A large, slow traffic element (simulating congestion).
*/
class Truck extends TrafficElement {
    
    public Truck(Point[] inShape, Point inPosition, double inRotation) {
        super(inShape, inPosition, inRotation);
        this.stepSize = 1.0; // Very slow speed
        this.rotationRate = 2.0; // Slow rotation
    }

    @Override
    public void paint(Graphics brush) {
        brush.setColor(new Color(139, 69, 19)); // Brown
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
    
    // Automatic Movement: Simple continuous movement
    @Override
    public void move() {
        double radians = Math.toRadians(rotation);
        position.x += stepSize * Math.cos(radians);
        position.y += stepSize * Math.sin(radians);
    }
}