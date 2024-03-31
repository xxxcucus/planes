package com.planes.android

import com.planes.android.game.multiplayer.IGameFragmentMultiplayer
import com.planes.multiplayer_engine.responses.*
import io.reactivex.Observable
import retrofit2.Response
import java.util.*
import androidx.core.util.Pair
import com.planes.multiplayer_engine.GameData
import com.planes.multiplayer_engine.requests.*
import com.planes.single_player_engine.*

interface MultiplayerRoundInterface: PlanesRoundInterface {
    override fun createPlanesRound()

    fun testServerVersion(): Observable<Response<VersionResponse>>

    fun login(username: String, password: String): Observable<Response<LoginResponse>>
    fun logout(username: String, userid: String): Observable<Response<LogoutResponse>>

    fun setUserData(username: String, password: String, authToken: String)

    fun authTokenExpired(): Boolean

    fun getGameId() : Long

    fun getRoundId() : Long

    fun getUserId(): Long

    fun getOpponentId(): Long

    fun getUsername(): String

    fun getPassword(): String

    fun getAuthToken(): String

    fun getOpponentName(): String

    fun register(username: String, password: String): Observable<Response<RegistrationResponse>>

    fun setRegistrationResponse(regResp: RegistrationResponse)

    fun getRegistrationResponse(): RegistrationResponse

    fun norobot(requestId: Long, answer: String): Observable<Response<NoRobotResponse>>

    fun isUserLoggedIn(): Boolean

    fun isUserConnectedToGame(): Boolean

    fun refreshGameStatus(gameName: String): Observable<Response<GameStatusResponse>>

    fun createGame(gameName: String): Observable<Response<CreateGameResponse>>

    fun setGameData(gameCreationResponse: CreateGameResponse)

    fun setGameData(connectToGameResponse: ConnectToGameResponse)

    fun setGameData(gameStatusResponse: GameStatusResponse)

    fun getGameData(): GameData

    fun setUserId(userid: Long)

    fun setRoundId(roundId: Long)

    fun connectToGame(gameName: String): Observable<Response<ConnectToGameResponse>>

    fun resetGameData()

    fun getPlayerPlaneNo(pos: Int): Plane

    fun sendPlanePositions(request: SendPlanePositionsRequest): Observable<Response<SendPlanePositionsResponse>>

    fun setComputerPlanes(plane1_x: Int, plane1_y: Int, plane1_orient: Orientation,
                          plane2_x: Int, plane2_y: Int, plane2_orient: Orientation,
                          plane3_x: Int, plane3_y: Int, plane3_orient: Orientation): Boolean

    fun setGameStage(stage: GameStages)

    fun acquireOpponentPlanePositions(request: AcquireOpponentPositionsRequest): Observable<Response<AcquireOpponentPositionsResponse>>

    fun sendWinner(draw: Boolean, winnerId: Long): Observable<Response<SendWinnerResponse>>

    fun checkWinnerSent()

    fun setGameFragment(gameFragment: IGameFragmentMultiplayer)

    fun addToNotSentMoves(moveIndex: Int)

    fun saveNotSentMoves()

    fun computeNotReceivedMoves(): Pair<Vector<Int>, Int>

    fun prepareNotSentMoves(): Vector<SingleMoveRequest>

    fun sendMove(sendMoveRequest: SendNotSentMovesRequest): Observable<Response<SendNotSentMovesResponse>>

    fun deleteFromNotSentList()

    fun moveAlreadyReceived(idx: Int): Boolean

    fun addOpponentMove(gp: GuessPoint, idx: Int)

    fun cancelRound(gameId: Long, roundId: Long): Observable<Response<CancelRoundResponse>>

    fun startNewRound(gameId: Long, userId: Long, opponentId: Long): Observable<Response<StartNewRoundResponse>>

    override fun cancelRound()

    fun getGameStats() : GameStatistics

    fun getComputerPlaneNo(pos: Int): Plane

    fun getNotSentMoveCount(): Int

    fun getReceivedMovesCount(): Int
}