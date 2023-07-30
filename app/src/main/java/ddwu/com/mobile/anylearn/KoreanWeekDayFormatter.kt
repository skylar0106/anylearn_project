package ddwu.com.mobile.anylearn

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.format.WeekDayFormatter


class KoreanWeekDayFormatter : WeekDayFormatter {
    override fun format(dayOfWeek: Int): CharSequence {
        // 요일에 따라 색상을 설정합니다.
        val weekDayStr: String
        weekDayStr = when (dayOfWeek) {
            1 -> "일"
            2 -> "월"
            3 -> "화"
            4 -> "수"
            5 -> "목"
            6 -> "금"
            7 -> "토"
            else -> ""
        }

        // '일'은 빨간색으로, '토'는 파란색으로 설정한 SpannableString을 반환합니다.
        val spannableString = SpannableString(weekDayStr)
        if (dayOfWeek == 1) {
            spannableString.setSpan(
                ForegroundColorSpan(Color.RED),
                0,
                weekDayStr.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } else if (dayOfWeek == 7) {
            spannableString.setSpan(
                ForegroundColorSpan(Color.BLUE),
                0,
                weekDayStr.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return spannableString
    }
}

