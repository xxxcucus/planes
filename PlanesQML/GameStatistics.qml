import QtQuick 2.0
import QtQuick.Controls 2.2
import QtQuick.Layouts 1.3

Rectangle {
    id: back
    width: parent.width
    height: parent.height
    color: "blue"
    property int textSize : width / 20
    property string textColor : "blue"
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
        color: "green"
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
                color: back.textColor
            }

            Label {
                text: "BlaBla"
                color: "green"
            }

            GridLayout {
                columns: 2
                columnSpacing:  back.width/10

                Label {
                    font.pixelSize: back.textSize
                    text: "Wins"
                    color: back.textColor
                }
                Label {
                    font.pixelSize: back.textSize
                    id: noWinsPlayer
                    text: back.playerWins
                    color: back.textColor
                }
                Label {
                    font.pixelSize: back.textSize
                    text: "Number of moves"
                    color: back.textColor
                }
                Label {
                    font.pixelSize: back.textSize
                    id: noMovesPlayer
                    text: back.playerMoves
                    color: back.textColor
                }
                Label {
                    font.pixelSize: back.textSize
                    text: "Number of misses"
                    color: back.textColor
                }
                Label {
                    font.pixelSize: back.textSize
                    id: noMissesPlayer
                    text: back.playerMisses
                    color: back.textColor
                }
                Label {
                    font.pixelSize: back.textSize
                    text: "Number of hits"
                    color: back.textColor
                }
                Label {
                    font.pixelSize: back.textSize
                    id: noHitsPlayer
                    text: back.playerHits
                    color: back.textColor
                }
                Label {
                    font.pixelSize: back.textSize
                    text: "Number of planes guessed"
                    color: back.textColor
                }
                Label {
                    font.pixelSize: back.textSize
                    id: noGuessedPlayer
                    text: back.playerDead
                    color: back.textColor
                }
            }

            Label {
                text: "BlaBla"
                color: "green"
            }

            Label {
                font.pixelSize: back.textSize
                text: "Computer"
                color: back.textColor
            }

            Label {
                text: "BlaBla"
                color: "green"
            }

            GridLayout {
                columns: 2
                columnSpacing:  back.width/10


                Label {
                    font.pixelSize: back.textSize
                    text: "Wins"
                    color: back.textColor
                }
                Label {
                    font.pixelSize: back.textSize
                    id: noWinsComputer
                    text: back.computerWins
                    color: back.textColor
                }

                Label {
                    font.pixelSize: back.textSize
                    text: "Number of moves"
                    color: back.textColor
                }
                Label {
                    font.pixelSize: back.textSize
                    id: noMovesComputer
                    text: back.computerMoves
                    color: back.textColor
                }
                Label {
                    font.pixelSize: back.textSize
                    text: "Number of misses"
                    color: back.textColor
                }
                Label {
                    font.pixelSize: back.textSize
                    id: noMissesComputer
                    text: back.computerMisses
                    color: back.textColor
                }
                Label {
                    font.pixelSize: back.textSize
                    text: "Number of hits"
                    color: back.textColor
                }
                Label {
                    font.pixelSize: back.textSize
                    id: noHitsComputer
                    text: back.computerHits
                    color: back.textColor
                }
                Label {
                    font.pixelSize: back.textSize
                    text: "Number of planes guessed"
                    color: back.textColor
                }
                Label {
                    font.pixelSize: back.textSize
                    id: noGuessedComputer
                    text: back.computerDead
                    color: back.textColor
                }
            }
        }
    }
}
