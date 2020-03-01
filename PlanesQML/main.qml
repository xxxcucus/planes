import QtQuick 2.14
import QtQuick.Controls 2.14
import QtQuick.Window 2.14

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
