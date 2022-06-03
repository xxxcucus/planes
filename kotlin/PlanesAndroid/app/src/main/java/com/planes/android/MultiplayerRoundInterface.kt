package com.planes.android

import com.planes.android.game.multiplayer.IGameFragmentMultiplayer
import com.planes.multiplayer_engine.MultiplayerRoundJava
import com.planes.multiplayer_engine.responses.*
import com.planes.single_player_engine.GameStages
import com.planes.single_player_engine.Orientation
import com.planes.single_player_engine.Plane
import io.reactivex.Observable
import retrofit2.Response
import java.util.*
import androidx.core.util.Pair
import com.planes.multiplayer_engine.requests.*
import com.planes.single_player_engine.GuessPoint

interface MultiplayerRoundInterface: PlanesRoundInterface {
    override fun createPlanesRound()

    fun testServerVersion(): Observable<Response<VersionResponse>>

    fun login(username: String, password: String): Observable<Response<LoginResponse>>

    fun setUserData(username: String, password: String, authToken: String)

    fun getGameId() : Long

    fun getRoundId() : Long

    fun getUserId(): Long

    fun getOpponentId(): Long

    fun getUsername(): String

    fun getOpponentName(): String

    fun register(username: String, password: String): Observable<Response<RegistrationResponse>>

    fun setRegistrationResponse(regResp: RegistrationResponse)

    fun getRegistrationResponse(): RegistrationResponse

    fun norobot(requestId: Long, answer: String): Observable<Response<NoRobotResponse>>

    fun isUserLoggedIn(): Boolean

    fun isUserConnectedToGame(): Boolean

    fun refreshGameStatus(gameName: String): Observable<retrofit2.Response<GameStatusResponse>>

    fun createGame(gameName: String): Observable<retrofit2.Response<CreateGameResponse>>

    fun setGameData(gameCreationResponse: CreateGameResponse)

    fun setGameData(connectToGameResponse: ConnectToGameResponse)

    fun setGameData(gameStatusResponse: GameStatusResponse)

    fun setUserId(userid: Long)

    fun connectToGame(gameName: String): Observable<retrofit2.Response<ConnectToGameResponse>>

    fun resetGameData()

    fun getPlayerPlaneNo(pos: Int): Plane

    fun sendPlanePositions(request: SendPlanePositionsRequest): Observable<retrofit2.Response<SendPlanePositionsResponse>>

    fun setComputerPlanes(plane1_x: Int, plane1_y: Int, plane1_orient: Orientation,
                          plane2_x: Int, plane2_y: Int, plane2_orient: Orientation,
                          plane3_x: Int, plane3_y: Int, plane3_orient: Orientation): Boolean

    fun setGameStage(stage: GameStages)

    fun acquireOpponentPlanePositions(request: AcquireOpponentPositionsRequest): Observable<retrofit2.Response<AcquireOpponentPositionsResponse>>

    fun sendWinner(draw: Boolean, winnerId: Long): Observable<retrofit2.Response<SendWinnerResponse>>

    fun setGameFragment(gameFragment: IGameFragmentMultiplayer)

    fun addToNotSentMoves(moveIndex: Int)

    fun saveNotSentMoves()

    fun computeNotReceivedMoves(): Pair<Vector<Int>, Int>

    fun prepareNotSentMoves(): Vector<SingleMoveRequest>

    fun sendMove(sendMoveRequest: SendNotSentMovesRequest): Observable<retrofit2.Response<SendNotSentMovesResponse>>

    fun deleteFromNotSentList()

    fun moveAlreadyReceived(idx: Int): Boolean

    fun addOpponentMove(gp: GuessPoint, idx: Int)
}