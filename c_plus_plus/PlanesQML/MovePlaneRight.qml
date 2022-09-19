import QtQuick
import "ButtonPaintFunctions.js" as PaintFunctions

Rectangle {
    id: back
    color: "red"
	radius: 10

    Canvas {
        anchors.fill: parent
        width: back.width
        height: back.height

        onPaint: {
            var ctx = getContext("2d")
            PaintFunctions.moveRightButton(ctx)
        }
    }

    MouseArea {
        width: parent.width
        height: parent.height
        onClicked: {
            //console.log("Plane right clicked")
            anim.start()
            PlayerPlaneGrid.moveRightSelectedPlane()
			PlayerPlaneGrid.verifyPlanePositionValid()			
        }
    }

    SequentialAnimation {
        id: anim
        PropertyAnimation { target: back; property: "color"; to: "green"; duration: 50 }
        PropertyAnimation { target: back; property: "color"; to: "red"; duration: 50 }
        }

}
