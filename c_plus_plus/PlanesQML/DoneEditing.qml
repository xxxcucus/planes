import QtQuick
import "ButtonPaintFunctions.js" as PaintFunctions

Rectangle {
    id: back
    property color enabledColor: "red"
    property color disabledColor: "lightGray"
    state: "Enabled"
	radius: 10

    Canvas {
        anchors.fill: parent
        width: back.width
        height: back.height

        onPaint: {
            var ctx = getContext("2d")
            PaintFunctions.doneButton(ctx)
        }
    }

    Connections {
        target: PlayerPlaneGrid
        function onPlanePositionNotValid(val) {
            if (val == true) {
                back.state = "Disabled"
                //console.log("Plane position not valid")
            } else {
                back.state = "Enabled"
                //console.log("Plane position valid")
            }
        }
    }

    MouseArea {
        width: parent.width
        height: parent.height
        onClicked: {
            if (back.state == "Enabled") {
                anim.start()
                PlaneGame.doneEditing()
                PlayerPlaneGrid.doneEditing()
                ComputerPlaneGrid.doneEditing()
                rightPane.currentTab = 1
                leftPane.currentTab = 0
                rightPane.computerBoardState = "Game"
                rightPane.playerBoardState = "Game"
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
