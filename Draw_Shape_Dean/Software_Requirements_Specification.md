# Software Requirements Specification (SRS)
## Project: SwiftBot Draw Shape

## 1. System Overview

The SwiftBot Draw Shape system is a command-line-based Java application designed to control a SwiftBot robot using QR code input. The system allows the robot to draw squares and triangles based on values encoded in the scanned QR code. It validates all input, calculates movement timing using calibration data, determines triangle angles before drawing, and logs execution details when the program terminates. The program continues running until the user presses the SwiftBot ‘X’ button.

## 2. Refined Functional Requirements

FR1. The system shall prompt the user at program start to scan a QR code containing shape drawing instructions.

FR2. The system shall accept QR code input specifying one or more shapes to be drawn in a single execution.

FR3. The system shall support square shapes defined using the format `S:<sideLength>`, where sideLength is an integer between 15 and 85 centimetres.

FR4. The system shall support triangle shapes defined using the format `T:<side1>:<side2>:<side3>`, where all side lengths are integers between 15 and 85 centimetres.

FR5. The system shall validate that all provided side lengths are within the allowed range before attempting to draw any shape.

FR6. The system shall validate that triangle side lengths satisfy the triangle inequality before executing the triangle drawing sequence.

FR7. The system shall allow up to a maximum of five shapes to be specified in a single QR code, separated by the `&` character.

FR8. When multiple shapes are requested, the system shall command the SwiftBot to move backwards by 15 centimetres before drawing each subsequent shape.

FR9. The system shall calculate the movement duration required to travel a specified distance using SwiftBot calibration data.

FR10. The system shall determine the internal angles of a triangle prior to drawing the triangle shape.

FR11. The system shall display informative status and error messages to the user via the command-line interface during execution.

FR12. The system shall detect invalid or unsupported QR code input and display an appropriate error message without terminating execution unexpectedly.

FR13. The system shall allow the user to terminate the program at any time by pressing the SwiftBot’s ‘X’ button.

FR14. The system shall record execution details to a persistent text file when the program terminates.

## 3. Refined Non-Functional Requirements

NFR1. The system shall operate exclusively using a command-line interface as the only user interface.

NFR2. The system shall be compatible with the SwiftBot robot hardware platform.

NFR3. The system shall operate reliably without unexpected termination during normal use.

NFR4. The system shall provide clear and understandable feedback messages suitable for users with limited technical experience.

NFR5. The system shall complete shape drawing operations within a reasonable time based on the physical movement constraints of the SwiftBot.

NFR6. The system shall store execution logs in a persistent text file that can be accessed after program termination.

## 4. Implemented Features That Were Not Added to the Design but Were Specified in the Brief

During the implementation, several features from the assignment brief were implemented even though they were not explicitly included in the Assignment 2 design.

### 4.1 Continuous scanning of multiple QR codes until ‘X’ is pressed
The system was extended to repeatedly prompt the user for QR input until the user presses the ‘X’ button. This allows the program to process multiple QR codes across the same session rather than ending after one run.

### 4.2 Real-time QR scanning using the SwiftBot camera
The final implementation uses the SwiftBot camera to capture and decode QR images directly, rather than relying only on an assumed QR input string.

### 4.3 Green LED feedback after shape completion
After successfully completing the drawing of a shape, the SwiftBot blinks the underlights in green to indicate completion.

### 4.4 Displaying the file path of the log file after termination
When the program is terminated, the system displays the location of the saved log file so that the user can access it easily.

### 4.5 Detailed statistical logging
The logging functionality was extended beyond the original design to include:
- shapes drawn in order
- time taken for each shape
- triangle angles
- largest shape by area
- most frequently drawn shape
- average drawing time

### 4.6 Enhanced input validation
The final implementation includes stronger validation than originally stated in the design, such as:
- empty QR input detection
- malformed command detection
- formatting error handling
- input recovery without crashing

## 5. Short Note on Design Consistency

The implemented system largely follows the Assignment 2 design, but the final version includes several minor enhancements and missing brief requirements that were added during implementation to make the system more complete, more robust, and more aligned with the required task description.
