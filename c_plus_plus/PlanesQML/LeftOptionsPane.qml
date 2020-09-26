import QtQuick 2.14
import QtQuick.Controls 2.14
import QtQuick.Layouts 1.11


Rectangle {
	id: optionsPane
	color: 'green'
	width: parent.width / 2
	height: parent.height

	property int textSize : width / 20
	property string textColorGame : "blue"
	property string backColorGame : "green"
	property string textColorGlobal : "red"
	property string backColorGlobal : "blue"
	
	GridLayout {
		columns: 2
		anchors.horizontalCenter : parent.horizontalCenter
		anchors.top : parent.top
		
        anchors.margins: textSize

        Label {
        	font.pixelSize: optionsPane.textSize
			id: gameDifficultyOptionsTitle
            text: "GameDifficultyOptions"
            color: 'blue'
        }

        Label {
                text: "BlaBla"
                color: optionsPane.backColorGame
            }

		Label {
                text: "BlaBla"
                color: optionsPane.backColorGame
            }

        Label {
                text: "BlaBla"
                color: optionsPane.backColorGame
            }

		Label {
			font.pixelSize: optionsPane.textSize
			id: computerSkillLabel
            text: "Computer Skill"
            color: optionsPane.textColorGame
        }

        ComboBox {
        	id: computerSkilCombo
    		model: ["Easy", "Medium", "Difficult"]
		}

		Label {
			font.pixelSize: optionsPane.textSize
        	id: showPlaneLabel
        	text: "Show Plane After Kill"
        	color: optionsPane.textColorGame
    	}

    	ComboBox {
        	id: showPlaneCombo
    		model: ["Yes", "No"]
		}
	}
}