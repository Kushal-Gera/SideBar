package kushal.application.gym.Fragments


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_perform.view.*
import kushal.application.gym.Activities.MainActivity
import kushal.application.gym.Activities.SHARED_PREF
import kushal.application.gym.DateDatabase.DateDatabase
import kushal.application.gym.R
import java.text.SimpleDateFormat
import java.util.*


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PerformFrag : Fragment() {

    val monthFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    private val smallDateFormat = SimpleDateFormat("MMM dd yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_perform, container, false)

        view.calendar_view.setLocale(TimeZone.getDefault(), Locale.getDefault())
        view.calendar_view.setUseThreeLetterAbbreviation(true)
        view.month.text = monthFormat.format(view.calendar_view.firstDayOfCurrentMonth)
        view.calendar_view.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date?) {
            }

            override fun onMonthScroll(month: Date) {
                view.month.text = monthFormat.format(month)
            }
        })


        val database =
            Room.databaseBuilder(
                activity!!.applicationContext,
                DateDatabase::class.java,
                "dates.db"
            ).allowMainThreadQueries().build()

        if (!database.myDAO.readDates().isNullOrEmpty()) {
            for (data in database.myDAO.readDates()) {
                val str2 = data.date
                val event2 = Event(
                    resources.getColor(R.color.green_pastel),
                    smallDateFormat.parse(str2).time
                )
                view.calendar_view.addEvent(event2)
            }
            view.perf_loading.visibility = View.GONE
        }
        else
            view.perf_loading.visibility = View.GONE

        /**
         * //change this to false
         * currently already false!! hahah
         */
        context!!.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE).edit()
            .putBoolean("perf_tv_dot", false).apply()
        activity!!.perf_tv_dot.visibility = View.GONE



        return view
    }

}
