# Sudoku Game on P2P Networks

## Problem statement
A P2P Sudoku challenge games. Each user can place a number of the sudoku game, if it is not already placed takes 1 point, if it is already placed and it is rights takes 0 point, in other case receive -1 point. The game is based on 9 x 9 matrix. All users that play to a game are automatically informed when a user increments its score, and when the game is finished.

## Team members
- Alessandra Orsi (Leader)
- Giulio Imperato
- Vincenzo Nastro

## Implementation

#### Functionalities of the system
All functionalities of the system are defined in SudokuGame interface and implemented in SudokuGameImpl class. These functionalities are:
-	generateNewSudoku, which permits to create new games;
-	join, which permits users to join in a game;
-	getSudoku, which permits to get the Sudoku matrix game with only the number placed by the user;
-	placeNumber, which permits to place a new solution number in the game.

In SudokuGameImpl class we have added other three useful methods:
-	sendMessage, for managing the sending of messages to the other peers (that are playing in the same challenge) when a user increments its score and when the game is finished;
-	verifyChallengeEnded, to verify if the challenge is finished;
-	victory, to verify who is the winner of a specific challenge.

### Additional Classes
In addition to the two classes already introduced in the previous paragraph (SudokuGame and SudokuGameImpl) , we have created other classes:
- SudokuChallenge, which is the class that describes the Sudoku challenge. Indeed this class contains the Sudoku, the list of peers that joined the Sudoku challenge and the list of scores of peers;
- Sudoku, which is the class that permits to generates a new game and also contains all informations about the Sudoku;
- SudokuGUI, which is the class that creates the Graphical User Interface;
- SudokuStart, which is the class that permits to boot the Sudoku Game with the graphic.

All these classes make use of other classes belonging to a Github repository that we have discovered:
- Generator, that generates random Sudoku Grids of various complexity;
- Grid, that represents a Sudoku Grid and features a variety of convenient methods;
- Solver, that solves any provided Grid using backtracking.

For more details see the following repository: https://github.com/a11n/sudoku.git

### GUI
In order to make the Sudoku Game more user-friendly we have created a graphical interface that, in addition to showing the various challenges, it also keeps track of scores and allows users to create new games and/or join in existing challenges.
In detail, you can search for the challenges that have been created and by double clicking on them you can join and play. When you need to enter a number in a cell, after entering it, press enter so that the placeNumber is successful.

## Testing
The sudoku used as an example in our tests is as follows:

[![Schermata_2018-03-06_alle_17.02.35.png](https://s14.postimg.org/s7ym8bdbl/Schermata_2018-03-06_alle_17.02.35.png)](https://postimg.org/image/y8wb5dzxp/)
### JUnit Test
The test cases analyzed are in the SudokuGameTest class and are as follows:
- 	One test for the "generateNewSudoku" method, in which we create two challenges with the same name (this is not possible);
- 	three tests for the "join" method with which we analyze a successful join, one that is unsuccessful because a user tries to join in a nonexistent challenge and then we test a join that fails because a peer tries to join in a challenge with the same nickname as another existing peer;
- 	five tests for the "placeNumber" method with which we analyze a placeNumber that does not succeed because the user tries to insert a number in a nonexistent challenge, one that fails because the user inserts a invalid value for a given cell, another that fails because the user inserts a valid value but incorrect (-1 point), then a successful placeNumber because the user inserts a valid and correct value (+1 point) and finally a placeNumber that fails because the user tries to insert a valid and correct value but already filled (0 points).

### Test 
In addition to testing the system with JUnit we have also developed a class called SudokuGameMain in which we create four peers and simulate the various methods provided by the interface and implemented by us in the SudokuGameImpl class. Furthermore, in this SudolkuGameMain we have also added a utility method [convert(Integer [][] sudoku)] that  converts the matrix of integers into a Grid.

## Instructions for execution
- JUnit tests were made by setting a fixed seed in the SudokuChallenge class (int seed = 71), so to execute them you simply need to run the SudokuGameTest class;
- To execute the system test without graphics you need to run the class SudokuGameMain (always using the default seed = 71);
- To run the system including the graphics, you have to do the run configuration of the SudokuStart class initially setting the 0 as the argument so as to allow the master peer (id = 0) to register and act as host for the other peers that will register in the future. After that, every time you want to insert another peer, you have to put as argument another peerID (different from 0) and a nickname (ex. 1 Alessandra).
You can run this class using the default seed that has been set for the JUnit tests or you can use a seed that is created randomly at each run. If you want to do this just comment on the setting of seed = 71 and uncomment the part where it is created randomly (always in the SudokuChallenge class).