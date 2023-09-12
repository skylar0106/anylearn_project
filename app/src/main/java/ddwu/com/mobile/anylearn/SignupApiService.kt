import ddwu.com.mobile.anylearn.SignUp1
import ddwu.com.mobile.anylearn.SignUp2
import retrofit2.Call
import retrofit2.http.*

interface SignupApiService {
    @Headers("Content-Type: application/json")
    @POST("api/v1/users/sign_up_one")
    fun signup(@Body requestModel: SignUp1.SignupRequestModel): Call<Void>
}
interface Signup2ApiService {
    @Headers("Content-Type: application/json")
    @POST("api/v1/users/sign_up_two")
    fun signup2(@Body requestModel: SignUp2.Signup2RequestModel): Call<SignUp2.SignUp2ResponseModel>
}
