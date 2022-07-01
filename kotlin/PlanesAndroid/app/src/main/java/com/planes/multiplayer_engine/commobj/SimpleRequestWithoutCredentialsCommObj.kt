package com.planes.multiplayer_engine.commobj

import androidx.fragment.app.FragmentActivity
import io.reactivex.Observable
import retrofit2.Response

class SimpleRequestWithoutCredentialsCommObj<A>(hideLoading: () -> Unit, showLoading: () -> Unit,
                              createObservable: () -> Observable<Response<A>>, errorStrg: String, unknownError: String,
                              doWhenSuccess: (A) -> String, finalizeRequestSuccessful: () -> Unit,
                                finalizeRequestError: () -> Unit, activity: FragmentActivity):
    BasisCommObj<A>(hideLoading, showLoading, true, createObservable, errorStrg, unknownError, false, false,
        "", "", false, "", "",
        {  a: String, b: String -> a }, doWhenSuccess, false, { a: String, b: String, c: String -> {} }, finalizeRequestSuccessful,
        finalizeRequestError, activity )
{
}