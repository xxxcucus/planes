import QtQuick 2.0

Rectangle {
    height: parent.height
    width: parent.width/3
    color: "blue"

    Button {
        anchors.top: parent.top
        anchors.horizontalCenter: parent.horizontalCenter
        id: selectPlaneButton
        buttonText: "Select Plane"
    }

    Button {
        anchors.top: selectPlaneButton.bottom
        anchors.horizontalCenter: parent.horizontalCenter
        id: rotatePlaneButton
        buttonText: "Rotate Plane"
    }

    Button {
        anchors.top: rotatePlaneButton.bottom
        anchors.horizontalCenter: parent.horizontalCenter
        id: planeUpButton
        buttonText: "Plane Upwards"
    }

    Button {
        anchors.top: planeUpButton.bottom
        anchors.horizontalCenter: planeUpButton.left
        id: planeLeftButton
        buttonText: "Plane Left"
    }

    Button {
        anchors.top: planeUpButton.bottom
        anchors.horizontalCenter: planeUpButton.right
        id: planeRightButton
        buttonText: "Plane Right"
    }

    Button {
        anchors.top: planeLeftButton.bottom
        anchors.horizontalCenter: parent.horizontalCenter
        id: planeDownButton
        buttonText: "Plane Downwards"
    }

    Button {
        anchors.top: planeDownButton.bottom
        anchors.horizontalCenter: parent.horizontalCenter
        id: doneButton
        buttonText: "Done Editing"
    }

}
