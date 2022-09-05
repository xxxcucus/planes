package com.planes.multiplayer_engine.commobj

import androidx.fragment.app.FragmentActivity
import com.planes.multiplayer_engine.responses.LoginResponse
import io.reactivex.Observable
import retrofit2.Response

class LoginWithoutLoadingCommObj(createObservable: () -> Observable<Response<LoginResponse>>, errorStrg: String, unknownError: String,
                   username: String, password: String,
                   saveCredentials: (String, String, String) -> Unit,
                   activity: FragmentActivity
):

    BasisCommObj<LoginResponse>(false, createObservable, errorStrg, unknownError, false,
        false, "", "", true, username, password,
        { _:String, _:String -> "" },
        { "" }, true, saveCredentials, { }, { },activity)
