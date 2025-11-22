🎮 Java - 15 Puzzle with AutoSolving Feature

A classic logic-based puzzle solving game built with Java Swing

📌 Overview

This project is a Java-based arcade puzzle game that challenges players to think quickly, plan moves carefully, and score as many points as quick as possible before the board times up. 
Developed using Java Swing, the game offers smooth animations, dynamic tile behavior, and customizable difficulty settings.

The goal is simple:
👉 Match tiles, clear the board, and achieve the highest possible score!

🕹️ Game Features
⭐ 1. Dynamic Tile Grid

A tile-based board that updates in real-time.

Tiles move based on player actions.

⭐ 2. Puzzle Logic

Players must order tiles according to the game rules.

⭐ 3. Multiple Difficulty Levels

Easy / Medium / Hard modes.

Faster tile generation and stricter penalties at higher levels.

⭐ 4. Score Tracking

Real-time scoring system.

High score memory .

⭐ 5. Customizable Visuals

Smooth font rendering

Adjustable grid size and tile spacing

⭐ 6. Randomization Engine

Board generation uses Java’s Random class for unpredictable tile placement.

Ensures each game run feels fresh.

⭐ 7. Event-Driven UI

Mouse-based tile selection

Buttons for restart, and exit


Continue playing until the game-over conditions are met.

🛠️ Technologies Used

Java 8+

Java Swing (UI rendering)

AWT (colors, fonts, graphics)

Custom logic classes for tile management, scoring, and game state

Random utilities for generating unpredictable board scenarios

📂 Project Structure
/src
 ├── BoardGUI.java
 ├── MainMenu.java
 ├── Player.java
 ├── Driver.java
/Pics
/Sounds
/build

▶️ How to Run
Option 1: Compile & Run via Terminal
javac *.java
java Driver.java

Option 2: Run in an IDE

Open the project in IntelliJ, Eclipse, or NetBeans

Set Driver.java as the entry point

Click Run


📄 License

This project is open for learning and personal use.
Please credit the original developer when reusing the code.
