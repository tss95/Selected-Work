# [Obligatory Assignment 2: “Battleship”](https://retting.ii.uib.no/inf101.v19.sem2/blob/master/SEM-2.md)


* **README**
* [Assignment instructions](SEM-2.md)

This project contains [obligatory assignment 2](SEM-2.md). You can also [read the assignment instructions online]
(https://retting.ii.uib.no/inf101.v19.oppgaver/inf101.v19.sem2/blob/master/SEM-2.md) (may contain small updates that aren't in your local copy).

**Deadline:**
* The entire assignment has to be handed in by **Friday the 26th of April 23:59** 
* [Tips for a smooth(er) hand-in (same as for SEM-1)](https://retting.ii.uib.no/inf101/inf101.v19/wikis/innlevering)

**You have to indicate which parts of the assignment you've completed and if the assignment is ready for grading in the section below in your README.md.**

**Extension:** You can ask the teaching assistants for a deadline extension if needed. You should already have completed parts of the assignment and pushed
these to the repository at before the deadline when asking for an extension.
   * A day or two extension is fine without motivation.
   * For longer extensions, we need a concrete reason for the delay. Please also contact us if there are [other things we need to consider]
   (http://www.uib.no/student/49241/trenger-du-tilrettelegging-av-ditt-studiel%C3%B8p).

# Progress report - Please add answers/descriptions/comments to your project below 
* Assignment by: *Tord Sture Stangeland* (*gus008*)
* [x] Assignment is ready for grading!
* Code review:
   * [ ] I've received feedback from @username, ...
   * [ ] I've given feedback to @username, ...
* Checklist:
   * [X] A playable Battleship game
   * [x] Explanation and motivation for how the game is implemented, e.g., design decisions, how the code is organized, what abstractions are used, etc.
   * [X] Tests
   * [X] Documentation (JavaDoc, comments, diagrams, README, etc.)
   * [X] Carefully named classes, interfaces, methods and variables.
   * [X] Carefully chosen abstractions and encapsulation (use of classes, interfaces, methods, etc).

### Running instructions
* To play the game, run: `Main.class`

## Overview
*(overview of your code and what you've done)*
The code is split up into 4 major interfaces: IUserInterface, IUIGame, IShip, IActor. These interfaces are central to the
code, with the Game class as the point of connection.

The Game class initiates all the non UI classes of the game. This class stores the grid information of the ships, takes user input and directs to the other classes,
changes the functionality of the Button class, handles the gamePhase and determines what is to be available for the player as well as cues the AI when to fire and place ships.
Game phase is really a key element of the code. This integer is the determinant of which methods are available, which methods does what, which methods should not be called etc.
The Game class also has a counter iterating over the Ship classes, which simplifies the code significantly.

The Ship classes are classes for each Ship that are available in the official game. These classes contain methods from everything from initializing the ship and give it default values, to rotating
and storing information about the ship. The ships are not exclusive to any player, so each class is used twice: once for the player and once for the AI.

The Player class implements the IPlayer interface which is an extension of the IActor interface. It is a class that stores information about the player, their decisions
and the class also has handlesDamage which handles the damage(obviously) done to them by the AI. The handleDamage() method in player is central to the functionality of the AI.

The Computer class implements the IPc interface which is also an extension of th IActor interface. This class does everything for the AI, but the handleDamage in the Player class.
The class stores the location of its' ship placements, the shots it has fired in lists of hits and misses. In gamePhase 1 The AI places ships in random valid locations on the grid, and
stores their information. In phase 3 the AI attacks. The AI has two types of attacks: a random attack on the board, and an attack followed by a recent hit. The random attack can not be
anywhere on the board. Given an odd number of columns, the AI attacks odd numbered columns on even rows, and even numbered columns on odd numbered rows (see design choices for more information).
The other attack is an attack followed by a recent hit. When the AI has hit something it will use infoHandler() to determine information about the current attack sequence (attacks following a hit
until a ship has been sunk). The next time the fire() method is called, the shot is determined by findNextGoodMove(). This method uses the information from infoHandler() to determine the next move.



## Design Choices
I decided early on to have the Game class handle the majority of the game logic. Using gamePhase as the director of the code, I combined all the classes in this one class. Having a clear divider in
what was to happen at each stage was a very useful tool that allowed me to organize and reuse methods efficiently. The game handles the placements of the 5 ships, which has to happen once for each participant.
Therefore, I needed an efficient way to handle repetitive tasks on a lot of classes. ShipNr() was excellent for this task, and has been used in many of the classes to great success.
However, so many features clustered into one class did become my greatest regret later in the development (see lessons learned).

Since there are only 5 different ships in the game, it made sense to have a class for each ship, and an interface to connect them all together. This is also a decision I wish I had done differently
as there classes are all the same. The only differences between them are their lengths and their names. This could easily have been handled by a single class, with name and length as parameters.
As far as the rotation of the ship is concerned, I decided to only have a 90 degree rotation. I realized that even if I allowed it to spin all the way around, the result would be the same:
either the ship is oriented vertically or the ship is oriented horizontally.

I decided to keep the UI fairly basic, keeping its initial code intact. I contemplated having two grids, but ultimately decided against it as it did not really add much to the game.
However, I did not separate the UI from the games' logic which was a major mistake. Due to time constraints I was unable to create a functioning version of the game, with this in mind, in time.
My decision to only have one button and to make it so versatile was very useful, however not very user friendly. It did however, make having a single board simpler and more distinct for the user.
The action of having to press the button is a clear divider between one screen and the other.

My implementation of the semi-smart AI made a big impact on the game for me. Having a working AI that does not only make random decisions turns the game into a challenge, which all games should be.
I decided fairly early on that I wanted to do this and as it is a simple game it shouldn't be difficult. 30 hours later it worked. As I mentioned in overview, there are two kinds of attack: random
and an attack followed by a recent hit. I knew that since the length of the shortest ship is 2, the ship has to cover at least 2 cells. Therefore, the AI does not need to try every single cell,
only every other. This reduces the amount of shots to search the entire board by half. The other type of attack, the one followed by a hit is a bit trickier. I decided that the AI needed a lot of
information about the meanings of misses and hits in order to determine a good move. Following this, I created the infoHandler() method. This method uses the data of previous shots to
determine important information about the board. This information is then used by findGoodNextMove() to produce a logical next move.

I decided to create interfaces that correspond to each category of classes. I have IShip for the ships, IUIGame for the Game class and IActor for the player and AI.
However since the AI and player have some differing methods I created extensions of IActor called IPc and IPlayer.



### Abstractions
*(how elements of the game are represented by classes/objects)*
The UMLs that are in each folder can be viewed by either installing a plantUML viewer or by posting the code in this website: https://www.planttext.com/
I will also post screenshots in the UML folder.

The ships are represented by 5 individual classes and united by the IShip interface and created in the game class.

The Game class implements the IUIGame interface and is the creator of all the other classes except for the UI.

The Computer class implements IPc which is an extension of the IActor interface.

The Player class implements IPlayer which is an extension of the IActor interface.

UI implements IUserInterface.


### Lessons learned
*(What choices turned out to work well or less well? Is there something you'd have done differently?)*
One of major mistakes have been to try to make the Game class do too much. I started a version of the game where I separated the class into three more classes.
Game, Board and Rules. This would have allowed for much simpler and dynamic changes to the game, the board and its rules. I could still have had my GamePhases
controlling the main phases of the game while leaving the rest of the game more organized, simpler and dynamic. This would also have created more separation of my methods,
leading to better and easier testing. However, time would not permit it.

Another major mistake has been my lack of separation between UI and the game logic. This has made testing nearly impossible, and has just lead to a messy product overall.
If I were to take this direction I could have created a class that connects the game with the UI, updating it every move, depending on information, and not updating it as
a part of the other methods.

I also wish I reduced to only one Ship class. Making one change to one of the classes and then having to copy and paste all the time has been time consuming and inefficient.
Since the number of ships are so low, this has not been a major issue for me, however on a bigger project this would be very important.

I did learn a lot about the difficulties of making a good AI. It was really challenging, yet cool, to break down the game to its core elements and write those elements
into rules for the game. It definitely ruined the fun of the game though.


## Testing
*(how have you tested your implementation?)*
My testing is limited due to my code structure. I was able to test some aspects of the game, but since I did not separate the UI from the game logic I had a hard time
testing all aspects of the game. In addition, my AI code is way too long and should have been divided into different sections in order to be more readable as well as
testable.

## Features / Bugs
*(what works and doesn't work)*
Bugs:
 - When placing the ships, you sometimes have to click multiple times in order for the ship to be placed.
 - You can fire at the same cell twice and it will register as a shot.
 - Rarely, but sometimes you are able to place the ships on top of each other. They are not placed correctly however and will create a weird circular ship, ultimately crashing the game.
 - The AI after a hit with no pattern or edge may fire a shot on a cell that has already been shot at. This is rare however, due to the random shot pattern.
 - The checkWinCondition() can trigger incorrectly in very specific scenarios. This could also be viewed as a feature if you are on the winning side.

 Features:
 - Fully playable Battleship game for one player
 - Good AI
 - Multifunctional button


## Code review experience
*(did you learn anything from review of your code or someone else's code?)*
From reviewing my own code I determined that there a lot of ways I could simplify my code. I should also start using other types of lists that List and ArrayList,
as the features of other types can reduce the amount of code I have to write. I should also work on planning further when I start to code, as workarounds
of bad implementation is time consuming and inefficient, and often leads to a tangled mess that can break the project. I should start writing shorter methods
that do less. This can improve readability, debugging and testing tremendously. Less is more and separation of UI and game logic sums up what is mostly wrong
with my code. Taking time to improve code rather than just adding is something that I will work on in the future.
