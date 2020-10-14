import QtQuick 2.14
import QtQuick.Controls 2.14
import QtQuick.Layouts 1.11
import QtQuick.Dialogs 1.3


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

	property int currentSkill: PlaneGame == null ? 2 : PlaneGame.getCurrentSkill() 
	property bool currentShowAfterKill: PlaneGame == null ? false : PlaneGame.getShowPlaneAfterKill()
	
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
        	currentIndex: optionsPane.currentSkill
        	id: computerSkilCombo
    		model: ["Easy", "Medium", "Difficult"]
    		onActivated: {
    			if (PlaneGame.setCurrentSkill(currentIndex)) {
    				optionsPane.currentSkill = currentIndex
    			} else {
    				currentIndex = optionsPane.currentSkill
    				messageDialog.open()
    			}
    		}
		}

		Label {
			font.pixelSize: optionsPane.textSize
        	id: showPlaneLabel
        	text: "Show Plane After Kill"
        	color: optionsPane.textColorGame
    	}

    	ComboBox {
        	currentIndex: PlaneGame == null ? 1 : PlaneGame.getShowPlaneAfterKill() ? 0 : 1
        	id: showPlaneCombo
    		model: ["Yes", "No"]
    		onActivated: {
    			if (PlaneGame.setShowPlaneAfterKill(currentIndex == 0)) {
    				optionsPane.currentShowAfterKill = currentIndex == 0
    			} else {
    				currentIndex = optionsPane.currentShowAfterKill ? 0 : 1
    				messageDialog.open()
    			}
    		}
		}
	}

	MessageDialog {
    id: messageDialog
    title:"Warning message"
    text: "Cannot set option during the game."
	}
}