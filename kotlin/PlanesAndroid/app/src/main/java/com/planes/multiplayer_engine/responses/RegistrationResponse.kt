package com.planes.multiplayer_engine.responses

import com.google.gson.annotations.SerializedName
import java.util.*

data class RegistrationResponse (
    @SerializedName("id")
    var m_Id: String,
    @SerializedName("username")
    var m_Username: String,
    @SerializedName("createdAt")
    var m_CreatedAt: Date,
    @SerializedName("question")
    var m_Question: String,
    @SerializedName("image_id_1")
    val m_ImageId_1: String,
    @SerializedName("image_id_2")
    val m_ImageId_2: String,
    @SerializedName("image_id_3")
    val m_ImageId_3: String,
    @SerializedName("image_id_4")
    val m_ImageId_4: String,
    @SerializedName("image_id_5")
    val m_ImageId_5: String,
    @SerializedName("image_id_6")
    val m_ImageId_6: String,
    @SerializedName("image_id_7")
    val m_ImageId_7: String,
    @SerializedName("image_id_8")
    val m_ImageId_8: String,
    @SerializedName("image_id_9")
    val m_ImageId_9: String
)