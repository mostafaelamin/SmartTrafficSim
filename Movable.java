package game;

/*
AUTHORS: Zishan Vahora and Mostafa Elamin
DATE: October 2025
INTERFACE: Movable
DESCRIPTION: Defines the behavior for game elements that can move and rotate.
USAGE: Implemented by TrafficElement.
*/
public interface Movable {
    void move();
    void rotate(int degrees); // inherited from Polygon, but included here for completeness
}