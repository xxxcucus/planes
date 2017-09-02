import QtQuick 2.0

Rectangle {
    id: back
    color: "red"
    property bool pressed: false
    signal clicked

    Canvas {
        anchors.fill: parent
        width: back.width
        height: back.height

        onPaint: {
            var ctx = getContext("2d")
            ctx.fillStyle = 'blue'
            ctx.lineWidth = 4

            var centerX = width/2
            var centerY = height/2
            var radius = Math.min(width/3, height/3)

            ctx.fillRect(centerX - radius/5, centerY - radius/2 + radius/3, radius*2/5, radius)

            ctx.beginPath()
            ctx.moveTo(centerX, centerY - radius/2 + radius/3)
            ctx.lineTo(centerX - radius/3, centerY - radius/2 + radius/3)
            ctx.lineTo(centerX, centerY - radius/2 - radius/3)
            ctx.lineTo(centerX + radius/3, centerY - radius/2 + radius/3)
            ctx.closePath()
            ctx.fill()

        }
    }

    MouseArea {
        width: parent.width
        height: parent.height
        onClicked: {
            console.log("Plane upwards clicked")
            anim.start()
        }
    }

    SequentialAnimation {
        id: anim
        PropertyAnimation { target: back; property: "color"; to: "green"; duration:50 }
        PropertyAnimation { target: back; property: "color"; to: "red"; duration: 50 }
        }
}
