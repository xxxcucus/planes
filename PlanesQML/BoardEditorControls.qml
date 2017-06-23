import QtQuick 2.0


Rectangle {
    width: parent.width/3
    height: parent.height

    Rectangle {
        id: panel1
        color: "green"
        anchors.top: parent.top
        width: parent.width
        height: parent.height*3/4

        Grid {
            anchors.centerIn: parent
            anchors.margins: panel1.width/10
            spacing: panel1.width/10
            columns: 2
            RotatePlane {
                width: panel1.width/3
                height: panel1.height/6
            }
            SelectPlane {
                width: panel1.width/3
                height: panel1.height/6
            }
            MovePlaneLeft {
                width: panel1.width/3
                height: panel1.height/6
            }
            MovePlaneRight {
                width: panel1.width/3
                height: panel1.height/6
            }
            MovePlaneUpwards {
                width: panel1.width/3
                height: panel1.height/6
            }
            MovePlaneDownwards {
                width: panel1.width/3
                height: panel1.height/6
            }
        }
    }

    Rectangle {
        color: "blue"
        anchors.bottom: parent.bottom
        anchors.top: panel1.bottom
        id: panel2
        width: parent.width
        height: parent.height*3/4
        DoneEditing {
            anchors.centerIn: parent
            width: panel2.width*2/3
            height: panel2.height/3
        }
    }
}
