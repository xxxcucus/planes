package com.planes.android.data

class DataOrError<T>(
    var data: T? = null,
    var loading: Boolean? = null,
    var e: String? = null
)