# Game Plan
## Max Smith

### Breakout Variant
My two favorite Breakout Variants were Super Breakout and Jet Ball.
- In Super Breakout, I enjoyed how the new levels filled into the map. It created an interesting advantage of ending the round with the ball high up to improve the "waterfall" effect as new bricks were added. 
- In Jet Ball, I thought the animations - specifically the paddle dip on impact, the ball trail, and the variety of brick animations on destruction were aesthetically pleasing. 

### General Level Descriptions
The general structure of the levels will progress in difficulty as the rounds continue. The map difficulty increase will 
be realized via an increase in "brick toughness" (i.e. hits to destroy the brick) as well as "brick density" (i.e. how 
many bricks there are on the map). Furthermore, the game play itself will increase in difficulty through an increase in 
ball speed as well as a shortening of the paddle as the game progresses.

### Bricks Ideas
- Indestructible bricks
- Bricks that hold power ups
- Bricks of varying point values

### Power Up Ideas
- Multiple balls
- Extended paddle
- Sticky paddle
- Slower ball

### Cheat Key Ideas
- Life increase
- Restart the level
- Select power up

### Something Extra
For my "something extra", I am hoping to create the game in a multimodal fashion. These additional modes will force me
to keep the code as flexible as possible, where generalizations are leveraged to ease new implementations. 

Furthermore, the possibilities for multiple modes are endless. However, considering the fact that the goal of the
assignment is to perfect code design, I will select a few modes to pursue. Some ideas I have are as follows:
- Muliplayer mode, where metrics of success are time and/or points
- Timed mode, where the player has a specific amount of time to rack up points
- Endless mode, where a level generator function generates unique brick distributions

Another addition that I believe will add a lot to the game is a per mode high scores table. My vision of this table is to
keep a carefully formatted text file in the resources folder with scores, names (and dates?) which is read and rewritten 
every time the game is played. Hand in hand with this is keeping track of points scored.
