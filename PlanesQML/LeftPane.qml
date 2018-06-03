import QtQuick 2.9
import QtQuick.Controls 2.2
import QtQuick.Layouts 1.3

Rectangle {
    width: parent.width/3
    height: parent.height
    property alias currentTab : stack.currentIndex

    StackLayout {
        id: stack
        width: parent.width
        height: parent.height

        currentIndex: 1
        GameStatistics {
            id: roundTab
        }
        BoardEditorControls {
        }
        Rectangle {
            id: startTab
            color: "blue"
        }
    }

    Connections {
        target: PlaneGame
        onUpdateStats: {
            roundTab.playerMoves = PlaneGame.getPlayerMoves()
            roundTab.playerHits = PlaneGame.getPlayerHits()
            roundTab.playerMisses = PlaneGame.getPlayerMisses()
            roundTab.playerDead = PlaneGame.getPlayerDead()
            roundTab.playerWins = PlaneGame.getPlayerWins()
            roundTab.computerMoves = PlaneGame.getComputerMoves()
            roundTab.computerHits = PlaneGame.getComputerHits()
            roundTab.computerMisses = PlaneGame.getComputerMisses()
            roundTab.computerDead = PlaneGame.getComputerDead()
            roundTab.computerWins = PlaneGame.getComputerWins()

            console.log("Stats updated")
        }
    }
}


