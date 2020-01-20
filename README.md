game
====

This project implements the game of Breakout.

Name: Max Smith

### Timeline

Start Date: January 11, 2020

Finish Date: January 19, 2020

Hours Spent: 15

### Resources Used
- JavaFX Tutorial (linked on course site)
- TA Office hours (very helpful)
- Piazza page

### Running the Program

Main class: Game.java

Data files needed: level_x.txt (x = 1-5)

Key/Mouse inputs: 
- left/right (paddle control)
- enter (exit splash screen)
- space (release ball from paddle)

Cheat keys:
- 'L' additional life
- 'R' restart level
- 'X' duplicate all bouncers on screen
- 'E' extend paddle size
- 'F' reduce paddle size
- 'S' make paddle sticky (space to release)
- 'A' accelerate paddle speed
- 'W' slow all bouncers on screen
- '1-9' jump to highest available level

Known Bugs:
- Collisions do not perform as expected (rare)
- Paddle can go off the screen (no bounds check)
- You can play over splash screen, glitches occur
- IOException for new files (should use different writer, create file if not exists)

Extra credit:
- Level generation class, trivial adjustment to play infinitely
    - Run LevelGenerator with max level set appropriately, then play game

### Notes/Assumptions
- Assumed that design was more important than functionality.
- Assumed that scenes were the best base jfx node (in hindsight, panes might have been better).
- Power ups assumed to be able to take the form of cheat keys.
- Paddle deflection dynamically calculated based on where intersection occurs

### Impressions
- Challenging and rewarding first project.
- Class extensions and inheritance proved very important.
- New experience using 'super' and 'this' constructor/expression
- Logical choice to use objects to transport private class information