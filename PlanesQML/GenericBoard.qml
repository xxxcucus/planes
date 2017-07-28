import QtQuick 2.0

Rectangle {
    property int gridSquaresOnLine: 10
    property int gridBorder: 3
    property bool isComputer: false

    width: parent.width*2/3
    height: parent.height
    color: "yellow"

    Grid {
        anchors.centerIn: parent
        anchors.margins: 8
        spacing: 4
        columns: gridSquaresOnLine + 2 * gridBorder
        Repeater {
            model: (gridSquaresOnLine + 2 * gridBorder) * (gridSquaresOnLine + 2 * gridBorder)
            GridSquare {
                width: 30
                color: (index / (gridSquaresOnLine + 2 * gridBorder) < gridBorder ||
                        index / (gridSquaresOnLine + 2 * gridBorder)  >= gridSquaresOnLine + gridBorder ||
                        index % (gridSquaresOnLine + 2 * gridBorder) < gridBorder ||
                        index % (gridSquaresOnLine + 2 * gridBorder)  >= gridSquaresOnLine + gridBorder) ? "aqua" : "#ea7025"
            }
        }
    }

    Connections {
        target: PlaneGrid
        onPlanesPointsChanged: console.log("Planes points changed")
    }
}
