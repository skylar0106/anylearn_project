import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface ScriptsApiService {
    @GET("api/v1/check_connection/") // 실제 API 엔드포인트에 맞게 수정해야 함
    @POST("api/v1/users/sign_in/")
    fun checkConnection(): Call<Void>
}

