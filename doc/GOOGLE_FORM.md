# Google Form
## Max Smith

- Reflect on your planning experience: did you over- or underestimate the size of this project? 
How did thinking about the entire project before coding help or hurt your efforts? How can you plan better in the future?

I underestimated the size of this project and could have absolutely benefitted from a more critical planning/design phase.
This, in conjunction with general inexperience with Object Oriented design (but lots with functionality :)), resulted
in lots of updating on the fly. I was well aware of the general idea of Objects, and the creation of these
definitely helped my planning process. Additionally, experimenting with the JavaFX library to familiarize myself
with its capabilities and limitations was incredibly helpful in planning out the design intersection with the main library
we were using. In the future, I should spend more time and effort crafting design BEFORE I code. Ideally this design
process would be conducted with peers and mentors, where I can bounce ideas off of them and our combined perspectives can
yield better results.

- Reflect on how you managed this project: what parts did you do early, or late? 
Did you work in small or big chunks of time? How can you better manage projects in the future?

I managed this project by allocating several large chunks of time to programming. I crafted out basic functionality 
in and exploration of the JavaFX library as well as in creation of the game engine early on. As I began extending
the design and adding features, it quickly became apparent that  I would need more flexibility in the design. The result
of this realization was the creation of global Group objects. In the future, I will manage my time by allocating
more in the beginning to planning with an emphasis on design. Although design focus and functional apathy is contrary
to prior programming experiences, I see the benefits of thinking in this way.

- Reflect on your programming experience: which kinds of tasks were easiest for you and which were hardest? 
What were good uses of your time and what were bad? How can you improve your programming process in the future?

The easiest task for me was implementation, and the hardest task for me was planning and design. Good uses of time
were in the initial JavaFX exploration to get my hands dirty, as well  as time spent researching basic inheritance
(e.g. extension and super construction call) for Objects. Bad uses of time were last minute, in the scramble to meet
specifications shifting gears towards functionality and straying away from design practices. For example, in my 
implementation of the Display and SplashScreen, it was very sloppy but got the job done. A more effective implementation
would have been using panes to orient the text and show scenes. As mentioned earlier, in the future I would greatly
benefit from more critical design process.

- Reflect on your experience with GIT: do you feel you committed often enough that others could have reviewed, understood, 
and integrated your commits in a team setting? Do you feel your commit messages accurately represent your project's "story"? 
What have you learned about working with GIT on this project?

My GIT experiences were fairly positive. The accessibility and tracking of program history was beneficial during review
processes (e.g. recalling a test class that I removed from commit to commit). I believe I may have over commited code
to the repository, and it may be a better coding practice to reserve commits for bigger changes. I learned that GIT
is a very powerful tool for documentation, accessibility, and enabling code history reviews.

- Reflect on your experience with design: which parts of your code required the most editing? 
Were you able to use design proactively to make later features you added easier or was it mostly reactively by refactoring poor code?
 What have you learned about design working on this project?
 
 The most editing required was in the main Game class in how the objects were oriented and held. The creation of Global
 groups, along with the (violation of open closed principle) passing of these objects to respective classes to handle
 behavior allowed for functionality extensions, as well  as cheat key addition, trivial. Furthermore, adding and creating
 new objects in the scene was very easy. I have learned about the importance of design, and how effort on the front
 side of the project can pay dividends later on in development. I was reactively refactoring in some places (e.g. in
 the creation of the global Groups), but proactively in others (e.g. different types of Level Generation).

- If you could work on one thing right now to improve any aspect of your project, what would it be?

One thing I would work on right now in my project would be to creat an inheritance heirarchy between objects of similar
behavior. Extending Bricks to incorporate power ups and other types of bricks would be very beneficial for increasing
flexibility of feature addition. The current structure is limiting in this regard, as to create a new type of Brick the
entire Brick class must be edited.