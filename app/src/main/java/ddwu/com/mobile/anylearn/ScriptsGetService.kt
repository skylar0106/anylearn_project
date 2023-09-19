import ddwu.com.mobile.anylearn.MyScript
import ddwu.com.mobile.anylearn.MyScriptList
import retrofit2.Call
import retrofit2.http.*

interface ScriptsGetService {
    @GET("api/v1/scripts/")
    fun scripListGet(
        @Header("Cookie") cookieToken: String,
    ): Call<Map<String, MutableList<MyScriptList.Item>>>
}
interface ScriptGetService {
    @GET("api/v1/scripts/{roomId}")
    fun scriptGet(
        @Header("X-CSRFToken") csrfToken: String,
        @Header("Cookie") cookieToken: String,
        @Path("roomId") roomId: String, // 여기에 날짜 전달
    ): Call<MyScript.ScriptResponseModel>
}


