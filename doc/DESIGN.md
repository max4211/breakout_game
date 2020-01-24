# Design
## Max Smith

The project was coded entirely by Max Smith, who played the role of developer.

The project's design goals were to enable the programmer to play the subtle variations to 
metadata surrounding aesthetics and functionality. One of my biggest design decisions was
to have global '''Groups''' that held all expected objects in the field of play at once (e.g. 
Bouncers, Paddle, Bricks, Walls). The result of this was that adding in new cheat keys that
affect objects in the game, in conjunction with those objects associated get and set methods, 
enabled the trivial addition of cheat keys. Furthermore, the game play was governed by monitoring
these global groups, in addition to other global data types  in the Game.java class (e.g. lives left)
during gameplay to determine level transitions (e.g. by checking contents of the Group myBrick to
verify level clearance). The last design decision that was maintained throughout the project is the  
dynamic nature of object generation, specifically walls and bricks. These objects are created depending
on various float/padding values, as well as on the screen dimensions so that the overall  aspect ratio
of the game is preserved even as the screen gets larger.

The high level design of the project involves the interaction of several core classes. The Game.java
class generates a game with several objects - Bouncer(s), Wall(s), Brick(s), Paddle. These objects
are then held in global Group data structures, and are monitored to govern gameplay.  The purpose
of each object is to hold information  regarding itself, as well as its instance triggering various
behaviors in the game. For example, the Paddle object deflects the Bouncer according to its '''PADDLE_EDGE'''
instance variable and how far over the Bouncer intersected the paddle. Furthermore, all of the main objects
in the game extend their basic shape (e.g. Bouncer extends Circle). This enables calling of the '''super'''
class in the object constructor, and allowed the object to inherit all of its super class (e.g. Shape/Rectangle)
function calls (e.g. 'getCenterX'). Additionally, there were two classes - LevelGenerator.java and Collision.java - 
with unconventional object behavior. LevelGenerator created the text files as new levels are created according
to its style (e.g. Random, Constant) and Collision.java processes all object collisions.   

Several decisions were made to simplify the project design, which had varying degrees of benefits and costs.
The global Groups for example, made governing the rules of the game very easy by just monitoring those groups.
Additionally, the assumption that other classes could be entrusted with objects all the time allowed for simplicity
of function inputs. In hindsight, although this may be unavoidable at times I believe I used this too often out
of convenience. It became very easy to modify object behavior (e.g. changing Bouncer theta after a collision). 
However, the cost of violating the open closed principle should be taken into consideration when naively
passing in entire objects when only some of their data is required for the method. Additionally, I would often pass
in the global Group for processing (e.g. Bouncer Group to add new Bouncers to it). This may not be the best design 
practice, especially considering the open closed principle, as it allows other classes to change an entire Group
of objects that are operating on the game.

To add new features to the project, an ideal object oriented practice would be to extend classes to add in new
functionality. Unfortunately I was unaware of this ability at the time of the project, but since learning
about the concept in class I see its immediate benefits for design flexibility. For example, to create new types
of Bricks, the Brick class could be extended to create other types of Bricks (e.g. PermanentBricks). With the current
design however, this would also require changing line 225 (in the checkLevelClear method in Game.java)
to instead of verifying that no bricks exist, only making sure that all of a certain type of Brick (e.g. point
bricks) are gone. On the other hand, adding in features such as cheat keys has been made very easy
by design of global Groups containing all objects in the scene, as well as in the creation of the objects themselves.
For example, to add a cheat key that speeds up balls, a KeyCode would be assigned, and then the Bouncer class
would be handed all Bouncers in the group and update their speeds.