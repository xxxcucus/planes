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
                    text: "Number of moves"
                    color: back.textColor
                }
                Label {
                    font.pixelSize: back.textSize
                    id: noMovesPlayer
                    text: "0"
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
                    text: "0"
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
                    text: "0"
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
                    text: "0"
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
                    text: "Number of moves"
                    color: back.textColor
                }
                Label {
                    font.pixelSize: back.textSize
                    id: noMovesComputer
                    text: "0"
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
                    text: "0"
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
                    text: "0"
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
                    text: "0"
                    color: back.textColor
                }
            }
        }
    }
}
