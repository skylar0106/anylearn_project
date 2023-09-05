import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ScriptsGetService {
    @GET("api/v1/scripts/")
    fun scriptsConnection(): Call<Void>
}


