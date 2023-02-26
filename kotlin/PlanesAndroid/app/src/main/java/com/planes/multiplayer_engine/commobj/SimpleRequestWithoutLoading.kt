package com.planes.multiplayer_engine.commobj

import androidx.fragment.app.FragmentActivity
import io.reactivex.Observable
import retrofit2.Response

class SimpleRequestWithoutLoadingCommObj<A>(createObservable: () -> Observable<Response<A>>, errorStrg: String, unknownError: String,
                              errorStrgNotLoggedIn: String, errorStrgNotConnected: String, doWhenSuccess: (A) -> String,
                              finalizeRequestSuccessful: () -> Unit, finalizeRequestError: () -> Unit, activity: FragmentActivity):
    BasisCommObj<A>(
        false, createObservable, errorStrg, unknownError, true, true,
        errorStrgNotLoggedIn, errorStrgNotConnected, false, "", "",
        { a: String, _: String -> a }, doWhenSuccess, false, { _: String, _: String, _: String -> },
        finalizeRequestSuccessful, finalizeRequestError, activity )
