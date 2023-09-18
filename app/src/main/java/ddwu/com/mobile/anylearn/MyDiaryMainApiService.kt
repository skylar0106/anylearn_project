package ddwu.com.mobile.anylearn

import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface MyDiaryMainApiService {
//    @Headers("Content-Type: application/json")
    @GET("api/v1/diaries/{date}")

    fun postSubject(
        @Header("X-Csrftoken") csrfToken: String,
        @Header("Cookie") cookieToken: String,
        @Path("date") date: String // 여기에 날짜 전달
    ): Call<MyDiaryMain.MyDiaryMainResponseModel>
}
