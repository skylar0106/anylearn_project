import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ScriptsApiService {
    //@GET("api/v1/check_connection/") // 실제 API 엔드포인트에 맞게 수정해야 함
    @Headers("Content-Type: application/json")
    @POST("api/v1/users/sign_in")
    fun checkConnection(@Body requestBody: Map<String, String>): Call<Void>
    fun checkConnection2(): Call<Void>
}

