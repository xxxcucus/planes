package com.planes.multiplayer_engine.commobj

import androidx.fragment.app.FragmentActivity
import io.reactivex.Observable
import retrofit2.Response

class SimpleRequestWithoutCredentialsCommObj<A>(createObservable: () -> Observable<Response<A>>, errorStrg: String, unknownError: String,
                              doWhenSuccess: (A) -> String, finalizeRequestSuccessful: () -> Unit,
                                finalizeRequestError: () -> Unit, activity: FragmentActivity):
    BasisCommObj<A>(true, createObservable, errorStrg, unknownError, false, false,
        "", "", false, "", "",
        { a: String, _: String -> a }, doWhenSuccess, false, { _: String, _: String, _: String, _:A -> }, finalizeRequestSuccessful,
        finalizeRequestError, activity )
