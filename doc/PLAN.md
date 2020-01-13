# Game Plan
## Max Smith

### Breakout Variant
My two favorite Breakout Variants were Super Breakout and Jet Ball.
- In Super Breakout, I enjoyed how the new levels filled into the map. It created an interesting advantage of ending the round with the ball high up to improve the "waterfall" effect as new bricks were added. 
- In Jet Ball, I thought the animations - specifically the paddle dip on impact, the ball trail, and the variety of brick animations on destruction were aesthetically pleasing. 

### General Level Descriptions
The general structure of the levels will progress in difficulty as the rounds continue. The difficulty increase will be
realized via an increase in "brick toughness" (i.e. hits to destroy the brick) as well as "brick density" (i.e. how many
bricks there are on the map).

### Bricks Ideas
- Indestructible bricks
- Bricks that hold power ups
- Bricks of varying point values

### Power Up Ideas
- Multiple balls
- Extended paddle
- Sticky paddle
- Small paddle

### Cheat Key Ideas
- Unlimited lives
- Slow ball speed down
- Enlarge the ball
- Additional balls

### Something Extra
For my "something extra", I am hoping to implement a "bonus" level with the following properties:
- The player has a single life
- The ball slowly speeds up as the round progresses
- Bricks periodically appear in empty slots as the board is cleared out
- Points are tracked based on how large the blocks that were cleared are

I believe this is a substantial addition as it will force me to make my code as flexible as possible. It seems trivial
to be able to populate a level with bricks in a certain shape, however keeping this "bonus" level in mind will ensure
that I don't hard code the brick distributions. Furthermore, with this level of flexibility it is possible to make the
game continue "forever" (subject to constraints, such as lives) making a never before seen "random" distribution of
bricks for each new level.
