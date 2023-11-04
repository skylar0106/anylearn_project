package ddwu.com.mobile.anylearn

import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface MyDiarySaveApiService {
//    @Headers("Content-Type: application/json")
    @PUT("api/v1/diaries/{date}")

    fun postSubject(
        @Header("X-Csrftoken") csrfToken: String,
        @Header("Cookie") cookieToken: String,
        @Path("date") date: String, // 여기에 날짜 전달
        @Body request: MyDiaryScript.RequestModel
    ): Call<Void>
}
interface MyDiaryDeleteApiService {
    //    @Headers("Content-Type: application/json")
    @DELETE("api/v1/diaries/{date}")

    fun postSubject(
        @Header("X-Csrftoken") csrfToken: String,
        @Header("Cookie") cookieToken: String,
        @Path("date") date: String, // 여기에 날짜 전달
    ): Call<Void>
}