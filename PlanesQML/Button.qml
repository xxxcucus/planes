import QtQuick 2.0

Rectangle {
    property string buttonText: "Button Text"
    id: button
    signal clicked()
    border {
        width: 2
        color: "black"
    }
    radius: 5
    color: marea.containsMouse ? "red" : "green"
    width: bText.width + bText.width/5
    height: bText.height + bText.height/8
    onClicked: console.log("Clicked!")

    Text {
        id: bText
        text: parent.buttonText
        font.family: "Arial"
        anchors {
            horizontalCenter: parent.horizontalCenter
        }
    }

    MouseArea {
        id: marea
        hoverEnabled: true
        anchors.fill: parent
        onClicked: button.clicked()
    }
}
