package com.planes.multiplayer_engine.commobj

import androidx.fragment.app.FragmentActivity
import com.planes.android.MainActivity
import com.planes.android.MultiplayerRoundInterface
import com.planes.android.Tools
import com.planes.multiplayer_engine.MultiplayerRoundJava
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Headers
import retrofit2.Response
import java.util.concurrent.TimeUnit

/*
Basis class for communicating with the game server via Retrofit
 - withLoadingAnimation: show a loader animation from start of the request until receiving of the response
 - createObservable: function creating the Retrofit observable
 - errorStrg: error string displayed in case of an known error
 - unknownError: error string displayed in case of an unknown error
 - shouldBeLoggedIn: should an user be logged in when this request is made
 - shouldBeConnectedToGame: should an user be connected to game when sending this request
 - errorStrgNotLoggedIn: error message shown when no user is logged in and it should be
 - errorStrgNotConnected: error message shown when no user is connected to a game and it should be
 - withCredentials: provide credentials for credentials validation
 - username: username
 - password: password
 - userPasswordValidation: function used for user password validation
 - doWhenSuccess: function performed when the request was successfull
 - checkAuthorization: verify the received "Authorization" header
 - saveCredentials: save the used credentials (user, password, token)
 - finalizeRequestSuccessful: function to end request when the request was successfull
 - finalizeRequestError: function to end request when the request failed
 - activity: the Fragment Activity from which the request is made
 */

open class BasisCommObj<A>(withLoadingAnimation: Boolean,
        createObservable: () -> Observable<Response<A>>, errorStrg: String, unknownError: String, shouldBeLoggedIn: Boolean,
        shouldBeConnectedToGame: Boolean, errorStrgNotLoggedIn: String, errorStrgNotConnected: String,
        withCredentials: Boolean, username: String, password: String, userPasswordValidation: (String, String) -> String,
        doWhenSuccess: (A) -> String,
        checkAuthorization: Boolean, saveCredentials: (String, String, String) -> Unit,
        finalizeRequestSuccessful: () -> Unit, finalizeRequestError: () -> Unit, activity: FragmentActivity) {

    private lateinit var m_RetrofitSubscription: Disposable
    protected var m_PlaneRound: MultiplayerRoundInterface = MultiplayerRoundJava()
    private lateinit var m_Observable: Observable<Response<A>>
    protected var m_RequestError: Boolean = false
    protected var m_RequestErrorString: String = ""
    private var m_GenericErrorString: String
    private var m_UnknownErrorString: String
    private var m_WithLoadingAnimation: Boolean
    private var m_ErrorStringNotConnected: String
    protected var m_ErrorStringNotLoggedIn: String
    private var m_ShouldBeLoggedIn: Boolean
    private var m_ShouldBeConnectedToGame: Boolean
    protected var m_CreateObservable: () -> Observable<Response<A>>

    protected var m_HideLoadingLambda: () -> Unit
    protected var m_ShowLoadingLambda: () -> Unit
    protected var m_IsActive = true  //Can I create another request (user clicks fast one time after the other)

    private var m_WithCredentials: Boolean
    protected var m_UserName: String
    protected var m_Password: String
    private var m_CheckAuthorization: Boolean
    protected var m_UserPasswordValidation: (String, String) -> String
    protected var m_SaveCredentials: (String, String, String) -> Unit

    protected var m_FinalizeRequestSuccessful: () -> Unit
    protected var m_FinalizeRequestError: () -> Unit
    protected var m_DoWhenSuccess: (A) -> String

    protected var m_MainActivity: FragmentActivity

    init {
        (m_PlaneRound as MultiplayerRoundJava).createPlanesRound()
        m_HideLoadingLambda = ::hideLoading
        m_ShowLoadingLambda = ::showLoading
        m_WithLoadingAnimation = withLoadingAnimation
        m_GenericErrorString = errorStrg
        m_UnknownErrorString = unknownError
        m_ErrorStringNotConnected = errorStrgNotConnected
        m_ErrorStringNotLoggedIn = errorStrgNotLoggedIn
        m_ShouldBeLoggedIn = shouldBeLoggedIn
        m_ShouldBeConnectedToGame = shouldBeConnectedToGame
        m_CreateObservable = createObservable

        m_WithCredentials = withCredentials
        m_UserName = username
        m_Password = password
        m_CheckAuthorization = checkAuthorization
        m_UserPasswordValidation = userPasswordValidation
        m_SaveCredentials = saveCredentials

        m_FinalizeRequestSuccessful = finalizeRequestSuccessful
        m_FinalizeRequestError = finalizeRequestError
        m_DoWhenSuccess = doWhenSuccess
        m_MainActivity = activity
    }

    open fun makeRequest() {
        m_RequestError = false
        m_RequestErrorString = ""

        if (m_ShouldBeLoggedIn) {
            if (!userLoggedIn()) {
                finalizeRequest()
                return
            }
        }

        if (m_ShouldBeConnectedToGame) {
            if (!connectedToGame()) {
                finalizeRequest()
                return
            }
        }

        if (m_WithCredentials) {
            if (!userPasswordValidation()) {
                finalizeRequest()
                return
            }
        }

        m_Observable = m_CreateObservable()
        sendRequest()
    }

    open fun finishedRequest(code: Int, jsonErrorString: String?, headrs: Headers, body: A?) {
        if (m_CheckAuthorization)
            finishedRequestAuthorization(jsonErrorString, headrs)
        else
            finishedRequestBody(jsonErrorString, body)
    }

    private fun finishedRequestBody(jsonErrorString: String?, body: A?) {
        if (body != null) {
            val errorStrg = m_DoWhenSuccess(body)
            if (errorStrg.isNotEmpty()) {
                m_RequestError = true
                m_RequestErrorString = errorStrg
            }
        } else {
            m_RequestErrorString = Tools.parseJsonError(jsonErrorString, m_GenericErrorString, m_UnknownErrorString)
            m_RequestError = true
        }
        finalizeRequest()
    }

    private fun finishedRequestAuthorization(jsonErrorString: String?, headrs: Headers) {
        if (headrs["Authorization"] != null) {
            val authorizationHeader = headrs["Authorization"]
            //TODO: should Bearer be removed from token?

            m_SaveCredentials(m_UserName, m_Password, authorizationHeader!!)

        } else {
            m_RequestErrorString = Tools.parseJsonError(jsonErrorString, m_GenericErrorString, m_UnknownErrorString)
            m_RequestError = true
        }
        finalizeRequest()
    }

    private fun setRequestError(errorMsg: String) {
        m_RequestError = true
        m_RequestErrorString = errorMsg
        finalizeRequest()
    }

    open fun finalizeRequest() {
        m_IsActive = false

        if (m_RequestError) {
            (m_MainActivity as MainActivity).onWarning(m_RequestErrorString)
            m_FinalizeRequestError()
        } else {
            m_FinalizeRequestSuccessful()
        }
    }

    fun disposeSubscription() {
        if (this::m_RetrofitSubscription.isInitialized) {
            m_RetrofitSubscription.dispose()
            m_IsActive = false
        }
    }

    fun isActive(): Boolean {
        return m_IsActive
    }


    private fun sendRequest() {
        if (m_WithLoadingAnimation) {
            m_RetrofitSubscription = m_Observable
                .delay (1500, TimeUnit.MILLISECONDS ) //TODO: to remove this
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { doOnSubscribe() }
                .doOnTerminate { doOnTerminate() }
                .doOnComplete { doOnTerminate() }
                .subscribe({data -> finishedRequest(data.code(), data.errorBody()?.string(), data.headers(), data.body())}
                    , {error -> error.localizedMessage?.let { setRequestError(it) } })
        } else {
            m_RetrofitSubscription = m_Observable
                .delay (1500, TimeUnit.MILLISECONDS ) //TODO: to remove this
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({data -> finishedRequest(data.code(), data.errorBody()?.string(), data.headers(), data.body())}
                    , {error -> error.localizedMessage?.let { setRequestError(it) } })
        }
    }

    private fun userLoggedIn(): Boolean {
        if (!m_PlaneRound.isUserLoggedIn()) {
            m_RequestError = true
            m_RequestErrorString = m_ErrorStringNotLoggedIn
            return false
        }
        return true
    }

    private fun connectedToGame(): Boolean {
        if (!m_PlaneRound.isUserConnectedToGame()) {
            m_RequestError = true
            m_RequestErrorString = m_ErrorStringNotConnected
            return false
        }
        return true
    }

    private fun userPasswordValidation(): Boolean {
        val error = m_UserPasswordValidation(m_UserName, m_Password)

        if (error.isNotEmpty()) {
            m_RequestError = true
            m_RequestErrorString = error
            return false
        }
        return true
    }

    private fun doOnSubscribe() {
        m_ShowLoadingLambda()
    }

    private fun doOnTerminate() {
        m_HideLoadingLambda()
    }

    fun showLoading() {
        (m_MainActivity as MainActivity).startProgressDialog()
    }

    fun hideLoading() {
        (m_MainActivity as MainActivity).stopProgressDialog()
    }
}