import QtQuick 2.5
import QtQuick.Controls 2.0

ApplicationWindow {
    visible: true
    width: 1000
    height: 700

    Row {
        anchors.fill: parent

        BoardEditorControls {
            //verticalDist: 10
        }

        GenericBoard {
        }
    }

}
