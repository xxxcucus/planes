import QtQuick
import "ButtonPaintFunctions.js" as PaintFunctions

Rectangle {
    id: back
    property color enabledColor: "red"
    property color disabledColor: "lightGray"
    state: "Disabled"
	radius: 10

    Canvas {
        id: playAgainCanvas
        anchors.centerIn: parent
        width: parent.width
        height: parent.height
        onPaint: {
            var ctx = getContext("2d")
            PaintFunctions.playAgainButton(ctx)
            //console.log("Play Again paint")
        }
    }

    MouseArea {
        width: parent.width
        height: parent.height
        onClicked: {
            if (back.state == "Enabled") {
                anim.start()
                PlaneGame.startNewGame()
                PlayerPlaneGrid.initGrid()
                ComputerPlaneGrid.initGrid()
                rightPane.currentTab = 0
                leftPane.currentTab = 1
                rightPane.computerBoardState = "BoardEditing"
                rightPane.playerBoardState = "BoardEditing"
                playAgainButton.visible = false
                gameEndMessage.color= gameStats.backColorGlobal
            }
        }
    }

    SequentialAnimation {
        id: anim
        PropertyAnimation { target: back; property: "color"; to: "green"; duration: 50 }
        PropertyAnimation { target: back; property: "color"; to: "red"; duration: 50 }
    }

    states: [
            State {
                name: "Enabled"
                PropertyChanges {
                    target: back
                    color: back.enabledColor
                }
                StateChangeScript {
                    script: playAgainCanvas.requestPaint()
                }
            },
            State {
                name: "Disabled"
                PropertyChanges {
                    target: back
                    color: back.disabledColor
                }
            }
        ]
}
