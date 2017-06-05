import QtQuick 2.5
import QtQuick.Window 2.2

Window {
    visible: true
    width: 640
    height: 480

    BoardEditor {
        verticalDist: 10
    }

    title: qsTr("Hello World")

    Text {
        anchors.centerIn: parent
        text: "Hello World!"
    }
}
