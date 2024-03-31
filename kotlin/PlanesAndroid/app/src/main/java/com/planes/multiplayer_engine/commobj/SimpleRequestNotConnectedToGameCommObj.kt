package com.planes.multiplayer_engine.commobj

import androidx.fragment.app.FragmentActivity
import io.reactivex.Observable
import retrofit2.Response

class SimpleRequestNotConnectedToGameCommObj<A>(createObservable: () -> Observable<Response<A>>, errorStrg: String, unknownError: String,
                              errorStrgNotLoggedIn: String, doWhenSuccess: (A) -> String,
                              finalizeRequestSuccessful: () -> Unit, activity: FragmentActivity
):
    BasisCommObj<A>(true, createObservable, errorStrg, unknownError, true, false,
        errorStrgNotLoggedIn, "", false, "", "",
        { a: String, _: String -> a }, doWhenSuccess, false, { _: String, _: String, _: String -> }, finalizeRequestSuccessful,
        { }, activity )