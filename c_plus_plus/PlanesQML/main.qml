import QtQuick
import QtQuick.Controls
import QtQuick.Window

Window {
    visible: true
    width: 1000
    height: 700

    Row {
        anchors.fill: parent
        LeftPane {
            id: leftPane
        }
        RightPane {
            id: rightPane
        }
    }
}
