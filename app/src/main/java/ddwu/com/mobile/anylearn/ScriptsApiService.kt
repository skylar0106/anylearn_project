import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ScriptsApiService {
    @Headers("Content-Type: application/json")
    @POST("api/v1/users/sign_in")
    fun checkConnection(@Body requestBody: Map<String, String>): Call<Void>

}


