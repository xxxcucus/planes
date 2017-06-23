import QtQuick 2.0

Rectangle {
    id: back
    color: "red"

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

            /*
            ctx.beginPath()
            ctx.moveTo(centerX - radius/10, centerY)
            ctx.moveTo(centerX - radius/10, centerY - radius)
            ctx.lineTo(centerX - radius/10, centerY + radius)
            ctx.stroke()
            ctx.beginPath()
            ctx.moveTo(centerX + radius/10 , centerY)
            ctx.moveTo(centerX + radius/10, centerY - radius)
            ctx.lineTo(centerX + radius/10, centerY + radius)
            ctx.stroke()
            ctx.beginPath()
            ctx.moveTo(centerX - radius, centerY - radius/3 - radius/10)
            ctx.lineTo(centerX + radius, centerY - radius/3 - radius/10)
            ctx.stroke()
            ctx.beginPath()
            ctx.moveTo(centerX - radius, centerY - radius/3 + radius/10)
            ctx.lineTo(centerX + radius, centerY - radius/3 + radius/10)
            ctx.stroke()
            ctx.beginPath()
            ctx.moveTo(centerX - radius/2, centerY + radius)
            ctx.lineTo(centerX + radius/2, centerY + radius)
            ctx.stroke()
            ctx.beginPath()
            ctx.moveTo(centerX - radius/2, centerY - radius/5 + radius)
            ctx.lineTo(centerX + radius/2, centerY - radius/5 + radius)
            ctx.stroke()*/

            ctx.beginPath()
            ctx.moveTo(centerX - radius/6, centerY - radius)
            ctx.lineTo(centerX - radius/6, centerY + radius)
            ctx.lineTo(centerX + radius/6, centerY + radius)
            ctx.lineTo(centerX + radius/6, centerY - radius)
            ctx.closePath()
            ctx.fill()

            ctx.beginPath()
            ctx.moveTo(centerX - radius, centerY - radius*2/3)
            ctx.lineTo(centerX - radius, centerY - radius/3)
            ctx.lineTo(centerX + radius, centerY - radius/3)
            ctx.lineTo(centerX + radius, centerY - radius*2/3)
            ctx.closePath()
            ctx.fill()

            ctx.beginPath()
            ctx.moveTo(centerX - radius/2, centerY + radius)
            ctx.lineTo(centerX - radius/2, centerY + radius - radius/3)
            ctx.lineTo(centerX + radius/2, centerY + radius - radius/3)
            ctx.lineTo(centerX + radius/2, centerY + radius)
            ctx.closePath()
            ctx.fill()
        }
    }
}
