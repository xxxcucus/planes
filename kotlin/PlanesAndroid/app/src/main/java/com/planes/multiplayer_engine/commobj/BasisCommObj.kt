package com.planes.multiplayer_engine.commobj

import android.app.Activity
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

open class BasisCommObj<A>(hideLoading: () -> Unit, showLoading: () -> Unit, withLoadingAnimation: Boolean,
        createObservable: () -> Observable<Response<A>>, errorStrg: String, unknownError: String, shouldBeLoggedIn: Boolean,
        shouldBeConnectedToGame: Boolean, errorStrgNotLoggedIn: String, errorStrgNotConnected: String,
        withCredentials: Boolean, username: String, password: String, userPasswordValidation: (String, String) -> String,
        doWhenSuccess: (A) -> String,
        checkAuthorization: Boolean, saveCredentials: (String, String, String) -> Unit,
        finalizeRequestSuccessful: () -> Unit, finalizeRequestError: () -> Unit, activity: FragmentActivity) {

    protected lateinit var m_RetrofitSubscription: Disposable
    protected var m_PlaneRound: MultiplayerRoundInterface
    protected lateinit var m_Observable: Observable<Response<A>>
    protected var m_RequestError: Boolean = false
    protected var m_RequestErrorString: String = ""
    protected var m_GenericErrorString: String
    protected var m_UnknownErrorString: String
    protected var m_WithLoadingAnimation: Boolean
    protected var m_ErrorStringNotConnected: String
    protected var m_ErrorStringNotLoggedIn: String
    protected var m_ShouldBeLoggedIn: Boolean
    protected var m_ShouldBeConnectedToGame: Boolean
    protected var m_CreateObservable: () -> Observable<Response<A>>

    protected var m_HideLoadingLambda: () -> Unit
    protected var m_ShowLoadingLambda: () -> Unit
    protected var m_IsActive = true  //Can I create another request (user clicks fast one time after the other)

    protected var m_WithCredentials: Boolean
    protected var m_UserName: String
    protected var m_Password: String
    protected var m_CheckAuthorization: Boolean
    protected var m_UserPasswordValidation: (String, String) -> String
    protected var m_SaveCredentials: (String, String, String) -> Unit

    protected var m_FinalizeRequestSuccessful: () -> Unit
    protected var m_FinalizeRequestError: () -> Unit
    protected var m_DoWhenSuccess: (A) -> String

    protected var m_MainActivity: FragmentActivity

    init {
        m_PlaneRound = MultiplayerRoundJava()
        (m_PlaneRound as MultiplayerRoundJava).createPlanesRound()
        m_HideLoadingLambda = hideLoading
        m_ShowLoadingLambda = showLoading
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
            finishedRequestAuthorization(code, jsonErrorString, headrs, body)
        else
            finishedRequestBody(code, jsonErrorString, headrs, body)
    }

    private fun finishedRequestBody(code: Int, jsonErrorString: String?, headrs: Headers, body: A?) {
        if (body != null) {
            var errorStrg = m_DoWhenSuccess(body!!)
            if (!errorStrg.isNullOrEmpty()) {
                m_RequestError = true
                m_RequestErrorString = errorStrg
            }
        } else {
            m_RequestErrorString = Tools.parseJsonError(jsonErrorString, m_GenericErrorString, m_UnknownErrorString)
            m_RequestError = true
        }
        finalizeRequest()
    }

    fun finishedRequestAuthorization(code: Int, jsonErrorString: String?, headrs: Headers, body: A?) {
        if (headrs.get("Authorization") != null) {
            var authorizationHeader = headrs.get("Authorization")
            //TODO: should Bearer be removed from token?

            m_SaveCredentials(m_UserName, m_Password, authorizationHeader!!)

        } else {
            m_RequestErrorString = Tools.parseJsonError(jsonErrorString, m_GenericErrorString, m_UnknownErrorString)
            m_RequestError = true
        }
        finalizeRequest()
    }

    fun setRequestError(errorMsg: String) {
        m_RequestError = true
        m_RequestErrorString = errorMsg
        finalizeRequest()
    }

    fun finalizeRequest() {
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


    fun sendRequest() {
        if (m_WithLoadingAnimation) {
            m_RetrofitSubscription = m_Observable
                .delay (1500, TimeUnit.MILLISECONDS ) //TODO: to remove this
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _ -> doOnSubscribe() }
                .doOnTerminate { doOnTerminate() }
                .doOnComplete { doOnTerminate() }
                .subscribe({data -> finishedRequest(data.code(), data.errorBody()?.string(), data.headers(), data.body())}
                    , {error -> setRequestError(error.localizedMessage.toString())});
        } else {
            m_RetrofitSubscription = m_Observable
                .delay (1500, TimeUnit.MILLISECONDS ) //TODO: to remove this
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({data -> finishedRequest(data.code(), data.errorBody()?.string(), data.headers(), data.body())}
                    , {error -> setRequestError(error.localizedMessage.toString())});
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
        var error = m_UserPasswordValidation(m_UserName, m_Password)

        if (!error.isNullOrEmpty()) {
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
}