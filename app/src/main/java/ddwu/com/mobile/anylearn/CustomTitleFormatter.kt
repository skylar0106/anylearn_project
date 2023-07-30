package ddwu.com.mobile.anylearn

import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.format.TitleFormatter


class CustomTitleFormatter : TitleFormatter {
    override fun format(day: CalendarDay): CharSequence {
        // 날짜를 숨기기 위해 빈 문자열을 반환
        return ""
    }
}
