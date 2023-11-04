import ddwu.com.mobile.anylearn.SettingPageAdapter
import ddwu.com.mobile.anylearn.SignIn
import retrofit2.Call
import retrofit2.http.*

interface AccountSettingApiService {
//    @Headers("Content-Type: application/json")
    @GET("api/v1/users/me")
    fun postSubject(
    @Header("X-Csrftoken") csrfToken: String,
    @Header("Cookie") cookieToken: String,
    ): Call<SettingPageAdapter.AccountResponseModel>
}
interface LogoutApiService {
    //    @Headers("Content-Type: application/json")
    @POST("api/v1/users/sign_out")
    fun postSubject(
        @Header("X-Csrftoken") csrfToken: String,
        @Header("Cookie") cookieToken: String,
    ): Call<Void>
}
interface RemoveApiService {
    //    @Headers("Content-Type: application/json")
    @DELETE("api/v1/users/me")
    fun postSubject(
        @Header("X-Csrftoken") csrfToken: String,
        @Header("Cookie") cookieToken: String,
    ): Call<Void>
}