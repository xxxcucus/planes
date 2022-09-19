import QtQml.Models

ListModel {

	ListElement {
		content: ""
		name: "How to play the game of Planes"
	}

    ListElement {
        content: "In order to win you must win more rounds as the computer."
        name: "Winning a Game"
    }
	
    ListElement {
        name: "Players"
        content: "There are two players: the computer and you."
    }
	
	ListElement {
		name: "Score"
		content: "The game consists of more rounds. The overall score is displayed in the left pane during the second phase of each round."
	}
	
	ListElement {
		name: "Game Board"
		content: "The game board is a chess board like square grid. You must place 3 planes on the grid and the computer as well 3 on its own. You will not be able to see where the computer has placed its planes. 
The placed planes should not overlap and should not lie outside the board. Near the allowed area for the planes\, 3 more square layers\, marked with a different color are added. 
These will allow you to rotate the plane outside the playable board when positioning it."
	}
	
	ListElement {
		name: "Game"
		content: "The game consists of a set of rounds. The first round will start immediately when the program is started. The other rounds are started by clicking on the \"Play Again\" button
in the left pane at the end of a round."
	}

	ListElement {
		name: "Winning a Round"
		content: "In order to win a round you must guess the position of the heads of the planes placed by the computer on its board before the computer guesses the same for your planes."
	}

	ListElement {
		name: "Round"
		content: "A round has two phases: during the first you position the planes on your grid\, while during the second you must guess the position of the planes position by the computer on its grid.
To position your planes you select a desired plane and then with the corresponding buttons in the interface you can translate it to the left\, or to the right\, upwards or downwards or rotate it.
 The selected 
plane can be changed with the help of a selection button. Once you have positioned your planes you can click on the done button to start guessing the computer's planes. Guessing
is achieved by clicking on the squares where you think a plane head lies. The computer will mark the square with a circle when you did not click on a plane\, with a rhombus when you have
guessed a plane point but not a plane head and with a X when you have guessed a plane head. The statistic of your attempts is given in the left panel near the game board. 
Similarly a statistic for the computer's attempts is displayed as well."
	}

}