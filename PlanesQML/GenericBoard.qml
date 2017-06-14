import QtQuick 2.0

Rectangle {
    property int gridSquaresOnLine: 10
    width: parent.width*2/3
    height: parent.height
    color: "yellow"

    Grid {
        anchors.centerIn: parent
        anchors.margins: 8
        spacing: 4
        columns: gridSquaresOnLine
        Repeater {
            model: gridSquaresOnLine * gridSquaresOnLine
            GridSquare {
                width: 30
            }
        }
    }
}
