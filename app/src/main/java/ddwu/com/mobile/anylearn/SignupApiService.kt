import ddwu.com.mobile.anylearn.SignUp1
import retrofit2.Call
import retrofit2.http.*

interface SignupApiService {
    @Headers("Content-Type: application/json")
    @POST("api/v1/users/sign_up")
    fun signup(@Body requestModel: SignUp1.SignupRequestModel): Call<SignUp1.SignupResponseModel>
}
