# Mastermind Project Specification

## Software Requirement Specification (SRS)

This specification defines a Mastermind game for a mobile robot platform. Following this specification exactly shall produce consistent, reproducible system behavior.

# Functional Requirements
1. Upon application initiation, the system shall display a clear interaction menu through the command-line interface and enter a wait state.
2. The system shall monitor for a primary (Control A) or secondary (Control B) interaction signal to determine the active application mode.
3. If the primary signal (Control A) is received, the system shall assign a target sequence length of four and a maximum attempt limit of six.
4. If the primary signal (Control A) is received, the system shall immediately lock these parameters and transition to the challenge generation phase.
5. If the secondary signal (Control B) is received, the system shall initiate a parameter request sequence for sequence length and attempt limits.
6. For customized length, the system shall prompt the user to specify a code length between 3 and 6 colors and verify the value is within numeric bounds.
7. For customized attempt limits, the system shall prompt the user to specify the maximum number of attempts allowed and verify the input is a positive integer.
8. The system shall provide informative error messages for any out-of-range or non-numeric values and repeat the prompt until valid data is secured.
9. The system shall provide an option to quit the program at the initial menu (Control X) which triggers the graceful exit sequence.
10. The system shall utilize exactly six predefined distinct colors for all challenges, specifically Red (R), Green (G), Blue (B), Yellow (Y), Orange (O), and Pink (P).
11. For Default Mode, the system shall randomly select four colors from the reference palette ensuring that no colors are repeated within the sequence.
12. For Customized Mode, the system shall randomly select colors from the reference palette to populate the user-defined sequence length between 3 and 6.
13. For Customized Mode, the system shall permit color repetitions within the generated secret sequence.
14. The system shall store the generated secret sequence in a non-volatile internal state, protected from user visibility until the final revelation phase.
15. The system shall execute an acquisition cycle for each individual position in the secret challenge, following a stepwise prompt and capture sequence.
16. The system shall explicitly instruct the user to present a specific color card to the optical sensor (e.g., "Hold card 1 of 4 in front of the camera").
17. Upon receiving the user's trigger signal, the system shall capture a digital image through the platform's optical sensor for processing.
18. The system shall transform the captured digital image into a representative pixel matrix for detailed RGB intensity analysis.
19. For every pixel within the sampled matrix, the system shall extract the Red, Green, and Blue (RGB) intensity values for calculation.
20. The system shall calculate the arithmetic mean of the RGB values across the entire matrix to determine a normalized color value for classification.
21. The system shall compare the normalized RGB value against the six reference palette members using objective geometric similarity metrics in the color space.
22. The system shall identify the palette member that provides the closest geometric match and provide an immediate visual confirmation on the platform.
23. The system shall record the identified color for the current guess and advance automatically to the subsequent position in the sequence.
24. Once a full sequence of colors is scanned, the system shall compare the player's attempt against the secret sequence to identify match criteria.
25. The system shall identify exact matches where colors are identical in both value and position, representing them with the '+' symbol.
26. The system shall identify partial matches where colors exist in the secret sequence but are in an incorrect position, representing them with the '-' symbol.
27. During evaluation, the system shall ensure that each color in the attempt and secret sequences is accounted for exactly once to prevent double-counting.
28. The system shall generate a response string where all '+' symbols are displayed first, followed by all '-' symbols (e.g., `++-`).
29. The system shall explicitly ensure that the feedback string does not reveal which specific colors in the guess corresponded to which symbols.
30. For a secret code of `RGBY` and a guess of `GPBY`, the system shall return an evaluation feedback of `++-`.
31. The system shall recognize a successful win if the feedback string contains only '+' symbols and matches the target sequence length exactly.
32. Upon a win, the system shall print a congratulatory success message to the interface and update the Player portion of the cumulative score.
33. The system shall recognize a loss if the attempt limit is zero and no success condition was achieved for the current secret sequence.
34. Upon a loss, the system shall declare the game lost, reveal the correct secret sequence to the user, and update the Computer score.
35. The system shall maintain a persistent "Player-vs-Computer" score variable (e.g., `1-0`) that remains in memory throughout the application session.
36. Upon the conclusion of every game, the system shall generate a detailed transaction record containing all session parameters.
37. Every log entry shall include the round number, secret sequence, player's input sequence, cumulative score, total guesses used, and guesses remaining.
38. The system shall append the record to a text file located in a dedicated "saved_game" directory relative to the application path.
39. Following the result notification, the system shall wait for a restart (Control Y) or quit (Control X) signal from the user.
40. If the restart signal (Control Y) is received, the system shall reset the game board, reset the attempt counter, and generate a new secret sequence.
41. If the restart signal is received, the system shall retain the current cumulative score and return to the main interaction menu.
42. If the quit signal (Control X) is received, the system shall display the absolute local path of the log file to the user for reference.
43. Upon final termination, the system shall perform a platform-specific hardware cleanup by returning all components to an idle or off state.
44. The system shall terminate all software processes and return a successful exit code to the operating environment.

# Non-Functional Requirements
1. All system instructions shall be presented in the imperative mood, following a "Action: Objective" pattern.
2. Prompts for numeric input shall explicitly state the valid range (e.g., "[3 - 6]").
3. The system shall acknowledge every primary control interaction (e.g., button press) within a maximum latency of 500 milliseconds.
4. The total time for image-to-color transformation and feedback calculation shall not exceed 2.0 seconds per attempt.
5. Logging operations shall complete in the background without causing a noticeable pause in the user interface thread.
6. The core game logic shall operate independently of the physical hardware drivers to ensure modular portability.
7. Application data and log files shall remain confined to the specified directory structure to prevent external file system corruption.
8. The system shall return to a known menu state if an invalid input is detected during configuration.

# Edge Cases
1. If a user enters non-numeric characters into a numeric field, the system shall reject the input and provide a corrective hint.
2. Parameters outside the programmed limits (e.g., a sequence length of 10) shall trigger an automatic re-prompt sequence.
3. In the event of concurrent control signals, the system shall prioritize the first registered interrupt and enforce a 500ms debounce interval.
4. If the color identification logic fails to reach a 70% certainty threshold, the system shall notify the user and request a re-scan of that specific position.
5. If the acquisition trigger fails to return valid image data, the system shall provide a diagnostic message suggesting sensor inspection and allow a retry.
6. If the log file is locked by the operating system, the system shall skip the current write operation and inform the user to prevent program stagnation.

# Additional Requirements
1. The system shall execute a complex robotic movement sequence to provide physical feedback to the user following significant achievements.
2. The kinetic sequence shall be activated automatically upon a win condition or a specific high-score milestone after player confirmation.
3. Upon success, the robot platform shall execute a 360-degree victory rotation at 50% motor power for a duration of exactly 2.0 seconds.
4. During the victory rotation, the platform's visual indicators shall pulse in synchronisation with the motor oscillations for enhanced feedback.