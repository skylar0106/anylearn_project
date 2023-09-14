    import com.google.gson.annotations.SerializedName

data class ScriptListResponse(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("renders") val renders: List<String>,
    @SerializedName("parses") val parses: List<String>,
    @SerializedName("scripts") val scripts: List<Script> // 추가
)

data class Script(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    // 필요한 다른 속성들도 추가할 수 있습니다.
)
