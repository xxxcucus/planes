import QtQuick 2.0

Rectangle {
    property int verticalDist : 10

    height: parent.height
    width: parent.width/3
    color: "blue"

    Button {
        anchors.topMargin: verticalDist
        anchors.top: parent.top
        anchors.horizontalCenter: parent.horizontalCenter
        id: selectPlaneButton
        buttonText: "Select Plane"
    }

    Button {
        anchors.topMargin: verticalDist
        anchors.top: selectPlaneButton.bottom
        anchors.horizontalCenter: parent.horizontalCenter
        id: rotatePlaneButton
        buttonText: "Rotate Plane"
    }

    Button {
        anchors.topMargin: verticalDist
        anchors.top: rotatePlaneButton.bottom
        anchors.horizontalCenter: parent.horizontalCenter
        id: planeUpButton
        buttonText: "Plane Upwards"
    }

    Button {
        anchors.topMargin: verticalDist
        anchors.top: planeUpButton.bottom
        anchors.horizontalCenter: planeUpButton.left
        id: planeLeftButton
        buttonText: "Plane Left"
    }

    Button {
        anchors.topMargin: verticalDist
        anchors.top: planeUpButton.bottom
        anchors.horizontalCenter: planeUpButton.right
        id: planeRightButton
        buttonText: "Plane Right"
    }

    Button {
        anchors.topMargin: verticalDist
        anchors.top: planeLeftButton.bottom
        anchors.horizontalCenter: parent.horizontalCenter
        id: planeDownButton
        buttonText: "Plane Downwards"
    }

    Button {
        anchors.topMargin: verticalDist
        anchors.top: planeDownButton.bottom
        anchors.horizontalCenter: parent.horizontalCenter
        id: doneButton
        buttonText: "Done Editing"
    }

}
