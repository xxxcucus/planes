import QtQuick 2.9
import "ButtonPaintFunctions.js" as PaintFunctions

Rectangle {
    id: back

    property color enabledColor: "red"
    property color disabledColor: "lightGray"

    color: enabledColor

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
        onPlanePositionNotValid: {
            (val == true) ? back.state = "Disabled" : back.state = "Enabled"
        }
    }

    MouseArea {
        width: parent.width
        height: parent.height
        onClicked: {
            console.log("Done clicked")
            if (state == "Enabled")
                anim.start()
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
