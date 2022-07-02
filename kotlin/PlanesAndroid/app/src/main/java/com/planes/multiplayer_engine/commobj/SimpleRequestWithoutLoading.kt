package com.planes.multiplayer_engine.commobj

import androidx.fragment.app.FragmentActivity
import io.reactivex.Observable
import retrofit2.Response

class SimpleRequestWithoutLoadingCommObj<A>(createObservable: () -> Observable<Response<A>>, errorStrg: String, unknownError: String,
                              errorStrgNotLoggedIn: String, errorStrgNotConnected: String, doWhenSuccess: (A) -> String,
                              finalizeRequestSuccessful: () -> Unit, activity: FragmentActivity):
    BasisCommObj<A>(
        false, createObservable, errorStrg, unknownError, true, true,
        errorStrgNotLoggedIn, errorStrgNotConnected, false, "", "",
        {  a: String, b: String -> a }, doWhenSuccess, false, { a: String, b: String, c: String -> {} }, finalizeRequestSuccessful,
        { -> {}}, activity )
{
}