package com.planes.multiplayer_engine

import com.planes.multiplayer_engine.requests.*
import com.planes.multiplayer_engine.responses.*
import com.planes.single_player_engine.*
import io.reactivex.Observable
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import org.mindrot.jbcrypt.BCrypt
import okhttp3.Interceptor

import okhttp3.OkHttpClient
import androidx.core.util.Pair
import com.planes.android.game.multiplayer.IGameFragmentMultiplayer
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.max

class MultiplayerRound(rowNo: Int, colNo: Int, planeNo: Int) {
    //communication structures
    private lateinit var m_Service: MultiplayerCommApi
    private var m_GameData: GameData = GameData()
    private var m_UserData: UserData = UserData()
    private lateinit var m_RegistrationData: RegistrationResponse

    //http client options
    private val OK_HTTP_CLIENT_TIMEOUT: Long = 300
    private val HTTP_LOGGING_INTERCEPTOR = HttpLoggingInterceptor()
    private val TOKEN_EXPIRATION_TIME_MINUTES = 30
    //TODO to adapt this to login requirements
    private val HTTP_HEADERS = constructHeaderInterceptor()
    private val HTTP_ORIGIN_HEADER = "Origin"
    private val HTTP_ORIGIN_VALUE = "Android"

    //whether the computer or the player moves first
    private var m_isComputerFirst = false

    //the  game statistics
    private var m_gameStats: GameStatistics

    //the player and computer's grid
    private var m_PlayerGrid: PlaneGrid
    private var m_ComputerGrid: PlaneGrid

    //the list of guesses for computer and player
    private var m_computerGuessList: Vector<GuessPoint>
    private var m_playerGuessList: Vector<GuessPoint>

    private var m_State = GameStages.GameNotStarted

    //size of the grid and number of planes
    private var m_rowNo = 10
    private var m_colNo = 10
    private var m_planeNo = 3

    private var m_PlayerMoveIndex = 0
    private var m_ComputerMoveIndex = 0
    private var m_WinnerFound: Boolean = false
    private var m_WinnerSent: Boolean = false

    private var m_NotSentMoves: Vector<Int> = Vector<Int>()
    private var m_LastNotSentMoveIndexSucces: Vector<Int> = Vector<Int>()
    private var m_ReceivedMoves: Vector<Int> = Vector<Int>()

    private lateinit var m_GameFragmentMultiplayer: IGameFragmentMultiplayer


    private fun constructHeaderInterceptor(): Interceptor {
        return Interceptor {
            /*val request = it.request()
            val newRequest = request.newBuilder().addHeader(HTTP_ORIGIN_HEADER, HTTP_ORIGIN_VALUE).build()
            it.proceed(newRequest)*/

            val requestBuilder = it.request().newBuilder()
            //requestBuilder.header("Content-Type", "application/json")
            //requestBuilder.header("Accept", "application/json")
            it.proceed(requestBuilder.build())
        }
    }

    init {

        //communication part
        HTTP_LOGGING_INTERCEPTOR.level = HttpLoggingInterceptor.Level.NONE

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
                )
                .build()
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(OK_HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(OK_HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(OK_HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
                .callTimeout(OK_HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
                .followSslRedirects(true)
                .retryOnConnectionFailure(true)
                .followRedirects(true)
                //.addInterceptor(HTTP_HEADERS)
                //.addInterceptor(HTTP_LOGGING_INTERCEPTOR)
                .connectionSpecs(Collections.singletonList(spec))
                .build()

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .baseUrl("https://planes.planes-android.com:8443/planesserver/")
                .build()
            m_Service = retrofit.create((MultiplayerCommApi::class.java))
        }

        //game part
        m_rowNo = rowNo
        m_colNo = colNo
        m_planeNo = planeNo

        //builds the plane grid objects
        m_PlayerGrid = PlaneGrid(m_rowNo, m_colNo, m_planeNo, false)
        m_ComputerGrid = PlaneGrid(m_rowNo, m_colNo, m_planeNo, true)

        m_computerGuessList = Vector()
        m_playerGuessList = Vector()
        m_gameStats = GameStatistics()

        reset()
        initRound()
    }

    //region communication
    fun testServerVersion(): Observable<Response<VersionResponse>> {
        return m_Service.getVersion()
    }

    fun login(username: String, password: String): Observable<Response<LoginResponse>> {

        return m_Service.login(LoginRequest(username, password))
    }

    fun setUserData(username: String, password: String, authToken: String) {
        if (m_UserData.userName != username || m_UserData.password != password) {
            //login with new credentials
            m_UserData.userName = username
            m_UserData.password = password
            m_UserData.authToken = authToken
            m_GameData.reset()
        } else {
            //token refresh
            m_UserData.authToken = authToken
        }

        m_UserData.lastTokenUpdate = LocalDateTime.now()
    }

    fun authTokenExpired(): Boolean {
        if (m_UserData.userName.isEmpty())
            return false
        return m_UserData.lastTokenUpdate != null && m_UserData.lastTokenUpdate!!.until(LocalDateTime.now(), ChronoUnit.MINUTES) > TOKEN_EXPIRATION_TIME_MINUTES
    }

    fun getUsername() : String {
        return m_UserData.userName
    }

    fun getPassword() : String {
        return m_UserData.password
    }

    fun getAuthToken(): String {
        return m_UserData.authToken
    }

    fun getOpponentName() : String {
        return m_GameData.otherUsername
    }

    fun getGameId() : Long {
        return m_GameData.gameId
    }

    fun getRoundId() : Long {
        return m_GameData.roundId
    }

    fun getUserId(): Long {
        return m_GameData.userId
    }

    fun getOpponentId(): Long {
        return m_GameData.otherUserId
    }

    fun getGameData(): GameData {
        return m_GameData
    }

    fun register(username: String, password: String): Observable<Response<RegistrationResponse>> {
        val bchash = BCrypt.hashpw(password, BCrypt.gensalt())
        return m_Service.register(RegistrationRequest(username, bchash))
    }

    fun setRegistrationResponse(regResp: RegistrationResponse) {
        m_RegistrationData = regResp
    }

    fun getRegistrationResponse(): RegistrationResponse {
        return m_RegistrationData
    }

    fun norobot(requestId: Long, answer: String): Observable<Response<NoRobotResponse>> {
        return m_Service.norobot(NoRobotRequest(requestId.toString(), answer))
    }

    fun isUserLoggedIn(): Boolean {
        return m_UserData.userName.isNotEmpty() && m_UserData.authToken.isNotEmpty()
    }

    fun isUserConnectedToGame(): Boolean {
        return m_GameData.gameId != 0L && m_GameData.userId != 0L && m_GameData.otherUserId != 0L
    }

    fun refreshGameStatus(gameName: String/*, gameId: String, userName: String, userId: String*/):
            Observable<Response<GameStatusResponse>> {
        return m_Service.refreshGameStatus(m_UserData.authToken,
            GameStatusRequest(gameName, m_UserData.userName, m_UserData.userId.toString() , m_GameData.gameId.toString()))
    }

    fun createGame(gameName: String): Observable<Response<CreateGameResponse>> {
        return m_Service.createGame(m_UserData.authToken,
            CreateGameRequest(gameName, m_UserData.userName, m_UserData.userId.toString() , m_GameData.gameId.toString())
        )
    }

    fun setGameData(gameCreationResponse: CreateGameResponse) {
        val resetScore = gameCreationResponse.m_GameName != m_GameData.gameName
        m_GameData.setFromCreateGameResponse(gameCreationResponse)
        if (resetScore)
            m_gameStats.resetGameScore()
    }

    fun setGameData(connectToGameResponse: ConnectToGameResponse) {
        val resetScore = connectToGameResponse.m_GameName != m_GameData.gameName
        m_GameData.setFromConnectToGameResponse(connectToGameResponse)
        if (resetScore)
            m_gameStats.resetGameScore()
    }

    fun setGameData(gameStatusResponse: GameStatusResponse) {
        val resetScore = gameStatusResponse.m_GameName != m_GameData.gameName
        m_GameData.setFromGameStatusResponse(gameStatusResponse, m_UserData.userId, m_UserData.userName)
        if (resetScore)
            m_gameStats.resetGameScore()
    }

    fun setUserId(userid: Long) {
        m_UserData.userId = userid
    }

    fun setRoundId(roundId: Long) {
        m_GameData.roundId = roundId
    }

    fun connectToGame(gameName: String): Observable<Response<ConnectToGameResponse>> {
        return m_Service.connectToGame(m_UserData.authToken,
            ConnectToGameRequest(gameName, m_UserData.userName, m_UserData.userId.toString() , m_GameData.gameId.toString())
        )
    }

    fun resetGameData() {
        m_GameData.reset()
    }

    //endregion

    //region AbstractPlanesRound

    fun reset() {
        m_PlayerGrid.resetGrid()
        m_ComputerGrid.resetGrid()
        m_playerGuessList.clear()
        m_computerGuessList.clear()
        m_gameStats.reset()

        m_PlayerMoveIndex = 0
        m_ComputerMoveIndex = 0
        m_WinnerFound = false
        m_WinnerSent = false
        m_GameData.reset()
        m_UserData.reset()
    }

    fun initRound() {
        m_PlayerGrid.initGrid()
        m_ComputerGrid.initGrid()
        m_State = GameStages.BoardEditing
        m_isComputerFirst = !m_isComputerFirst
        m_playerGuessList.clear()
        m_computerGuessList.clear()
        m_gameStats.reset()

        m_WinnerFound = false
        m_WinnerSent = false
        m_PlayerMoveIndex = 0
        m_ComputerMoveIndex = 0
        m_NotSentMoves.clear()
        m_ReceivedMoves.clear()

        //emit gameStatsUpdated(m_gameStats); TODO
        //round id is set through separate function
    }

    //based on a guesspoint updates the game stats
    private fun updateGameStats(gp: GuessPoint, isComputer: Boolean): Boolean
    {
        return if ((!isComputer && !m_gameStats.playerFinished(m_planeNo)) || (isComputer && !m_gameStats.computerFinished(m_planeNo))) {
            m_gameStats.updateStats(gp, isComputer)
            true
        } else {
            false
        }
    }

    fun doneEditing() {
        m_State = GameStages.Game
    }

    /*
    -2 - plane head
    -1 - plane intersection
    0 - is not on plane
    i - plane but not head
    */
    fun getPlaneSquareType(row: Int, col: Int, isComputer: Boolean): Int {
        val isOnPlane: Pair<Boolean, Int>
        if (isComputer) {
            isOnPlane = m_ComputerGrid.isPointOnPlane(row, col)
            if (!isOnPlane.first) return 0
            val annotation = m_ComputerGrid.getPlanePointAnnotation(isOnPlane.second)
            val planesIdx = m_ComputerGrid.decodeAnnotation(annotation)
            if (planesIdx.size > 1) {
                return -1
            }
            if (planesIdx.size == 1) {
                return if (planesIdx[0] < 0) -2 else planesIdx[0] + 1
            }
        } else {
            isOnPlane = m_PlayerGrid.isPointOnPlane(row, col)
            if (!isOnPlane.first) return 0
            val annotation = m_PlayerGrid.getPlanePointAnnotation(isOnPlane.second)
            val planesIdx = m_PlayerGrid.decodeAnnotation(annotation)
            if (planesIdx.size > 1) {
                return -1
            }
            if (planesIdx.size == 1) {
                return if (planesIdx[0] < 0) -2 else planesIdx[0] + 1
            }
        }
        return 0
    }

    fun setRoundEnd() {
        m_State = GameStages.GameNotStarted
    }

    /**
     * Rotate the plane and return false if the current plane configuration is valid.
     */
    fun rotatePlane(idx: Int): Boolean {
        m_PlayerGrid.rotatePlane(idx)
        return !(m_PlayerGrid.doPlanesOverlap() || m_PlayerGrid.isPlaneOutsideGrid)
    }

    /**
     * Move the plane left and return false if the current plane configuration is valid.
     */
    fun movePlaneLeft(idx: Int): Boolean {
        m_PlayerGrid.movePlaneLeft(idx)
        return !(m_PlayerGrid.doPlanesOverlap() || m_PlayerGrid.isPlaneOutsideGrid)
    }

    /**
     * Move the plane right and return false if the current plane configuration is valid.
     */
    fun movePlaneRight(idx: Int): Boolean {
        m_PlayerGrid.movePlaneRight(idx)
        return !(m_PlayerGrid.doPlanesOverlap() || m_PlayerGrid.isPlaneOutsideGrid)
    }

    /**
     * Move the plane upwards and return false if the current plane configuration is valid.
     */
    fun movePlaneUpwards(idx: Int): Boolean {
        m_PlayerGrid.movePlaneUpwards(idx)
        return !(m_PlayerGrid.doPlanesOverlap() || m_PlayerGrid.isPlaneOutsideGrid)
    }

    /**
     * Move the plane downwards and return false if the current plane configuration is valid.
     */
    fun movePlaneDownwards(idx: Int): Boolean {
        m_PlayerGrid.movePlaneDownwards(idx)
        return !(m_PlayerGrid.doPlanesOverlap() || m_PlayerGrid.isPlaneOutsideGrid)
    }

    fun getRowNo(): Int {
        return m_rowNo
    }

    fun getColNo(): Int {
        return m_colNo
    }

    fun getPlaneNo(): Int {
        return m_planeNo
    }

    /**
     * @param row, col - coordinates of player's guess
     * Check if a guess was already made at this position.
     */
    fun playerGuessAlreadyMade(row: Int, col: Int): Int {
        for (guess in m_playerGuessList) {
            if (guess.row() == col && guess.col() == row) return 1
        }
        return 0
    }

    //endregion

    //region MultiplayerRound

    fun playerGuessIncomplete(row: Int, col: Int): Pair<Type, PlayerGuessReaction> {
        val tp = m_ComputerGrid.getGuessResult(Coordinate2D(col, row))
        val gp = GuessPoint(col, row, tp)
        return Pair.create(tp, playerGuess(gp).second)
    }

    fun playerGuess(gp: GuessPoint): Pair<Boolean, PlayerGuessReaction> {

        val pgr = PlayerGuessReaction()
        if (m_State != GameStages.Game) {
            /*QMessageBox msgBox(m_ParentWidget);
            msgBox.setText("Not ready to play game.\n You do not have the opponent's planes positions!");
            msgBox.exec();*/
            //TODO

            return Pair<Boolean, PlayerGuessReaction>(true, pgr)
        }

        //update the game statistics
        if (updateGameStats(gp, false)) {
            //add the player's guess to the list of guesses
            //assume that the guess is different from the other guesses
            m_playerGuessList.add(gp)
            m_ComputerGrid.addGuess(gp)

            //GuessPoint::Type guessResult =  m_ComputerGrid->getGuessResult(PlanesCommonTools::Coordinate2D(gp.m_row, gp.m_col));
            m_PlayerMoveIndex++
            if (this::m_GameFragmentMultiplayer.isInitialized) {
                m_GameFragmentMultiplayer.sendMove(gp, m_PlayerMoveIndex)
            }
        //TODO
        } else {
            //qDebug() << "Player has already found all planes";
        }

        if (!m_WinnerSent) {
            val pgr1 = checkRoundEnd()
            if (pgr1.m_RoundEnds) {
                if (m_NotSentMoves.isEmpty()) {
                    if (this::m_GameFragmentMultiplayer.isInitialized) {  //asynchronous call from game fragment
                        m_GameFragmentMultiplayer.sendWinner(
                            pgr1.m_IsDraw,
                            if (pgr1.m_isPlayerWinner) m_GameData.userId else m_GameData.otherUserId
                        )
                        m_WinnerSent = true
                    }
                } else {
                    if (this::m_GameFragmentMultiplayer.isInitialized  && m_State != GameStages.SendRemainingMoves) {  //asynchronous call from game fragment
                        m_GameFragmentMultiplayer.pollForOpponentMoves(false)
                    }
                }
            }

            if (pgr1.m_PlayerFinishedStartPolling) {
                if (this::m_GameFragmentMultiplayer.isInitialized && m_State != GameStages.WaitForOpponentMoves) {  //asynchronous call from game fragment
                    m_GameFragmentMultiplayer.pollForOpponentMoves(true)
                }
            }

            return Pair<Boolean, PlayerGuessReaction> (false, pgr1)
        } else {
            return Pair<Boolean, PlayerGuessReaction> (true, PlayerGuessReaction())
        }

    }

    /*void MultiplayerRound::playerGuess(const GuessPoint& gp, PlayerGuessReaction& pgr) {
        if (m_State != AbstractPlaneRound::GameStages::Game) {
            QMessageBox msgBox(m_ParentWidget);
            msgBox.setText("Not ready to play game.\n You do not have the opponent's planes positions!");
            msgBox.exec();

            return;
        }

        //update the game statistics
        if (updateGameStats(gp, false)) {
            //add the player's guess to the list of guesses
            //assume that the guess is different from the other guesses
            m_playerGuessList.push_back(gp);
            m_ComputerGrid->addGuess(gp);

            //GuessPoint::Type guessResult =  m_ComputerGrid->getGuessResult(PlanesCommonTools::Coordinate2D(gp.m_row, gp.m_col));
            m_PlayerMoveIndex++;
            sendMove(gp, m_PlayerMoveIndex);
        } else {
            //qDebug() << "Player has already found all planes";
        }

        bool draw = false;
        long int winnerId = 0;
        bool isPlayerWinner = false;

        if (!m_WinnerFound) {
            if (checkRoundEnd(draw, winnerId, isPlayerWinner)) {
                emit winnerSent(isPlayerWinner, draw);
                sendWinner(draw, winnerId);
            }
            emit gameStatsUpdated(m_gameStats);
        }
    }*/


    private fun checkRoundEnd(): PlayerGuessReaction {

        val pgr = PlayerGuessReaction()

        if (m_gameStats.computerFinished(m_planeNo) && m_gameStats.playerFinished(m_planeNo)) {
            if (m_ComputerMoveIndex > m_PlayerMoveIndex) {
                //player winner
                if (!m_WinnerFound)
                    m_gameStats.updateWins(false)
                pgr.m_RoundEnds = true
                pgr.m_IsDraw = false
                pgr.m_isPlayerWinner = true
                m_WinnerFound = true
                return pgr
            } else if (m_ComputerMoveIndex < m_PlayerMoveIndex) {
                //computer winner
                if (!m_WinnerFound)
                    m_gameStats.updateWins(true)
                pgr.m_RoundEnds = true
                pgr.m_IsDraw = false
                pgr.m_isPlayerWinner = false
                m_WinnerFound = true
                return pgr
            } else {
                //draw
                if (!m_WinnerFound)
                    m_gameStats.addDrawResult()
                pgr.m_RoundEnds = true
                pgr.m_IsDraw = true
                m_WinnerFound = true

                return pgr
            }
        }


        if (m_gameStats.computerFinished(m_planeNo) && !m_gameStats.playerFinished(m_planeNo)) {
            //qDebug() << "Computer finished and player not finished " << m_ComputerMoveIndex << " " << m_PlayerMoveIndex;
            if (m_ComputerMoveIndex <= m_PlayerMoveIndex) {
                //computer winner
                if (!m_WinnerFound)
                    m_gameStats.updateWins(true)
                pgr.m_RoundEnds = true
                pgr.m_IsDraw = false
                pgr.m_isPlayerWinner = false
                m_WinnerFound = true
                return pgr
            }
        }

        if (!m_gameStats.computerFinished(m_planeNo) && m_gameStats.playerFinished(m_planeNo)) {
            //qDebug() << "Computer not finished and player finished " << m_ComputerMoveIndex << " " << m_PlayerMoveIndex;
            if (m_ComputerMoveIndex >= m_PlayerMoveIndex) {
                //player winner
                if (!m_WinnerFound)
                    m_gameStats.updateWins(false)
                pgr.m_RoundEnds = true
                pgr.m_IsDraw = false
                pgr.m_isPlayerWinner = true
                m_WinnerFound = true
                return pgr
            } else {
                pgr.m_RoundEnds = false
                pgr.m_IsDraw = false
                pgr.m_isPlayerWinner = false
                pgr.m_PlayerFinishedStartPolling = true
                m_WinnerFound = false
            }
        }

        pgr.m_RoundEnds = false
        return pgr
    }

    //endregion

    fun getPlayerGuessesNo(): Int {
        return m_playerGuessList.size
    }
    fun getComputerGuessesNo(): Int {
        return m_computerGuessList.size
    }

    fun getPlayerGuess(idx: Int): GuessPoint {
        return if (idx < 0 || idx >= m_playerGuessList.size) GuessPoint(-1, -1, Type.Miss) else m_playerGuessList[idx].clone() as GuessPoint
    }

    fun getComputerGuess(idx: Int): GuessPoint {
        return if (idx < 0 || idx >= m_computerGuessList.size) GuessPoint(-1, -1, Type.Miss) else m_computerGuessList[idx].clone() as GuessPoint
    }

    fun getCurrentStage(): Int {
        return m_State.value
    }

    fun setGameStage(stage: GameStages) {
        m_State = stage
    }

    fun getPlayerPlaneNo(pos: Int): Plane {
        return m_PlayerGrid.getPlane(pos).second
    }

    fun sendPlanePositions(request: SendPlanePositionsRequest): Observable<Response<SendPlanePositionsResponse>> {
        return m_Service.sendPlanePositions(m_UserData.authToken, request)
    }

    fun setComputerPlanes(plane1_x: Int, plane1_y: Int, plane1_orient: Orientation,
                          plane2_x: Int, plane2_y: Int, plane2_orient: Orientation,
                          plane3_x: Int, plane3_y: Int, plane3_orient: Orientation): Boolean {
        m_State = GameStages.Game
        return m_ComputerGrid.initGridByUser(plane1_x, plane1_y, plane1_orient,
            plane2_x, plane2_y, plane2_orient, plane3_x, plane3_y, plane3_orient)
    }

    fun acquireOpponentPlanePositions(request: AcquireOpponentPositionsRequest): Observable<Response<AcquireOpponentPositionsResponse>> {
        return m_Service.acquireOpponentPlanePositions(m_UserData.authToken, request)
    }

    fun sendWinner(draw: Boolean, winnerId: Long): Observable<Response<SendWinnerResponse>> {
        return m_Service.sendWinner(m_UserData.authToken, SendWinnerRequest(m_GameData.gameId.toString(), m_GameData.roundId.toString(), winnerId.toString(), draw))
    }

    fun setGameFragment(gameFragment: IGameFragmentMultiplayer) {
        m_GameFragmentMultiplayer = gameFragment
    }

    fun addToNotSentMoves(moveIndex: Int) {
        m_NotSentMoves.add(moveIndex)
    }

    fun saveNotSentMoves() {
        m_LastNotSentMoveIndexSucces.clear()
        m_LastNotSentMoveIndexSucces.addAll(m_NotSentMoves)
    }

    fun computeNotReceivedMoves(): Pair<Vector<Int>, Int> {
        if (m_ReceivedMoves.isEmpty())
            return Pair<Vector<Int>, Int>(Vector<Int>(), 0)

        val maxReceivedMoveIndex =
            m_ReceivedMoves.indices.map { i: Int -> m_ReceivedMoves[i]}.maxOrNull()
        val notReceivedMoves = Vector<Int>()

        for (i in 0 .. maxReceivedMoveIndex!!) {
            if (!m_ReceivedMoves.contains(i))
                notReceivedMoves.add(i)
        }

        return Pair<Vector<Int>, Int>(notReceivedMoves, maxReceivedMoveIndex)
    }

    fun sendMove(sendMoveRequest: SendNotSentMovesRequest): Observable<Response<SendNotSentMovesResponse>> {
        return m_Service.sendOwnMove(m_UserData.authToken, sendMoveRequest)
    }

    fun prepareNotSentMoves(): Vector<SingleMoveRequest> {
        val retValue = Vector<SingleMoveRequest>()

        for (moveIdx in m_LastNotSentMoveIndexSucces) {
            val gp = m_playerGuessList[moveIdx - 1]
            val move = SingleMoveRequest(moveIdx, gp.m_row, gp.m_col)
            retValue.add(move)
        }

        return retValue
    }

    fun deleteFromNotSentList() {
        for (idx in m_LastNotSentMoveIndexSucces) {
            m_NotSentMoves.remove(idx)
        }
    }

    fun moveAlreadyReceived(idx: Int): Boolean {
        return m_ReceivedMoves.contains(idx)
    }

    fun addOpponentMove(gp: GuessPoint, idx: Int): Pair<Boolean, PlayerGuessReaction> {
        m_ReceivedMoves.add(idx)

        val guessResult = m_PlayerGrid.getGuessResult(Coordinate2D(gp.m_row, gp.m_col))
        gp.setType(guessResult)

        if (updateGameStats(gp, true)) {
            m_computerGuessList.add(gp)
            m_PlayerGrid.addGuess(gp)
            m_ComputerMoveIndex = max(m_ComputerMoveIndex, idx)
        } else {
            //qDebug() << "computer has already found all planes";
        }

        if (!m_WinnerSent) {
            val pgr = checkRoundEnd()
            if (pgr.m_RoundEnds) {
                if (m_NotSentMoves.isEmpty()) {
                    if (this::m_GameFragmentMultiplayer.isInitialized) {  //asynchronous call from game fragment
                        m_GameFragmentMultiplayer.sendWinner(
                            pgr.m_IsDraw,
                            if (pgr.m_isPlayerWinner) m_GameData.userId else m_GameData.otherUserId
                        )
                        m_WinnerSent = true
                    }
                } else {
                    if (this::m_GameFragmentMultiplayer.isInitialized && m_State != GameStages.SendRemainingMoves) {  //asynchronous call from game fragment
                        m_GameFragmentMultiplayer.pollForOpponentMoves(false)
                    }
                }
            }

            return Pair<Boolean, PlayerGuessReaction> (false, pgr)
        } else {
            return Pair<Boolean, PlayerGuessReaction> (true, PlayerGuessReaction())
        }
    }

    fun checkWinnerSent() {
        if (!m_WinnerSent) {
            val pgr = checkRoundEnd()
            if (pgr.m_RoundEnds) {
                if (m_NotSentMoves.isEmpty()) {
                    if (this::m_GameFragmentMultiplayer.isInitialized) {  //asynchronous call from game fragment
                        m_GameFragmentMultiplayer.sendWinner(
                            pgr.m_IsDraw,
                            if (pgr.m_isPlayerWinner) m_GameData.userId else m_GameData.otherUserId
                        )
                        m_WinnerSent = true
                    }
                } else {
                    if (this::m_GameFragmentMultiplayer.isInitialized && m_State != GameStages.SendRemainingMoves) {  //asynchronous call from game fragment
                        m_GameFragmentMultiplayer.pollForOpponentMoves(false)
                    }
                }
            }
        }
    }

    fun cancelRound(gameId: Long, roundId: Long): Observable<Response<CancelRoundResponse>> {
        return m_Service.cancelRound(m_UserData.authToken, CancelRoundRequest(gameId.toString(), roundId.toString()))
    }

    fun startNewRound(gameId: Long, userId: Long, opponentId: Long): Observable<Response<StartNewRoundResponse>> {
        return m_Service.startRound(m_UserData.authToken, StartNewRoundRequest(gameId.toString(), userId.toString(), opponentId.toString()))
    }

    fun getGameStats() : GameStatistics {
        return m_gameStats
    }

}

