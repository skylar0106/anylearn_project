import ddwu.com.mobile.anylearn.MyDiaryScript
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
        @Path("roomId") roomId: String,
    ): Call<MyScript.ScriptResponseModel>
}
interface ScriptDeleteService {
    @DELETE("api/v1/scripts/{roomId}")
    fun scriptDelete(
        @Header("X-CSRFToken") csrfToken: String,
        @Header("Cookie") cookieToken: String,
        @Path("roomId") roomId: String,
    ): Call<Void>
}
interface ScriptAddService {
    @PUT("api/v1/scripts/{roomId}")
    fun scriptAdd(
        @Header("X-CSRFToken") csrfToken: String,
        @Header("Cookie") cookieToken: String,
        @Path("roomId") roomId: String,
        @Body request: MyScript.ScriptAddRequestModel
    ): Call<Void>
}


