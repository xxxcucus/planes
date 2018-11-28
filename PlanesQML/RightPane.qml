import QtQuick 2.9
import QtQuick.Controls 2.3
import QtQuick.Layouts 1.3


Rectangle {
    width: parent.width * 2 / 3
    height: parent.height
    property alias currentTab : bar.currentIndex
    property alias computerBoardState : computerBoard.state
    property alias playerBoardState : playerBoard.state

    TabBar {
        width: parent.width
        id: bar
        TabButton {
            text: "Player Board"
        }
        TabButton {
            text: "Computer Board"
        }
        TabButton {
            text: "Help"
        }
    }

    onWidthChanged: {
        //console.log("Right pane width changed")
        if (stackLayout.currentIndex == 0)
            PlayerPlaneGrid.resetModel()
        if (stackLayout.currentIndex == 1)
            ComputerPlaneGrid.resetModel()
    }

    onHeightChanged: {
        //console.log("Right pane height changed")
        if (stackLayout.currentIndex == 0)
            PlayerPlaneGrid.resetModel()
        if (stackLayout.currentIndex == 1)
            ComputerPlaneGrid.resetModel()
    }


    StackLayout {
        id: stackLayout
        anchors.top: bar.bottom
        currentIndex: bar.currentIndex
        width: parent.width
        height: parent.height - bar.height

        GenericBoard {
            boardModel: PlayerPlaneGrid
            id : playerBoard
            isComputer: false
        }

        GenericBoard {
            boardModel: ComputerPlaneGrid
            id : computerBoard
            isComputer: true
        }

        Rectangle {
           color: 'yellow'
           width: parent.width
           height: parent.height
		   
		   
		  Text {
				text: '<html>
					<head>
					<title> How to play the game of \"Planes\" </title>
					</head>

					<body>

					<div>

					<h3> Winning a Game </h3>
					<p> In order to win you must win more rounds as the computer\. </p>

					<h3> Players </h3>
					<p> There are two players: the computer and you\. </p>

					<h3> Score </h3>
					<p> The game consists of more rounds\. The overall score is displayed near the \"Start new round\" button in the \"Start Round\" Tab. </p>

					<h3> Game Board </h3>
					<p> The game board is a chess board like square grid\. You must place 3 planes on the grid and the computer as well 3 on its own\. You will not be able to see where the computer has placed its planes\. 
					The placed planes should not overlap and should not lie outside the board\. Near the allowed area for the planes\, 3 more square layers\, marked with a different color are added\. 
					These will allow you to rotate the plane outside the playable board when positioning it\.
					</p>

					<h3> Game </h3>
					<p> The game consists of a set of rounds\. The first round will start immediately when the program is started\. The other rounds are started by clicking on the \"Start new round\" button
					in the "Start Round" Tab\. </p>

					<h3> Winning a Round </h3>
					<p> In order to win a round you must guess the position of the heads of the planes placed by the computer on its board before the computer guesses the same for your planes\.</p>

					<h3> Round </h3>
					<p> A round has two phases\: during the first you position the planes on your grid\, while during the second you must guess the position of the planes position by the computer on its grid\.
					To position your planes you select a desired plane and then with the corresponding buttons in the interface you can translate it to the left\, or to the right\, upwards or downwards or rotate it\.
					 The selected 
					plane can be changed with the help of a selection button\. Once you have positioned your planes you can click on the done button to start guessing the computer\'s planes\. Guessing
					is achieved by clicking on the squares where you think a plane head lies\. The computer will mark the square with a circle when you did not click on a plane\, with a triangle when you have
					guessed a plane point but not a plane head and with a X when you have guessed a plane head\. The statistic of your attempts is given in the left panel near the game board\. 
					Similarly a statistic for the computer\'s attempts is displayed as well\. </p>
					</div>

					</body>


					</html>'
				// For text to wrap, a width has to be explicitly provided
				width: parent.width - 10
				height: parent.height - 10

				// This setting makes the text wrap at word boundaries when it goes
				// past the width of the Text object
				wrapMode: Text.WordWrap
				
				x : 5
				y : 5
			}

        }
    }

}
