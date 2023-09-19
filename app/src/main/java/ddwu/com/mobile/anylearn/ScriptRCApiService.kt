import ddwu.com.mobile.anylearn.MyScript
import ddwu.com.mobile.anylearn.MyScriptList
import retrofit2.Call
import retrofit2.http.*

interface ScriptsRCApiService {
    @PUT("api/v1/scripts/{roomId}")
    fun scriptGet(
        @Header("X-CSRFToken") csrfToken: String,
        @Header("Cookie") cookieToken: String,
        @Path("roomId") roomId: String, // 여기에 날짜 전달
        @Body request : MyScript.PrRequestModel
    ): Call<MyScript.PrResponseModel>
}


