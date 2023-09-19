package ddwu.com.mobile.anylearn

import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.format.TitleFormatter


class CustomTitleFormatter : TitleFormatter {
    override fun format(day: CalendarDay): CharSequence {
        val month = day.month + 1
        return "${month}월"
    }
}
