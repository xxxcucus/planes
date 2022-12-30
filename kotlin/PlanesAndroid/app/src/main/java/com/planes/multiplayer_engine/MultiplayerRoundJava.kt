package com.planes.multiplayer_engine

import androidx.core.util.Pair
import com.planes.android.MultiplayerRoundInterface
import com.planes.android.game.multiplayer.IGameFragmentMultiplayer
import com.planes.multiplayer_engine.requests.*
import com.planes.multiplayer_engine.responses.*
import com.planes.single_player_engine.*
import io.reactivex.Observable
import retrofit2.Response
import java.util.*

class MultiplayerRoundJava : MultiplayerRoundInterface {
    override fun createPlanesRound() {
        if (global_Round != null) return
        global_Round = MultiplayerRound(10,10, 3)
    }

    override fun testServerVersion(): Observable<Response<VersionResponse>> {
        return global_Round!!.testServerVersion()
    }

    override fun login(username: String, password: String): Observable<Response<LoginResponse>> {
        return global_Round!!.login(username, password)
    }

    override fun setUserData(username: String, password: String, authToken: String) {
        global_Round!!.setUserData(username, password, authToken)
    }

    override fun authTokenExpired(): Boolean {
        return global_Round!!.authTokenExpired()
    }

    override fun getAuthToken(): String {
        return global_Round!!.getAuthToken()
    }

    override fun getGameId() : Long {
        return global_Round!!.getGameId()
    }

    override fun getRoundId() : Long {
        return global_Round!!.getRoundId()
    }

    override fun getUserId(): Long {
        return global_Round!!.getUserId()
    }

    override fun getOpponentId(): Long {
        return global_Round!!.getOpponentId()
    }

    override fun getUsername(): String {
        return global_Round!!.getUsername()
    }

    override fun getPassword(): String {
        return global_Round!!.getPassword()
    }

    override  fun getOpponentName(): String {
        return global_Round!!.getOpponentName()
    }

    override fun register(username: String, password: String): Observable<Response<RegistrationResponse>> {
        return global_Round!!.register(username, password)
    }

    override fun setRegistrationResponse(regResp: RegistrationResponse) {
        global_Round!!.setRegistrationResponse(regResp)
    }

    override fun getRegistrationResponse(): RegistrationResponse {
        return global_Round!!.getRegistrationResponse()
    }

    override fun norobot(requestId: Long, answer: String): Observable<Response<NoRobotResponse>> {
        return global_Round!!.norobot(requestId, answer)
    }

    override fun isUserLoggedIn(): Boolean {
        return global_Round!!.isUserLoggedIn()
    }

    override fun isUserConnectedToGame(): Boolean {
        return global_Round!!.isUserConnectedToGame()
    }

    override fun refreshGameStatus(gameName: String):
            Observable<Response<GameStatusResponse>> {
        return global_Round!!.refreshGameStatus(gameName)
    }

    override fun createGame(gameName: String): Observable<Response<CreateGameResponse>> {
        return global_Round!!.createGame(gameName)
    }

    override fun setGameData(gameCreationResponse: CreateGameResponse) {
        return global_Round!!.setGameData(gameCreationResponse)
    }

    override fun setGameData(connectToGameResponse: ConnectToGameResponse) {
        return global_Round!!.setGameData(connectToGameResponse)
    }

    override fun getGameData(): GameData {
        return global_Round!!.getGameData()
    }

    override fun setGameData(gameStatusResponse: GameStatusResponse) {
        return global_Round!!.setGameData(gameStatusResponse)
    }

    override fun setUserId(userid: Long) {
        return global_Round!!.setUserId(userid)
    }

    override fun connectToGame(gameName: String): Observable<Response<ConnectToGameResponse>> {
        return global_Round!!.connectToGame(gameName)
    }

    override fun resetGameData() {
        global_Round!!.resetGameData()
    }

    override fun getRowNo(): Int {
        return global_Round!!.getRowNo()
    }

    override fun getColNo(): Int {
        return global_Round!!.getColNo()
    }

    override fun getPlaneNo(): Int {
        return global_Round!!.getPlaneNo()
    }

    override fun getPlaneSquareType(i: Int, j: Int, isComputer: Int): Int {
        return global_Round!!.getPlaneSquareType(i, j, isComputer > 0)
    }

    //edit the board
    override fun movePlaneLeft(idx: Int): Int {
        return if (global_Round!!.movePlaneLeft(idx)) 1 else 0
    }

    override fun movePlaneRight(idx: Int): Int {
        return if (global_Round!!.movePlaneRight(idx)) 1 else 0
    }

    override fun movePlaneUpwards(idx: Int): Int {
        return if (global_Round!!.movePlaneUpwards(idx)) 1 else 0
    }

    override fun movePlaneDownwards(idx: Int): Int {
        return if (global_Round!!.movePlaneDownwards(idx)) 1 else 0
    }

    override fun rotatePlane(idx: Int): Int {
        return if (global_Round!!.rotatePlane(idx)) 1 else 0
    }

    override fun doneClicked() {
        global_Round!!.doneEditing()
    }

    override fun playerGuessAlreadyMade(row: Int, col: Int): Int {
        return global_Round!!.playerGuessAlreadyMade(row, col)
    }

    override fun playerGuess(row: Int, col: Int) {
        TODO("Not yet implemented")
    }

    override fun playerGuess(gp: GuessPoint): PlayerGuessReaction {
        val result = global_Round!!.playerGuess(gp)
        if (!result.first)
            global_Player_Guess_Reaction = result.second
        return result.second
    }

    override fun playerGuessIncomplete(row: Int, col: Int): Pair<Type, PlayerGuessReaction> {
        var result = global_Round!!.playerGuessIncomplete(row, col)
        global_Player_Guess_Reaction = result.second
        return result
    }

    override fun playerGuess_RoundEnds(): Boolean {
        global_Player_Guess_Reaction.m_GameStats = global_Round!!.getGameStats()  //TODO: this is a hack and should be corrected
        return global_Player_Guess_Reaction.m_RoundEnds
    }

    override fun playerGuess_IsDraw(): Boolean {
        global_Player_Guess_Reaction.m_GameStats = global_Round!!.getGameStats()
        return global_Player_Guess_Reaction.m_IsDraw
    }

    override fun playerGuess_IsPlayerWinner(): Boolean {
        global_Player_Guess_Reaction.m_GameStats = global_Round!!.getGameStats()
        return global_Player_Guess_Reaction.m_isPlayerWinner
    }

    override fun playerGuess_ComputerMoveGenerated(): Boolean {
        return global_Player_Guess_Reaction.m_ComputerMoveGenerated
    }

    override fun playerGuess_StatNoPlayerMoves(): Int {
        global_Player_Guess_Reaction.m_GameStats = global_Round!!.getGameStats()
        return global_Player_Guess_Reaction.m_GameStats.playerMoves()
    }

    override fun playerGuess_StatNoPlayerHits(): Int {
        global_Player_Guess_Reaction.m_GameStats = global_Round!!.getGameStats()
        return global_Player_Guess_Reaction.m_GameStats.playerHits()
    }

    override fun playerGuess_StatNoPlayerMisses(): Int {
        global_Player_Guess_Reaction.m_GameStats = global_Round!!.getGameStats()
        return global_Player_Guess_Reaction.m_GameStats.playerMisses()
    }

    override fun playerGuess_StatNoPlayerDead(): Int {
        global_Player_Guess_Reaction.m_GameStats = global_Round!!.getGameStats()
        return global_Player_Guess_Reaction.m_GameStats.playerDead()
    }

    override fun playerGuess_StatNoPlayerWins(): Int {
        global_Player_Guess_Reaction.m_GameStats = global_Round!!.getGameStats()
        return global_Player_Guess_Reaction.m_GameStats.playerWins()
    }

    override fun playerGuess_StatNoComputerMoves(): Int {
        global_Player_Guess_Reaction.m_GameStats = global_Round!!.getGameStats()
        return global_Player_Guess_Reaction.m_GameStats.computerMoves()
    }

    override fun playerGuess_StatNoComputerHits(): Int {
        global_Player_Guess_Reaction.m_GameStats = global_Round!!.getGameStats()
        return global_Player_Guess_Reaction.m_GameStats.computerHits()
    }

    override fun playerGuess_StatNoComputerMisses(): Int {
        global_Player_Guess_Reaction.m_GameStats = global_Round!!.getGameStats()
        return global_Player_Guess_Reaction.m_GameStats.computerMisses()
    }

    override fun playerGuess_StatNoComputerDead(): Int {
        global_Player_Guess_Reaction.m_GameStats = global_Round!!.getGameStats()
        return global_Player_Guess_Reaction.m_GameStats.computerDead()
    }

    override fun playerGuess_StatNoComputerWins(): Int {
        global_Player_Guess_Reaction.m_GameStats = global_Round!!.getGameStats()
        return global_Player_Guess_Reaction.m_GameStats.computerWins()
    }

    override fun playerGuess_StatNoDraws(): Int {
        global_Player_Guess_Reaction.m_GameStats = global_Round!!.getGameStats()
        return global_Player_Guess_Reaction.m_GameStats.draws()
    }

    override fun roundEnds() {
        global_Round!!.setRoundEnd()
    }

    override fun initRound() {
        global_Round!!.initRound()
    }

    override fun getPlayerGuessesNo(): Int {
        return global_Round!!.getPlayerGuessesNo()
    }

    override fun getPlayerGuessRow(idx: Int): Int {
        return global_Round!!.getPlayerGuess(idx).row()
    }

    override fun getPlayerGuessCol(idx: Int): Int {
        return global_Round!!.getPlayerGuess(idx).col()
    }

    override fun getPlayerGuessType(idx: Int): Int {
        return global_Round!!.getPlayerGuess(idx).type().value
    }

    override fun getComputerGuessesNo(): Int {
        return global_Round!!.getComputerGuessesNo()
    }

    override fun getComputerGuessRow(idx: Int): Int {
        return global_Round!!.getComputerGuess(idx).row()
    }

    override fun getComputerGuessCol(idx: Int): Int {
        return global_Round!!.getComputerGuess(idx).col()
    }

    override fun getComputerGuessType(idx: Int): Int {
        return global_Round!!.getComputerGuess(idx).type().value
    }

    override fun getGameStage(): Int {
        return global_Round!!.getCurrentStage()
    }

    override fun setGameStage(stage: GameStages) {
        global_Round!!.setGameStage(stage)
    }

    override fun setComputerSkill(skill: Int): Boolean {
        return true
    }

    override fun setShowPlaneAfterKill(show: Boolean): Boolean {
        return true
    }

    override fun getComputerSkill(): Int {
        return 0
    }

    override fun getShowPlaneAfterKill(): Boolean {
        return true
    }

    override fun getPlayerPlaneNo(pos: Int): Plane {
        return global_Round!!.getPlayerPlaneNo(pos)
    }

    override fun sendPlanePositions(request: SendPlanePositionsRequest): Observable<Response<SendPlanePositionsResponse>> {
        return global_Round!!.sendPlanePositions(request)
    }

    override fun setComputerPlanes(plane1_x: Int, plane1_y: Int, plane1_orient: Orientation,
                          plane2_x: Int, plane2_y: Int, plane2_orient: Orientation,
                          plane3_x: Int, plane3_y: Int, plane3_orient: Orientation): Boolean {
        return global_Round!!.setComputerPlanes(plane1_x, plane1_y, plane1_orient, plane2_x, plane2_y, plane2_orient, plane3_x, plane3_y, plane3_orient)
    }

    override fun acquireOpponentPlanePositions(request: AcquireOpponentPositionsRequest): Observable<Response<AcquireOpponentPositionsResponse>> {
        return global_Round!!.acquireOpponentPlanePositions(request)
    }

    override fun setGameFragment(gameFragment: IGameFragmentMultiplayer) {
        global_Round!!.setGameFragment(gameFragment)
    }

    override fun sendWinner(draw: Boolean, winnerId: Long): Observable<Response<SendWinnerResponse>> {
        return global_Round!!.sendWinner(draw, winnerId)
    }

    override fun checkWinnerSent() {
        global_Round!!.checkWinnerSent()
    }

    override fun addToNotSentMoves(moveIndex: Int) {
        global_Round!!.addToNotSentMoves(moveIndex)
    }

    override fun saveNotSentMoves() {
        global_Round!!.saveNotSentMoves()
    }

    override fun computeNotReceivedMoves(): Pair<Vector<Int>, Int> {
        return global_Round!!.computeNotReceivedMoves()
    }

    override fun prepareNotSentMoves(): Vector<SingleMoveRequest> {
        return global_Round!!.prepareNotSentMoves()
    }

    override fun sendMove(sendMoveRequest: SendNotSentMovesRequest): Observable<Response<SendNotSentMovesResponse>> {
        return global_Round!!.sendMove(sendMoveRequest)
    }

    override fun deleteFromNotSentList() {
        global_Round!!.deleteFromNotSentList()
    }

    override fun moveAlreadyReceived(idx: Int): Boolean {
        return global_Round!!.moveAlreadyReceived(idx)
    }

    override fun addOpponentMove(gp: GuessPoint, idx: Int) {
        val result = global_Round!!.addOpponentMove(gp, idx)
        if (!result.first)
            global_Player_Guess_Reaction = result.second
    }

    override fun cancelRound(gameId: Long, roundId: Long): Observable<Response<CancelRoundResponse>> {
        return global_Round!!.cancelRound(gameId, roundId)
    }

    override fun startNewRound(gameId: Long, userId: Long, opponentId: Long): Observable<Response<StartNewRoundResponse>> {
        return global_Round!!.startNewRound(gameId, userId, opponentId)
    }

    override fun setRoundId(roundId: Long) {
        global_Round!!.setRoundId(roundId)
    }

    override fun cancelRound() {
        global_Round!!.setGameStage(GameStages.GameNotStarted)
        global_Player_Guess_Reaction.m_Cancelled = true
    }

    override fun getGameStats() : GameStatistics {
        return global_Round!!.getGameStats()
    }

    companion object {
        private var global_Round: MultiplayerRound? = null
        private var global_Player_Guess_Reaction = PlayerGuessReaction()
    }
}