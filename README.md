# Minesweeper

## Game Play
### How to Play the game
The game is played by using the left mouse click to reveal a hex cell, the right mouse click to flag a cell with a suspected mine. The goal is to reveal all non-mine cells by clicking on them, using the displayed numbers to determine mine locations and the flags to mark the location. The game is won by uncovering all unmined cells, and is lost when a mine is hit.
### How the Game is Scored
The game is scored using the number of flags remaining and the time since the game has begun. The number of flags available will start with how many mines are on the grid, and count down as they are placed. The timer starts when the first click is made, and a faster time means a better played game.

## Description of Program Internals
### Description of Classes
HexMines.java is the main class that puts together all of the other classes. It begins the game and assembles different parts of the GUI frame to create the game. It also creates a button to change the game difficulty, with the harder game being a bigger grid with more mines.
GameBoardGUI.java assembles the main game board panel for the GUI. It creates the hex cells, puts them into the grid, handles left and right clicks, and checks for a win or loss after each click.
HexMineManager.java handles the bookkeeping for the game. It creates the minesweeper board by randomly placing mines, takes the appropriate action when a cell is uncovered, toggles a flag, and checks the board to see if the game has been won or lost.
NumFlagsGUI.java creates an individual panel that displays the number of flags used. If a flag is placed the number goes down and if a flag is removed the number goes up. The number of flags starts at the number of mines on the board.
TimerGUI.java creates a panel to display the timer for the game, which starts when the first click is made and stops when the game is ended.
GameEndGUI.java creates an option pane that will be displayed when the game is over. There is a seperate win and loss message, and the user is given the option to play another game or to close the game.
### Algorithm Details
The Hex Coordinates are made using a rougly rectangular grid that is an odd-q vertical layout and the associated geometry of these grids. Mines are placed randomly each game onto the grid of the specified size. Uncovering cells and neighbors is done based on what is under the uncovered area. If the cell is blank, all adjacent blank and numbered cells are uncovered. If the cell is numbered, only that cell is uncovered. If the cell has a mine, all unmined cells are uncovered and the game is lost. The end of a game is detected by checking if a mine has been hit (win) or if all covered/flagged cells have a mine.

## Extras
The game is themed based on the Lorax. Flags are Truffula trees and mines are the Lorax. When a game is started, background music from the Lorax begins playing, and is reset when a new game is started. A custom message is displayed at the end of a game that differs based on a win or loss.

## Known Bugs and Feature Requests
Minesweeper could have a feature that would allow the first click of the game to uncover a large area, insteading of potentially hitting a mine or just uncovering a number. This would be implemented by not placing the mines on the grid until after the first click, and simply starting with uncovering a set amount of cells adjacent to the first clicked cell.
