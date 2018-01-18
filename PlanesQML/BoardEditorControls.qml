import QtQuick 2.0

Rectangle {
    width: parent.width
    height: parent.height

    Rectangle {
        id: panel1
        color: "green"
        anchors.top: parent.top
        width: parent.width
        height: parent.height * 3/4

        Grid {
            anchors.centerIn: parent
            anchors.margins: panel1.width/10
            spacing: panel1.width/10
            columns: 2
            RotatePlane {
                id: rotateplane
                width: panel1.width/3
                height: panel1.height/6
            }
            SelectPlane {
                id: selectplane
                width: panel1.width/3
                height: panel1.height/6
            }
            MovePlaneLeft {
                id: moveplaneleft
                width: panel1.width/3
                height: panel1.height/6
            }
            MovePlaneRight {
                id: moveplaneright
                width: panel1.width/3
                height: panel1.height/6
            }
            MovePlaneUpwards {
                id: moveplaneupwards
                width: panel1.width/3
                height: panel1.height/6
            }
            MovePlaneDownwards {
                id: moveplanedownwards
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
