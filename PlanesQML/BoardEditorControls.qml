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
                id: rotateplane
                width: panel1.width/3
                height: panel1.height/6
                onClicked: {
                    rotateplane.color = "linen"
                    selectplane.color = "red"
                    moveplaneleft.color = "red"
                    moveplaneright.color = "red"
                    moveplaneupwards.color = "red"
                    moveplanedownwards.color = "red"
                    console.log(PlaneGrid.getRows());
                }
            }
            SelectPlane {
                id: selectplane
                width: panel1.width/3
                height: panel1.height/6
                onClicked: {
                    rotateplane.color = "red"
                    selectplane.color = "linen"
                    moveplaneleft.color = "red"
                    moveplaneright.color = "red"
                    moveplaneupwards.color = "red"
                    moveplanedownwards.color = "red"
                }
            }
            MovePlaneLeft {
                id: moveplaneleft
                width: panel1.width/3
                height: panel1.height/6
                onClicked: {
                    rotateplane.color = "red"
                    selectplane.color = "red"
                    moveplaneleft.color = "linen"
                    moveplaneright.color = "red"
                    moveplaneupwards.color = "red"
                    moveplanedownwards.color = "red"
                }
            }
            MovePlaneRight {
                id: moveplaneright
                width: panel1.width/3
                height: panel1.height/6
                onClicked: {
                    rotateplane.color = "red"
                    selectplane.color = "red"
                    moveplaneleft.color = "red"
                    moveplaneright.color = "linen"
                    moveplaneupwards.color = "red"
                    moveplanedownwards.color = "red"
                }
            }
            MovePlaneUpwards {
                id: moveplaneupwards
                width: panel1.width/3
                height: panel1.height/6
                onClicked: {
                    rotateplane.color = "red"
                    selectplane.color = "red"
                    moveplaneleft.color = "red"
                    moveplaneright.color = "red"
                    moveplaneupwards.color = "linen"
                    moveplanedownwards.color = "red"
                }
            }
            MovePlaneDownwards {
                id: moveplanedownwards
                width: panel1.width/3
                height: panel1.height/6
                onClicked: {
                    rotateplane.color = "red"
                    selectplane.color = "red"
                    moveplaneleft.color = "red"
                    moveplaneright.color = "red"
                    moveplaneupwards.color = "red"
                    moveplanedownwards.color = "linen"
                }
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
