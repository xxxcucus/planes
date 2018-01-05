import QtQuick 2.9
import QtQuick.Controls 2.2
import QtQuick.Layouts 1.3

Rectangle {
    width: parent.width/3
    height: parent.height

    StackLayout {
        width: parent.width
        height: parent.height

        currentIndex: 1
        Rectangle {
            id: roundTab
            color: "red"
        }
        BoardEditorControls {
        }
        Rectangle {
            id: startTab
            color: "blue"
        }
    }
}


