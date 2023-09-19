import ddwu.com.mobile.anylearn.SettingPageAdapter
import ddwu.com.mobile.anylearn.SignIn
import retrofit2.Call
import retrofit2.http.*

interface LogoutApiService {
//    @Headers("Content-Type: application/json")
    @POST("api/v1/users/sign_out")
    fun postSubject(
    @Header("X-Csrftoken") csrfToken: String,
    @Header("Cookie") cookieToken: String,
    ): Call<Void>
}