import QtQuick 2.7
import QtQuick.Controls 1.4
import "ButtonPaintFunctions.js" as PaintFunctions
Rectangle {
    id: board
    state: "BoardEditing"

    property int gridSquaresOnLine: 10
    property int gridBorder: 3
    property string colorBackground: "yellow"
    property string colorBoard: "#ea7025"
    property string colorBorder: "aqua"
    property int cellSize : (Math.min(width, height) - 20) / (board.gridSquaresOnLine + 2 * board.gridBorder)
    property int spacing: 2
    property variant boardModel: ComputerPlaneGrid
    property bool isComputer: boardModel.isComputer()

    width: parent.width
    height: parent.height
    color: board.colorBackground

    Rectangle {
        anchors.centerIn: parent
        width : board.cellSize * (board.gridSquaresOnLine + 2 * board.gridBorder)
        height: width
        color: board.colorBackground

        GridView {
            anchors.centerIn: parent
            width: parent.width
            height: parent.height
            cellWidth: board.cellSize
            cellHeight: board.cellSize
            flow: GridView.FlowTopToBottom

            model: board.boardModel
            delegate: Rectangle {
                width: board.cellSize
                height: board.cellSize
                color: board.colorBackground
                Rectangle {
                    anchors.centerIn: parent
                    width: parent.width - board.spacing
                    height: parent.height - board.spacing
                    color: planeColorData

                    Canvas {
                        anchors.fill: parent
                        width: parent.width
                        height: parent.height

                        onPaint: {
                            var ctx = getContext("2d")
                            //enum Type {Miss = 0, Hit = 1, Dead = 2};
                            //console.log("guess is ", guessData);
                            if (guessData == 0)
                                PaintFunctions.testedNotPlane(ctx, width, height);
                            if (guessData == 1)
                                PaintFunctions.planeGuessed(ctx, width, height);
                            if (guessData == 2)
                                PaintFunctions.planeHeadGuessed(ctx, width, height);
                        }
                    }

                    MouseArea {
                        anchors.fill : parent
                        onClicked : {
                            if (isComputer && board.state == "Game")
                                boardModel.computerBoardClick(index)
                        }
                    }
                }
            }
        }
    }

    //todo: to use this
    states: [
            State {
                name: "GameNotStarted"
            },
            State {
                name: "BoardEditing"
            },
            State {
                name: "Game"
            }
        ]


}
