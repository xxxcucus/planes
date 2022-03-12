package com.planes.android.register

class PhotoModel(id: Int, selected: Boolean) {

    var m_ImageId: Int
    var m_Selected: Boolean

    init {
        m_ImageId = id
        m_Selected = selected
    }
}