import QtQuick 2.0

Rectangle {
    id: back
    color: "red"

    Canvas {
        width: back.width
        height: back.height

        onPaint: {
            var ctx = getContext("2d")
            ctx.fillStyle = 'blue'
            ctx.lineWidth = 4

            var centerX = width/2
            var centerY = height/2
            var radius = Math.min(width/3, height/3)

            ctx.beginPath()
            ctx.moveTo(centerX + radius, centerY)
            ctx.arc(centerX, centerY, radius, 0, Math.PI + Math.PI/2, false)
            ctx.lineTo(centerX, centerY - radius/3)
            ctx.arc(centerX, centerY, radius - radius/3, Math.PI + Math.PI/2, 0, true)
            ctx.closePath()
            ctx.fill()

            ctx.beginPath();
            ctx.moveTo(centerX + radius - radius/6, centerY);
            ctx.lineTo(centerX + radius - radius/6 - radius/3 , centerY);
            ctx.lineTo(centerX + radius - radius/6 , centerY - radius/3);
            ctx.lineTo(centerX + radius - radius/6 + radius/3, centerY);
            ctx.closePath();
            ctx.fill();

        }
    }
}
