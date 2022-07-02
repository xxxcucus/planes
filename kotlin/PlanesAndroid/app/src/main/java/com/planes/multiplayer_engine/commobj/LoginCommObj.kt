package com.planes.multiplayer_engine.commobj

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import com.planes.multiplayer_engine.responses.LoginResponse
import io.reactivex.Observable
import retrofit2.Response

class LoginCommObj(createObservable: () -> Observable<Response<LoginResponse>>, errorStrg: String, unknownError: String,
                     username: String, password: String, userPasswordValidation: (String, String) -> String,
                     saveCredentials: (String, String, String) -> Unit,
                   finalizeRequestSuccessful: () -> Unit, activity: FragmentActivity
):

        BasisCommObj<LoginResponse>(true, createObservable, errorStrg, unknownError, false,
        false, "", "", true, username, password, userPasswordValidation,
            { a: LoginResponse -> "" }, true, saveCredentials, finalizeRequestSuccessful, { -> {} },activity)
{
}