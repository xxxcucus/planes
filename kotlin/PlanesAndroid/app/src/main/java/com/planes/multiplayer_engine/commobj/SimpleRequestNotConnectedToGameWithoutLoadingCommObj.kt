package com.planes.multiplayer_engine.commobj

import androidx.fragment.app.FragmentActivity
import io.reactivex.Observable
import retrofit2.Response

class SimpleRequestNotConnectedToGameWithoutLoadingCommObj<A>(createObservable: () -> Observable<Response<A>>, errorStrg: String, unknownError: String,
                                                           errorStrgNotLoggedIn: String, doWhenSuccess: (A) -> String,
                                                           finalizeRequestSuccessful: () -> Unit, finalizeRequestError: () -> Unit, activity: FragmentActivity
):
    BasisCommObj<A>(false, createObservable, errorStrg, unknownError, true, false,
        errorStrgNotLoggedIn, "", false, "", "",
        { a: String, _: String -> a }, doWhenSuccess, false, { _: String, _: String, _: String, _:A? -> }, finalizeRequestSuccessful,
        finalizeRequestError, activity )