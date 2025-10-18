package game;

import java.awt.*;

// AUTHORS: Zishan Vahora and Mostafa Elamin
// DATE: October 2025
//CLASS: Ambulance
//DESCRIPTION: A fast emergency vehicle that moves automatically.
class Ambulance extends TrafficElement {
    
    public Ambulance(Point[] inShape, Point inPosition, double inRotation) {
        super(inShape, inPosition, inRotation);
        this.stepSize = 5.0; // Fast speed
    }

    @Override
    public void paint(Graphics brush) {
        brush.setColor(Color.RED);
        // Convert Point[] (double coords) to int[] for drawing methods
        Point[] points = getPoints();
        int[] xCoords = new int[points.length];
        int[] yCoords = new int[points.length];
        
        for (int i = 0; i < points.length; i++) {
            xCoords[i] = (int) points[i].x;
            yCoords[i] = (int) points[i].y;
        }
        
        brush.fillPolygon(xCoords, yCoords, points.length);
        
        // Draw a white cross
        brush.setColor(Color.WHITE);
        brush.drawLine((int)position.x, (int)position.y - 5, (int)position.x, (int)position.y + 5);
        brush.drawLine((int)position.x - 5, (int)position.y, (int)position.x + 5, (int)position.y);
    }
    
    // automatic Movement, simple continuous movement
    @Override
    public void move() {
        double radians = Math.toRadians(rotation);
        position.x += stepSize * Math.cos(radians);
        position.y += stepSize * Math.sin(radians);
    }
}