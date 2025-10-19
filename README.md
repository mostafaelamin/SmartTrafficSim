# TrafficFlowGame

A **Java-based Object-Oriented Programming (OOP)** project that simulates real-world traffic flow using polymorphic vehicle classes, geometric logic, and modular design principles. Built to demonstrate clean class hierarchies, abstraction, and maintainable code structure.

---

## Overview

**TrafficFlowGame** is a simulation that models vehicles — including cars, trucks, and ambulances — moving within a traffic environment.  
The project emphasizes **software design principles** and **object-oriented architecture**, showcasing strong fundamentals in Java development such as:

- Inheritance and polymorphism  
- Interface-driven design  
- Abstraction and encapsulation  
- Code reusability and modularity  

This project serves as both an educational exercise and a demonstration of scalable OOP architecture.

---

## Technical Highlights

- **OOP Architecture:** Implements inheritance hierarchies where all vehicle types extend from a base `TrafficElement` class.  
- **Interface Implementation:** `Movable` interface defines consistent movement behavior across all objects.  
- **Abstraction & Encapsulation:** Abstract classes and private instance variables promote maintainable and extensible code.  
- **Geometric Modeling:** Provided `Point` and `Polygon` classes handle positional logic, aiding movement and collision checks.  
- **Polymorphism in Action:** Multiple vehicle subclasses (Car, Truck, Ambulance) demonstrate runtime method overriding and behavior customization.  

---

## Project Structure

TrafficFlowGame/
├── src/
│ └── game/
│ ├── Ambulance.java # Specialized vehicle subclass
│ ├── Car.java # Standard vehicle subclass
│ ├── Game.java # Core simulation controller
│ ├── Movable.java # Interface defining movement behavior
│ ├── Point.java # Provided geometric helper class
│ ├── Polygon.java # Provided geometric helper class
│ ├── TrafficElement.java # Abstract base class for all vehicles
│ ├── TrafficFlowGame.java # Main program entry point
│ └── Truck.java # Heavy vehicle subclass
├── .gitignore
├── LICENSE
└── README.md

How to Run!

1. Clone the repository to your local machine.
2. Open the project in your preferred Java IDE (e.g., Eclipse, IntelliJ).
3. Locate the TrafficFlowGame.java file inside the src/game package.
4. Right-click on TrafficFlowGame.java and select "Run As" > "Java Application".

The game window will launch, and you can begin playing immediately!
