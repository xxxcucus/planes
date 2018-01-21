
function doneButton(ctx) {
    /*var ctx = getContext("2d")*/
    ctx.fillStyle = 'blue'

    var centerX = width/2
    var centerY = height/2
    var radius = Math.min(width/3, height/3)

    var str1 = height/2
    var str2 = "px sans-serif"
    ctx.font = str1.toString().concat(str2)
    ctx.textAlign = "center"
    ctx.fillText("DONE", centerX, centerY + 10)
}

function moveDownButton(ctx) {
    ctx.fillStyle = 'blue'
    ctx.lineWidth = 4

    var centerX = width / 2
    var centerY = height / 2
    var radius = Math.min(width / 3, height / 3)

    ctx.fillRect(centerX - radius / 5, centerY - radius / 2 - radius / 3, radius * 2 / 5, radius)

    ctx.beginPath()
    ctx.moveTo(centerX, centerY + radius / 2 - radius / 3)
    ctx.lineTo(centerX - radius / 3, centerY + radius / 2 - radius / 3)
    ctx.lineTo(centerX, centerY + radius / 2 + radius / 3)
    ctx.lineTo(centerX + radius / 3, centerY + radius / 2 - radius / 3)
    ctx.closePath()
    ctx.fill()
}

function moveLeftButton(ctx) {
    ctx.fillStyle = 'blue'
    ctx.lineWidth = 4

    var centerX = width / 2
    var centerY = height / 2
    var radius = Math.min(width / 3, height / 3)

    ctx.fillRect(centerX - radius / 2 + radius / 3, centerY - radius / 5, radius, radius * 2 / 5)

    ctx.beginPath()
    ctx.moveTo(centerX - radius / 2 + radius / 3, centerY)
    ctx.lineTo(centerX - radius / 2 + radius / 3, centerY + radius / 3)
    ctx.lineTo(centerX - radius / 2 - radius / 3, centerY)
    ctx.lineTo(centerX - radius / 2 + radius / 3, centerY - radius / 3)
    ctx.closePath()
    ctx.fill()
}

function moveRightButton(ctx) {
    ctx.fillStyle = 'blue'
    ctx.lineWidth = 4

    var centerX = width / 2
    var centerY = height / 2
    var radius = Math.min(width / 3, height / 3)

    ctx.fillRect(centerX - radius / 2 - radius / 3, centerY - radius / 5, radius, radius * 2 / 5)

    ctx.beginPath()
    ctx.moveTo(centerX + radius / 2 - radius / 3, centerY)
    ctx.lineTo(centerX + radius / 2 - radius / 3, centerY + radius / 3)
    ctx.lineTo(centerX + radius / 2 + radius / 3, centerY)
    ctx.lineTo(centerX + radius / 2 - radius / 3, centerY - radius / 3)
    ctx.closePath()
    ctx.fill()
}

function moveUpButton(ctx) {
    ctx.fillStyle = 'blue'
    ctx.lineWidth = 4

    var centerX = width / 2
    var centerY = height / 2
    var radius = Math.min(width / 3, height / 3)

    ctx.fillRect(centerX - radius / 5, centerY - radius / 2 + radius / 3, radius * 2 / 5, radius)

    ctx.beginPath()
    ctx.moveTo(centerX, centerY - radius / 2 + radius / 3)
    ctx.lineTo(centerX - radius / 3, centerY - radius / 2 + radius / 3)
    ctx.lineTo(centerX, centerY - radius / 2 - radius / 3)
    ctx.lineTo(centerX + radius / 3, centerY - radius / 2 + radius / 3)
    ctx.closePath()
    ctx.fill()
}

function rotateButton(ctx) {
    ctx.fillStyle = 'blue'
    ctx.lineWidth = 4

    var centerX = width / 2
    var centerY = height / 2
    var radius = Math.min(width / 3, height / 3)

    ctx.beginPath()
    ctx.moveTo(centerX + radius, centerY)
    ctx.arc(centerX, centerY, radius, 0, Math.PI + Math.PI / 2, false)
    ctx.lineTo(centerX, centerY - radius / 3)
    ctx.arc(centerX, centerY, radius - radius / 3, Math.PI + Math.PI / 2, 0, true)
    ctx.closePath()
    ctx.fill()

    ctx.beginPath();
    ctx.moveTo(centerX + radius - radius / 6, centerY);
    ctx.lineTo(centerX + radius - radius / 6 - radius / 3 , centerY);
    ctx.lineTo(centerX + radius - radius / 6 , centerY - radius / 3);
    ctx.lineTo(centerX + radius - radius / 6 + radius / 3, centerY);
    ctx.closePath();
    ctx.fill();
}


function selectButton(ctx) {
    ctx.fillStyle = 'blue'
    ctx.lineWidth = 4

    var centerX = width / 2
    var centerY = height / 2
    var radius = Math.min(width / 3, height / 3)

    ctx.beginPath()
    ctx.moveTo(centerX - radius / 6, centerY - radius)
    ctx.lineTo(centerX - radius / 6, centerY + radius)
    ctx.lineTo(centerX + radius / 6, centerY + radius)
    ctx.lineTo(centerX + radius / 6, centerY - radius)
    ctx.closePath()
    ctx.fill()

    ctx.beginPath()
    ctx.moveTo(centerX - radius, centerY - radius * 2 / 3)
    ctx.lineTo(centerX - radius, centerY - radius / 3)
    ctx.lineTo(centerX + radius, centerY - radius / 3)
    ctx.lineTo(centerX + radius, centerY - radius * 2 / 3)
    ctx.closePath()
    ctx.fill()

    ctx.beginPath()
    ctx.moveTo(centerX - radius / 2, centerY + radius)
    ctx.lineTo(centerX - radius / 2, centerY + radius - radius / 3)
    ctx.lineTo(centerX + radius / 2, centerY + radius - radius / 3)
    ctx.lineTo(centerX + radius / 2, centerY + radius)
    ctx.closePath()
    ctx.fill()
}
