import QtQuick 2.0

Rectangle {
    property int gridSquaresOnLine: 10
    property int gridBorder: 3
    property bool isComputer: true

    width: parent.width
    height: parent.height
    color: "yellow"

    Component {
        id: boardDelegate
        Rectangle {
            width : grid.cellSize
            height : grid.cellSize
        }

    }


    GridView {
        id: grid
        anchors.centerIn: parent
        anchors.margins: 8
        property string colorBoard: "#ea7025"
        property string colorBorder: "aqua"
        property int cellSize : 30

        width: parent.width
        height: parent.height



}
