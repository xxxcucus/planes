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
            model: grid.columns * grid.columns

            GridSquare {
                width: 30
                color: grid.colorBorder
            }
        }
    }

    Connections {
        target: PlaneGrid
        onPlanesPointsChanged: {
            console.log("Planes points changed")
            var i = 0

            for (i = 0; i < grid.columns * grid.columns; i++) {
                if (i / grid.columns < gridBorder ||
                    i / grid.columns  >= gridSquaresOnLine + gridBorder ||
                    i % grid.columns < gridBorder ||
                    i % grid.columns  >= gridSquaresOnLine + gridBorder)
                    squares.itemAt(i).color = grid.colorBorder
                else
                    squares.itemAt(i).color = grid.colorBoard
            }

            var pointsNo = PlaneGrid.getPlanesPointsCount()
            for (i = 0; i < pointsNo; i++) {
                var posX = PlaneGrid.getPlanePoint(i).x +  gridBorder
                var posY = PlaneGrid.getPlanePoint(i).y +  gridBorder
                var width = gridSquaresOnLine + 2 * gridBorder
                squares.itemAt(posY * width + posX).color = PlaneGrid.getPlanePointColor(i)
            }
        }
    }
}
