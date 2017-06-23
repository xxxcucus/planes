import QtQuick 2.5
import QtQuick.Window 2.2

Window {
    visible: true
    width: 640
    height: 480

    Row {
        anchors.fill: parent

        BoardEditorControls {
            //verticalDist: 10
        }

        GenericBoard {

        }
    }

}
