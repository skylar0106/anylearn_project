import ddwu.com.mobile.anylearn.SignIn
import retrofit2.Call
import retrofit2.http.*

interface SignInApiService {
//    @Headers("Content-Type: application/json")
    @POST("api/v1/users/sign_in")
    fun getToken(@Body request: SignIn.YourTokenRequestModel): Call<SignIn.YourTokenResponseModel>
}