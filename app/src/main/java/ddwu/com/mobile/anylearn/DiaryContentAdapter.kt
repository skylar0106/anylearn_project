package ddwu.com.mobile.anylearn

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DiaryContentAdapter(var diaryContents: List<MyDiaryMain.DiaryContent>)
    : RecyclerView.Adapter<DiaryContentAdapter.DiaryContentViewHolder>() {
    // 뷰 홀더 클래스 정의
    class DiaryContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contentsTextView: TextView = itemView.findViewById(R.id.contentsTextView)
        val hashtagTextView: TextView = itemView.findViewById(R.id.hashtagTextView)
    }

    // onCreateViewHolder: 새로운 뷰 홀더 객체 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryContentViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.diary_script_layout, parent, false)
        return DiaryContentViewHolder(itemView)
    }

    // onBindViewHolder: 뷰 홀더에 데이터 바인딩
    override fun onBindViewHolder(holder: DiaryContentViewHolder, position: Int) {
        val currentItem = diaryContents[position]

        // 데이터를 뷰 홀더의 뷰에 설정
        holder.contentsTextView.text = currentItem.contents
        if(currentItem.hashtag != null)
            holder.hashtagTextView.text =
                "Hashtags: ${currentItem.hashtag?.joinToString(", ") { it.tag }}"
        else
            holder.hashtagTextView.text =
                "Hashtags: "
    }

    // 다이어리 내용 갱신 메서드
    fun updateDiaryContents(newDiaryContentsList: List<MyDiaryMain.DiaryContent>) {
        diaryContents = newDiaryContentsList
        notifyDataSetChanged()
    }

    // getItemCount: 데이터 아이템 수 반환
    override fun getItemCount() = diaryContents.size
}
