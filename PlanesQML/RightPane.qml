import QtQuick 2.9
import QtQuick.Controls 2.2
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
        }
        TabButton {
            text: "Computer Board"
        }
        TabButton {
            text: "Help"
        }
    }

    StackLayout {
        anchors.top: bar.bottom
        currentIndex: bar.currentIndex
        width: parent.width
        height: parent.height - bar.height

        GenericBoard {
            boardModel: PlayerPlaneGrid
            id : playerBoard
        }

        GenericBoard {
            boardModel: ComputerPlaneGrid
            id : computerBoard
        }

        Rectangle {
           color: 'blue'
           width: parent.width
           height: parent.height
        }
    }

}
