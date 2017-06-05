import QtQuick 2.0

Rectangle {
    property string buttonText: "Button Text"
    id: button
    signal clicked()
    border { width: 2; color: "black" }
    radius: 5
    color: marea.containsMouse ? "red" : "green"
    width: 4 + buttonText.width
    height: buttonText.height + 8
    onClicked: console.log("Clicked!")

    Text {
        id: buttonText
        text: parent.buttonText
        anchors {
            verticalCenter: parent.verticalCenter
        }
    }

    MouseArea {
        id: marea
        hoverEnabled: true
        anchors.fill: parent
        onClicked: button.clicked()
    }
}
