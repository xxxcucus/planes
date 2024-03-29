import QtQuick
import QtQuick.Controls
import QtQuick.Layouts

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
        function onUpdateStats() {
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
            roundTab.draws = PlaneGame.getDraws()
        }
        //onRoundEnds: {
        //    console.log(isPlayerWinner ? "Player wins" : "Computer wins")
        //}
    }
}


