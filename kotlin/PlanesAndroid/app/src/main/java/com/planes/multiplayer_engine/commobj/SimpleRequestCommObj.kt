package com.planes.multiplayer_engine.commobj

import androidx.fragment.app.FragmentActivity
import io.reactivex.Observable
import retrofit2.Response

class SimpleRequestCommObj<A>(hideLoading: () -> Unit, showLoading: () -> Unit,
                           createObservable: () -> Observable<Response<A>>, errorStrg: String, unknownError: String,
                              errorStrgNotLoggedIn: String, errorStrgNotConnected: String, doWhenSuccess: (A) -> String,
                           finalizeRequestSuccessful: () -> Unit, activity: FragmentActivity):
        BasisCommObj<A>(hideLoading, showLoading, true, createObservable, errorStrg, unknownError, true, true,
            errorStrgNotLoggedIn, errorStrgNotConnected, false, "", "",
            {  a: String, b: String -> a }, doWhenSuccess, false, { a: String, b: String, c: String -> {} }, finalizeRequestSuccessful,
            { -> {} }, activity )
{
}