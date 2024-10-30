package ua.rikutou.studio.data.remote

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import okhttp3.ResponseBody
import ua.rikutou.studio.data.datasource.DataSourceResponse
import javax.inject.Inject

data class Error(
    val code: Int,
    val message: String,
)

fun parseError(errorBody: ResponseBody?): DataSourceResponse<Error> =
    DataSourceResponse.Error<Any>(
        message = errorBody?.let {
            try {
                GsonBuilder()
                    .setLenient()
                    .create()
                    .fromJson(it.string(), Error::class.java).message
            } catch (ex: JsonSyntaxException) {
                null
            }
        }
    )