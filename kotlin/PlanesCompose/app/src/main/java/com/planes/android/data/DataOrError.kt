package com.planes.android.data

import okhttp3.ResponseBody

class DataOrError<T>(
    var data: T? = null,
    var loading: Boolean? = null,
    var e: String? = null
) {

}