# TrafficFlowGame

<img src="https://raw.githubusercontent.com/mostafaelamin/TrafficFlowGame/main/TrafficFlow.gif" alt="TrafficFlowGame Gameplay Demo" width="550"/>

A **Java-based Object-Oriented Programming (OOP)** project that simulates real-world traffic flow using polymorphic vehicle classes, geometric logic, and modular design principles. Built to demonstrate clean class hierarchies, abstraction, and maintainable code structure.

---

## Overview

**TrafficFlowGame** is a simulation that models vehicles — including cars, trucks, and ambulances — moving within a traffic environment.  
The project emphasizes **software design principles** and **object-oriented architecture**, showcasing strong fundamentals in Java development such as:

- Inheritance and polymorphism  
- Interface-driven design  
- Abstraction and encapsulation  
- Code reusability and modularity  

This project serves as an demonstration of scalable OOP architecture.

---

## Technical Highlights

- **OOP Architecture:** Implements inheritance hierarchies where all vehicle types extend from a base `TrafficElement` class.  
- **Interface Implementation:** `Movable` interface defines consistent movement behavior across all objects.  
- **Abstraction & Encapsulation:** Abstract classes and private instance variables promote maintainable and extensible code.  
- **Geometric Modeling:** Provided `Point` and `Polygon` classes handle positional logic, aiding movement and collision checks.  
- **Polymorphism in Action:** Multiple vehicle subclasses (Car, Truck, Ambulance) demonstrate runtime method overriding and behavior customization.  

---

## Project Structure

* **TrafficFlowGame/**
    * **src/**
        * **game/**
            * `Ambulance.java` - A fast-moving, autonomous vehicle subclass
            * `Car.java` - The player-controlled vehicle subclass
            * `Game.java` - The abstract engine for the game window and loop
            * `Movable.java` - An interface defining movement behavior
            * `Point.java` - A geometric helper class for coordinates
            * `Polygon.java` - A geometric helper class for defining shapes
            * `TrafficElement.java` - The abstract base class for all vehicle objects
            * `TrafficFlowGame.java` - The main program entry point and game controller
            * `Truck.java` - A slow-moving, autonomous vehicle subclass
    * `.gitignore`
    * `LICENSE`
    * `README.md`

How to Run!

1. Clone the repository to your local machine.
2. Open the project in your preferred Java IDE (e.g., Eclipse, IntelliJ).
3. Locate the TrafficFlowGame.java file inside the src/game package.
4. Right-click on TrafficFlowGame.java and select "Run As" > "Java Application".

The game window will launch, and you can begin playing immediately!
