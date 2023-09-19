package ddwu.com.mobile.anylearn

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.request.RequestOptions

@GlideModule
class GlideModule : AppGlideModule(){
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        // 기본 로딩 옵션 설정 예시:
        builder.setDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.layout_profile)
                .error(R.drawable.ic_launcher_background)
        )
    }
}