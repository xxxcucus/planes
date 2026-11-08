
struct GameStatistics {
    //keeps the number of moves and various guesses in the current round

    var playerMoves = 0
    var playerHits = 0
    var playerDead = 0
    var playerMisses = 0
    var computerMoves = 0
    var computerHits = 0
    var computerDead = 0
    var computerMisses = 0

    var lastComputerUpdated = GuessType.miss
    var lastPlayerUpdated = GuessType.miss

    //keeps the score
    var playerWins = 0
    var computerWins = 0
    var draws = 0

    //constructor
    init() {
        reset()
        resetGameScore()
    }
    //resets the fields related to one round of the game
    mutating func reset() {
        playerMoves = 0
        playerHits = 0
        playerDead = 0
        playerMisses = 0
        computerMoves = 0
        computerHits = 0
        computerDead = 0
        computerMisses = 0
    }

    mutating func resetGameScore() {
        playerWins = 0
        computerWins = 0
        draws = 0
    }

    //updates the statistics for one round with one guess
    mutating func updateStats(gp: GuessPoint, isComputer: Bool) {
        if (isComputer) {
            computerMoves += 1
            if (gp.type == GuessType.dead) {
                lastComputerUpdated = GuessType.dead
                computerDead += 1
            }
            if (gp.type == GuessType.hit) {
                lastComputerUpdated = GuessType.hit
                computerHits += 1
            }

            if (gp.type == GuessType.miss) {
                lastComputerUpdated = GuessType.miss
                computerMisses += 1
            }
        } else {
            playerMoves += 1
            if (gp.type == GuessType.dead) {
                lastPlayerUpdated = GuessType.dead
                playerDead += 1
            }
            if (gp.type == GuessType.hit) {
                lastPlayerUpdated = GuessType.hit
                playerHits += 1
            }

            if (gp.type == GuessType.miss) {
                lastPlayerUpdated = GuessType.miss
                playerMisses += 1
            }
        }
    }

    //adds to the score
    mutating func updateWins(isComputerWinner: Bool) {
        if (isComputerWinner) {
            computerWins += 1
        } else {
            playerWins += 1
        }
    }

    mutating func addDrawResult() {
        draws += 1
    }

    func playerFinished(noPlanes: Int) -> Bool {
        return playerDead >= noPlanes
    }

    func computerFinished(noPlanes: Int) -> Bool {
        return computerDead >= noPlanes
    }
}
