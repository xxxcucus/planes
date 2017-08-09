import QtQuick 2.0

Rectangle {
    property int gridSquaresOnLine: 10
    property int gridBorder: 3
    property bool isComputer: false

    width: parent.width*2/3
    height: parent.height
    color: "yellow"

    Grid {
        id: grid
        anchors.centerIn: parent
        anchors.margins: 8
        spacing: 4
        columns: gridSquaresOnLine + 2 * gridBorder
        property string colorBoard: "#ea7025"
        property string colorBorder: "aqua"


        Repeater {
            id: squares
            model: (gridSquaresOnLine + 2 * gridBorder) * (gridSquaresOnLine + 2 * gridBorder)

            GridSquare {
                width: 30
                color: (index / (gridSquaresOnLine + 2 * gridBorder) < gridBorder ||
                        index / (gridSquaresOnLine + 2 * gridBorder)  >= gridSquaresOnLine + gridBorder ||
                        index % (gridSquaresOnLine + 2 * gridBorder) < gridBorder ||
                        index % (gridSquaresOnLine + 2 * gridBorder)  >= gridSquaresOnLine + gridBorder) ? grid.colorBorder : grid.colorBoard
            }
        }
    }

    Connections {
        target: PlaneGrid
        onPlanesPointsChanged: {
            console.log("Planes points changed")
            var pointsNo = PlaneGrid.getPlanesPointsCount()
            var i = 0
            for (i = 0; i < pointsNo; i++) {
                var posX = PlaneGrid.getPlanePoint(i).x +  gridBorder
                var posY = PlaneGrid.getPlanePoint(i).y +  gridBorder
                var width = gridSquaresOnLine + 2 * gridBorder
                squares.itemAt(posY * width + posX).color = "black"
            }
        }
    }
}
