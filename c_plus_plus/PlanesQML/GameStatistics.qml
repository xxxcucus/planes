import QtQuick 2.14
import QtQuick.Controls 2.14
import QtQuick.Layouts 1.11

Rectangle {
    id: gameStats
    width: parent.width
    height: parent.height
    property int textSize : width / 20
    //TODO: to rename
    property string textColorGame : "blue"
    property string backColorGame : "green"
    property string textColorGlobal : "red"
    property string backColorGlobal : "blue"
    property int playerMoves : 0
    property int playerHits : 0
    property int playerDead : 0
    property int playerMisses : 0
    property int playerWins : 0
    property int computerMoves : 0
    property int computerHits : 0
    property int computerDead : 0
    property int computerMisses : 0
    property int computerWins : 0
    property int draws : 0

    Rectangle {
        id: score
        color: gameStats.backColorGame
        anchors.top: parent.top
        width: parent.width
        height: parent.height * 3 / 4

        ColumnLayout {
            anchors.top: parent.top
            anchors.centerIn: parent
            anchors.margins: gameStats.width / 10

            Label {
                font.pixelSize: gameStats.textSize
                text: "Player"
                color: gameStats.textColorGame
            }

            Label {
                text: "BlaBla"
                color: gameStats.backColorGame
            }

            GridLayout {
                columns: 2
                columnSpacing:  gameStats.width/10

                Label {
                    font.pixelSize: gameStats.textSize
                    text: "Number of moves"
                    color: gameStats.textColorGame
                }
                Label {
                    font.pixelSize: gameStats.textSize
                    id: noMovesPlayer
                    text: gameStats.playerMoves
                    color: gameStats.textColorGame
                }
                Label {
                    font.pixelSize: gameStats.textSize
                    text: "Number of misses"
                    color: gameStats.textColorGame
                }
                Label {
                    font.pixelSize: gameStats.textSize
                    id: noMissesPlayer
                    text: gameStats.playerMisses
                    color: gameStats.textColorGame
                }
                Label {
                    font.pixelSize: gameStats.textSize
                    text: "Number of hits"
                    color: gameStats.textColorGame
                }
                Label {
                    font.pixelSize: gameStats.textSize
                    id: noHitsPlayer
                    text: gameStats.playerHits
                    color: gameStats.textColorGame
                }
                Label {
                    font.pixelSize: gameStats.textSize
                    text: "Number of planes guessed"
                    color: gameStats.textColorGame
                }
                Label {
                    font.pixelSize: gameStats.textSize
                    id: noGuessedPlayer
                    text: gameStats.playerDead
                    color: gameStats.textColorGame
                }
            }

            Label {
                text: "BlaBla"
                color: gameStats.backColorGame
            }

            Label {
                font.pixelSize: gameStats.textSize
                text: "Computer"
                color: gameStats.textColorGame
            }

            Label {
                text: "BlaBla"
                color: gameStats.backColorGame
            }

            GridLayout {
                columns: 2
                columnSpacing:  gameStats.width/10
                anchors.margins: gameStats.width/10

                Label {
                    font.pixelSize: gameStats.textSize
                    text: "Number of moves"
                    color: gameStats.textColorGame
                }
                Label {
                    font.pixelSize: gameStats.textSize
                    id: noMovesComputer
                    text: gameStats.computerMoves
                    color: gameStats.textColorGame
                }
                Label {
                    font.pixelSize: gameStats.textSize
                    text: "Number of misses"
                    color: gameStats.textColorGame
                }
                Label {
                    font.pixelSize: gameStats.textSize
                    id: noMissesComputer
                    text: gameStats.computerMisses
                    color: gameStats.textColorGame
                }
                Label {
                    font.pixelSize: gameStats.textSize
                    text: "Number of hits"
                    color: gameStats.textColorGame
                }
                Label {
                    font.pixelSize: gameStats.textSize
                    id: noHitsComputer
                    text: gameStats.computerHits
                    color: gameStats.textColorGame
                }
                Label {
                    font.pixelSize: gameStats.textSize
                    text: "Number of planes guessed"
                    color: gameStats.textColorGame
                }
                Label {
                    font.pixelSize: gameStats.textSize
                    id: noGuessedComputer
                    text: gameStats.computerDead
                    color: gameStats.textColorGame
                }
            }

            Label {
                text: "BlaBla"
                color: gameStats.backColorGame
            }

            Label {
                font.pixelSize: gameStats.textSize
                text: "Global Score"
                color: gameStats.textColorGame
            }

            Label {
                text: "BlaBla"
                color: gameStats.backColorGame
            }

            GridLayout {
                columns: 2
                columnSpacing:  gameStats.width/10
                anchors.margins: gameStats.width/10

                Label {
                    font.pixelSize: gameStats.textSize
                    text: "Player Wins"
                    color: gameStats.textColorGame
                }
                Label {
                    font.pixelSize: gameStats.textSize
                    text: gameStats.playerWins
                    color: gameStats.textColorGame
                }
                Label {
                    font.pixelSize: gameStats.textSize
                    text: "Computer Wins"
                    color: gameStats.textColorGame
                }
                Label {
                    font.pixelSize: gameStats.textSize
                    text: gameStats.computerWins
                    color: gameStats.textColorGame
                }
                Label {
                    font.pixelSize: gameStats.textSize
                    text: "Draws"
                    color: gameStats.textColorGame
                }
                Label {
                    font.pixelSize: gameStats.textSize
                    text: gameStats.draws
                    color: gameStats.textColorGame
                }                
            }
        }
    } //current game statistics - green rectangle

    Rectangle {
        id: gameEnd
        color: gameStats.backColorGlobal
        width: parent.width
        anchors.top: score.bottom
        anchors.bottom: gameStats.bottom

        Rectangle {
            id: gameEndHolder
            width: parent.width
            height: parent.height/2
            anchors.top : parent.top
            color: gameStats.backColorGlobal

            Label {
                id: gameEndMessage
                anchors.centerIn: parent
                font.pixelSize: gameStats.textSize
                text: ""
                color: gameStats.textColorGlobal
            }
            onWidthChanged: {
                if (playAgainButton.visible == true)
                    gameEndMessage.font.pixelSize = 30
            }
            onHeightChanged: {
                if (playAgainButton.visible == true)
                    gameEndMessage.font.pixelSize = 30
            }
        }

        Rectangle {
            width: parent.width
            height: parent.height/2
            anchors.top : gameEndHolder.bottom
            color: gameStats.backColorGlobal
            PlayAgain {
                id: playAgainButton
                anchors.centerIn : parent
                visible: false
                width: parent.width * 2 / 3
                height : parent.height * 2 / 3
            }
        }

        Connections {
            target: PlaneGame
            function onRoundEnds(isPlayerWinner, isDraw) {
                gameEndMessage.text = isPlayerWinner ? "Player wins!" : "Computer wins!"
                gameEndMessage.text = isDraw ? "Draw !" : gameEndMessage.text
                playAgainButton.visible = "true"
                playAgainButton.state = "Enabled"
                gameEndMessage.color= gameStats.textColorGlobal
                gameEndMessage.font.pixelSize = gameStats.textSize
                anim.start()
            }
        }
    }

    SequentialAnimation {
        id: anim
        loops: 5
        PropertyAnimation { target: gameEndMessage; property: "font.pixelSize"; to: "30"; duration: 500 }
        PropertyAnimation { target: gameEndMessage; property: "font.pixelSize"; to: gameStats.textSize; duration: 500 }
        PropertyAnimation { target: gameEndMessage; property: "font.pixelSize"; to: "30"; duration: 500 }
    }
}
