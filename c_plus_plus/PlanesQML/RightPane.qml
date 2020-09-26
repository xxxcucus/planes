import QtQuick 2.11
import QtQuick.Controls 2.4
import QtQuick.Layouts 1.3


Rectangle {
    width: parent.width * 2 / 3
    height: parent.height
    property alias currentTab : bar.currentIndex
    property alias computerBoardState : computerBoard.state
    property alias playerBoardState : playerBoard.state

    TabBar {
        width: parent.width
        id: bar
        TabButton {
            text: "Player Board"
            background: Rectangle {
                color: bar.currentIndex ==  0 ? "yellow" : "gray"
            }
        }
        TabButton {
            text: "Computer Board"
            background: Rectangle {
                color: bar.currentIndex ==  1 ? "yellow" : "gray"
            }
 
        }

        TabButton {
            text: "Options"
            background: Rectangle {
                color: bar.currentIndex ==  2 ? "green" : "gray"
            }
 
        }

        TabButton {
            text: "Help"
            background: Rectangle {
                color: bar.currentIndex ==  3 ? "yellow" : "gray"
            }
 
        }
    }

    onWidthChanged: {
        //console.log("Right pane width changed")
        if (stackLayout.currentIndex == 0)
            PlayerPlaneGrid.resetModel()
        if (stackLayout.currentIndex == 1)
            ComputerPlaneGrid.resetModel()
    }

    onHeightChanged: {
        //console.log("Right pane height changed")
        if (stackLayout.currentIndex == 0)
            PlayerPlaneGrid.resetModel()
        if (stackLayout.currentIndex == 1)
            ComputerPlaneGrid.resetModel()
    }


    StackLayout {
        id: stackLayout
        anchors.top: bar.bottom
        currentIndex: bar.currentIndex
        width: parent.width
        height: parent.height - bar.height

        GenericBoard {
            boardModel: PlayerPlaneGrid
            id : playerBoard
            isComputer: false
        }

        GenericBoard {
            boardModel: ComputerPlaneGrid
            id : computerBoard
            isComputer: true
        }


        Rectangle {
            color: 'yellow'
            id: gameOptions
            width: parent.width
            height: parent.height

            Row {
                anchors.fill: parent
                LeftOptionsPane {
                    id: leftOptionsPane
                    width: parent.width / 2
                    height: parent.height
                }

                RightOptionsPane {
                    id: rightOptionsPane
                    width: parent.width / 2
                    height: parent.height
                }
            }
        }


        Rectangle {
            color: 'yellow'
            width: parent.width
            height: parent.height

			ListView {
				anchors.fill : parent 
				model: HelpPageModel {}
				delegate: Text {
					text: "<h3> "+ name +  "</h3>" + " <p> " + content + " </p>"
					width: parent.width - 10
					x: 5
					wrapMode: Text.WordWrap
				}
				clip: true
				 ScrollBar.vertical: ScrollBar {}
			}
        }
    }

}
