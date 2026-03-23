# SwiftBot Software Design & Implementation

This repository contains the design and implementation for the **CS1813 Software Design** project.  
The project focuses on developing autonomous and interactive behaviours for the **SwiftBot robot** using **Java** and **Object-Oriented Programming (OOP)** principles.

---

## Project Overview

The aim of this assignment is to design and implement a software solution for a robotics-based problem using a structured software development approach.

The project includes the following stages:

- **Software Requirements Specification (SRS)** – defining functional and non-functional requirements
- **Algorithm Design** – developing logic using flowcharts and sub-processes
- **User Interface (UI) Design** – creating a Command-Line Interface (CLI) for user interaction
- **Project Planning** – organising development using tools such as Gantt charts
- **Implementation** – building the final Java solution for SwiftBot
- **Testing and Refinement** – validating the program and improving reliability

---

## SwiftBot Tasks

The SwiftBot project includes a range of robotics challenges designed to test computer vision, movement logic, sensors, interaction, and problem-solving.

### Task 1: Master Mind
A colour-guessing game where SwiftBot uses its camera to scan colour cards and provides feedback to the player using symbols such as `+` and `-`.

### Task 2: ZigZag Path
SwiftBot scans a QR code to determine path length and number of sections, then moves in an orthogonal zigzag pattern while changing LED colours.

### Task 3: Snakes and Ladders
A physical board game simulation where SwiftBot moves according to dice rolls and reacts to snakes and ladders on the board.

### Task 4: Traffic Light
SwiftBot autonomously navigates a route, using its camera to detect traffic light colours and respond appropriately by stopping, waiting, or turning.

### Task 5: SpyBot
A secure communication task where SwiftBot encodes and decodes Morse code messages using its buttons, LEDs, and movement features.

### Task 6: Shape Drawer
SwiftBot scans QR codes containing shape instructions (for example `S:16` for a 16 cm square) and physically moves to draw the required shape.

### Task 7: Noughts and Crosses
A 3x3 grid-based game where the user plays against SwiftBot, with the robot tracking moves and updating the game state.

### Task 8: Search for Light
An autonomous task where SwiftBot searches for the brightest light source while avoiding obstacles in its environment.

### Task 9: Number System Converter
SwiftBot scans hexadecimal values from QR codes, converts them into other number systems, and uses the results to control movement and LED output.

### Task 10: Curious / Scaredy / Dubious Bot
A multi-mode robot behaviour task where SwiftBot can:
- follow objects (**Curious**)
- move away from objects (**Scaredy**)
- alternate between behaviours (**Dubious**)

### Task 11: Tunnel Navigator
A navigation challenge where SwiftBot detects and travels through a sequence of tunnels.

---

## OOP Concepts Applied

This project applies core **Object-Oriented Programming** principles to create a structured, reusable, and maintainable codebase.

### Encapsulation
Data such as sensor readings, QR values, and game states are kept private within classes and accessed through controlled methods where needed.

### Abstraction
Complex operations such as robot movement, QR scanning, and validation are broken into simpler methods so the main program remains readable and manageable.

### Inheritance
Where appropriate, common logic can be placed into parent classes and shared across specialised subclasses.

### Polymorphism
Different classes can provide their own implementation of shared behaviours where needed.

### Modular Design
The system is divided into multiple components and sub-processes, such as:

- input handling and validation
- data processing
- movement control
- logging and output generation

This improves readability, testing, debugging, and future maintenance.

---

## Technologies Used

- **Java**
- **SwiftBot API**
- **Object-Oriented Programming**
- **Command-Line Interface (CLI)**
- **Flowcharts and software design documentation**
- **GitHub for version control**
