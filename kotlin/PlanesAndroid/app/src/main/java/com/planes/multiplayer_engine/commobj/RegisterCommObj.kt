package com.planes.multiplayer_engine.commobj

import androidx.fragment.app.FragmentActivity
import com.planes.multiplayer_engine.responses.LoginResponse
import com.planes.multiplayer_engine.responses.RegistrationResponse
import io.reactivex.Observable
import retrofit2.Response

class RegisterCommObj(hideLoading: () -> Unit, showLoading: () -> Unit,
                   createObservable: () -> Observable<Response<RegistrationResponse>>, errorStrg: String, unknownError: String,
                   username: String, password: String, userPasswordValidation: (String, String) -> String, doWhenSuccess: (RegistrationResponse) -> String,
                   finalizeRequestSuccessful: () -> Unit, activity: FragmentActivity
):

    BasisCommObj<RegistrationResponse>(hideLoading, showLoading, true, createObservable, errorStrg, unknownError, false,
        false, "", "", true, username, password, userPasswordValidation,
        doWhenSuccess, false,
        { a: String, b:String, c: String -> "" } , finalizeRequestSuccessful, { -> {} },activity)
{
}