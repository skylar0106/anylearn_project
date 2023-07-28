package ddwu.com.mobile.anylearn

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import ddwu.com.mobile.anylearn.databinding.ActivityMyDiaryMainBinding
import java.util.*

class MyDiaryMain : AppCompatActivity() {

    lateinit var mdmBinding: ActivityMyDiaryMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mdmBinding = ActivityMyDiaryMainBinding.inflate(layoutInflater)
        setContentView(mdmBinding.root)

        val sundayDecorator = SundayDecorator()
        val saturdayDecorator = SaturdayDecorator()
        val todayDecorator = TodayDecorator(this)
        val weekdayDecorator = WeekdayDecorator()

        mdmBinding.calendarView.addDecorators(sundayDecorator, saturdayDecorator, todayDecorator, weekdayDecorator)
        mdmBinding.calendarView.setTopbarVisible(false)

    }
    inner class TodayDecorator(context: Context): DayViewDecorator {
        private var date = CalendarDay.today()
        val drawable = ContextCompat.getDrawable(context, R.drawable.style_only_radius_10)
        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return day?.equals(date)!!
        }
        override fun decorate(view: DayViewFacade?) {
            if (drawable != null) {
                view?.setBackgroundDrawable(drawable)
            }
        }
    }

    inner class SundayDecorator:DayViewDecorator {
        private val calendar = Calendar.getInstance()
        override fun shouldDecorate(day: CalendarDay?): Boolean {
            day?.copyTo(calendar)
            val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
            return weekDay == Calendar.SUNDAY
        }
        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(object: ForegroundColorSpan(Color.RED){})
        }
    }

    inner class SaturdayDecorator:DayViewDecorator {
        private val calendar = Calendar.getInstance()
        override fun shouldDecorate(day: CalendarDay?): Boolean {
            day?.copyTo(calendar)
            val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
            return weekDay == Calendar.SATURDAY
        }
        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(object: ForegroundColorSpan(Color.BLUE){})
        }
    }

    inner class WeekdayDecorator:DayViewDecorator {
        private val calendar = Calendar.getInstance()
        override fun shouldDecorate(day: CalendarDay?): Boolean {
            day?.copyTo(calendar)
            val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
            return weekDay in Calendar.MONDAY..Calendar.FRIDAY
        }
        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(ForegroundColorSpan(Color.WHITE))
        }
    }
}





