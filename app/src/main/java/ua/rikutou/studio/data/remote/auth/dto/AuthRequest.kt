package ua.rikutou.studio.data.remote.auth.dto

import com.google.gson.annotations.SerializedName

data class AuthRequest(
    @SerializedName("name") val name: String,
    @SerializedName("password") val password: String
)
