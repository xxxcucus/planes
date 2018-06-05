import QtQuick 2.0
import QtQuick.Controls 2.2
import QtQuick.Layouts 1.3

Rectangle {
    id: back
    width: parent.width
    height: parent.height
    property int textSize : width / 20
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

    Rectangle {
        id: currentGameScore
        color: back.backColorGame
        anchors.top: parent.top
        width: parent.width
        height: parent.height * 3 / 4

        ColumnLayout {
            anchors.top: parent.top
            anchors.centerIn: parent
            anchors.margins: back.width / 10

            Label {
                font.pixelSize: back.textSize
                text: "Player"
                color: back.textColorGame
            }

            Label {
                text: "BlaBla"
                color: back.backColorGame
            }

            GridLayout {
                columns: 2
                columnSpacing:  back.width/10

                Label {
                    font.pixelSize: back.textSize
                    text: "Number of moves"
                    color: back.textColorGame
                }
                Label {
                    font.pixelSize: back.textSize
                    id: noMovesPlayer
                    text: back.playerMoves
                    color: back.textColorGame
                }
                Label {
                    font.pixelSize: back.textSize
                    text: "Number of misses"
                    color: back.textColorGame
                }
                Label {
                    font.pixelSize: back.textSize
                    id: noMissesPlayer
                    text: back.playerMisses
                    color: back.textColorGame
                }
                Label {
                    font.pixelSize: back.textSize
                    text: "Number of hits"
                    color: back.textColorGame
                }
                Label {
                    font.pixelSize: back.textSize
                    id: noHitsPlayer
                    text: back.playerHits
                    color: back.textColorGame
                }
                Label {
                    font.pixelSize: back.textSize
                    text: "Number of planes guessed"
                    color: back.textColorGame
                }
                Label {
                    font.pixelSize: back.textSize
                    id: noGuessedPlayer
                    text: back.playerDead
                    color: back.textColorGame
                }
            }

            Label {
                text: "BlaBla"
                color: back.backColorGame
            }

            Label {
                font.pixelSize: back.textSize
                text: "Computer"
                color: back.textColorGame
            }

            Label {
                text: "BlaBla"
                color: back.backColorGame
            }

            GridLayout {
                columns: 2
                columnSpacing:  back.width/10
                anchors.margins: back.width/10

                Label {
                    font.pixelSize: back.textSize
                    text: "Number of moves"
                    color: back.textColorGame
                }
                Label {
                    font.pixelSize: back.textSize
                    id: noMovesComputer
                    text: back.computerMoves
                    color: back.textColorGame
                }
                Label {
                    font.pixelSize: back.textSize
                    text: "Number of misses"
                    color: back.textColorGame
                }
                Label {
                    font.pixelSize: back.textSize
                    id: noMissesComputer
                    text: back.computerMisses
                    color: back.textColorGame
                }
                Label {
                    font.pixelSize: back.textSize
                    text: "Number of hits"
                    color: back.textColorGame
                }
                Label {
                    font.pixelSize: back.textSize
                    id: noHitsComputer
                    text: back.computerHits
                    color: back.textColorGame
                }
                Label {
                    font.pixelSize: back.textSize
                    text: "Number of planes guessed"
                    color: back.textColorGame
                }
                Label {
                    font.pixelSize: back.textSize
                    id: noGuessedComputer
                    text: back.computerDead
                    color: back.textColorGame
                }
            }

            Label {
                text: "BlaBla"
                color: back.backColorGame
            }

            Label {
                font.pixelSize: back.textSize
                text: "Global Score"
                color: back.textColorGame
            }

            Label {
                text: "BlaBla"
                color: back.backColorGame
            }

            GridLayout {
                columns: 2
                columnSpacing:  back.width/10
                anchors.margins: back.width/10

                Label {
                    font.pixelSize: back.textSize
                    text: "Player Wins"
                    color: back.textColorGame
                }
                Label {
                    font.pixelSize: back.textSize
                    text: back.playerWins
                    color: back.textColorGame
                }
                Label {
                    font.pixelSize: back.textSize
                    text: "Computer Wins"
                    color: back.textColorGame
                }
                Label {
                    font.pixelSize: back.textSize
                    text: back.computerWins
                    color: back.textColorGame
                }
            }
        }
    } //current game statistics - green rectangle

    Rectangle {
        id: globalScore
        color: back.backColorGlobal
        width: parent.width
        anchors.top: currentGameScore.bottom
        anchors.bottom: back.bottom
    }
}
