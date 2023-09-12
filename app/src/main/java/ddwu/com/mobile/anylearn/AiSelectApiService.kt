package ddwu.com.mobile.anylearn

import retrofit2.Call
import retrofit2.http.*

interface AiSelectApiService {
//    @Headers("Content-Type: application/json")
    @POST("api/v1/chats/")

    fun postSubject(
//        @Header("Authorization") authorization: String, // Authorization 헤더 추가
        @Header("X-Csrftoken") csrfToken: String,
        @Header("Cookie") cookieToken: String,
        @Body request: WithaiSelect.SelectRequestModel
    ): Call<WithaiSelect.SelectResponseModel>
}
