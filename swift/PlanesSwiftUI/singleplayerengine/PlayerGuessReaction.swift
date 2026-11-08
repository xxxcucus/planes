

struct PlayerGuessReaction {
    //victory for computer or player
    var roundEnds = false

    //is it a draw
    var isDraw = false

    //who won
    var isPlayerWinner = false

    //if no victory then a computer move is generated
    var computerMoveGenerated = false

    //which computer move was generated
    var computerGuess: GuessPoint = GuessPoint(row: 0, col: 0)

    var gameStats: GameStatistics = GameStatistics()

    var playerFinishedStartPolling: Bool = false

    var cancelled = false

}

