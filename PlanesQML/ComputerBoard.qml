import QtQuick 2.7
import QtQuick.Controls 1.4

Rectangle {
    property int gridSquaresOnLine: 10
    property int gridBorder: 3
    property bool isComputer: true

    width: parent.width
    height: parent.height
    color: "yellow"

    Rectangle {
        anchors.centerIn: parent
        width : grid.cellSize * (gridSquaresOnLine + 2 * gridBorder)
        height: width
        color: "yellow"

        GridView {
            id: grid
            anchors.centerIn: parent
            anchors.margins: 8

            property string colorBoard: "#ea7025"
            property string colorBorder: "aqua"
            property int cellSize : 35

            width: parent.width
            height: parent.height
            cellWidth: cellSize
            cellHeight: cellSize
            flow: GridView.FlowTopToBottom

            model: ComputerPlaneGrid
            delegate: Rectangle {
                    width : grid.cellSize
                    height : grid.cellSize
                    color: "yellow"
                    Rectangle {
                        anchors.centerIn: parent
                        width: parent.width - 5
                        height: parent.height - 5
                        color: colorRGB
                    }
                }
        }
    }
}
