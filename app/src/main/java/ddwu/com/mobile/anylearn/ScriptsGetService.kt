import ddwu.com.mobile.anylearn.MyScriptList
import ddwu.com.mobile.anylearn.WithaiSelect
import retrofit2.Call
import retrofit2.http.*

interface ScriptsGetService {
    @GET("api/v1/scripts/")
    fun scripListGet(
        @Header("Cookie") cookieToken: String,
    ): Call<MyScriptList.ScriptListResponseModel>
}


