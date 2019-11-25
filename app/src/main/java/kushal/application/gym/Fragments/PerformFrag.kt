package kushal.application.gym.Fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import kotlinx.android.synthetic.main.fragment_perform.view.*
import kushal.application.gym.R
import java.text.SimpleDateFormat
import java.util.*


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PerformFrag : Fragment() {

    val dateFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_perform, container, false)


        view.calendar_view.setLocale(TimeZone.getDefault(), Locale.getDefault())
        view.calendar_view.setUseThreeLetterAbbreviation(true)
        view.month.text = dateFormat.format(view.calendar_view.firstDayOfCurrentMonth)

        //Epoch time into long format
        val str = "NOV 13 2019"
        val smallDateFormat = SimpleDateFormat("MMM dd yyyy", Locale.getDefault())

        val event = Event(
            resources.getColor(R.color.green_pastel), smallDateFormat.parse(str).time, "something"
        )
        view.calendar_view.addEvent(event)


        view.calendar_view.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date?) {

            }

            override fun onMonthScroll(month: Date) {
                view.month.text = dateFormat.format(month)
            }

        })

        return view
    }


}
