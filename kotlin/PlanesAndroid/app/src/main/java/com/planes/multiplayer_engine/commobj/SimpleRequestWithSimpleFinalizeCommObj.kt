package com.planes.multiplayer_engine.commobj

import androidx.fragment.app.FragmentActivity
import com.planes.android.MainActivity
import com.planes.android.R
import io.reactivex.Observable
import retrofit2.Response


class SimpleRequestWithSimpleFinalizeCommObj<A>(createObservable: () -> Observable<Response<A>>, errorStrg: String, unknownError: String,
                              successMsg: String,
                              activity: FragmentActivity
):
    BasisCommObj<A>(true, createObservable, errorStrg, unknownError, false, false,
        successMsg, "", false, "", "",
        {  a: String, b: String -> a },
        { a : A -> "" }, false, { a: String, b: String, c: String -> {} },
        { -> {} },
        { -> {} }, activity )
{

    override fun  finalizeRequest() {
       m_IsActive = false
        (m_MainActivity as MainActivity).startRegistrationFragment(m_RequestError,
            if (m_RequestError) m_RequestErrorString else m_ErrorStringNotLoggedIn)  //Hack to integrate class into this pattern
   }

}