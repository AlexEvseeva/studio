package ua.rikutou.studio.data.remote.auth.dto

import com.google.gson.annotations.SerializedName

data class SignInResponse(
    val token: String,
    val userId: Int?,
    val userName: String?,
    val studioId: Int? = null
)