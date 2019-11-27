package kushal.application.gym.Fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_perform.*
import kotlinx.android.synthetic.main.fragment_perform.view.*
import kushal.application.gym.R
import java.text.SimpleDateFormat
import java.util.*


class PerformFrag : Fragment() {

    private val auth = FirebaseAuth.getInstance().currentUser
    val monthFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    val smallDateFormat = SimpleDateFormat("MMM dd yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
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


        FirebaseDatabase.getInstance().reference.child("Users")
            .child(auth!!.uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                may be use a 'for loop' for children in dataSnapshot
                    if (dataSnapshot.hasChildren()) {
                        for (data in dataSnapshot.children) {

                            val str2 = data.child("date").value.toString()
                            val event2 = Event(
                                resources.getColor(R.color.green_pastel),
                                smallDateFormat.parse(str2).time
                            )
                            view.calendar_view.addEvent(event2)
                            perf_loading.visibility = LottieAnimationView.GONE
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("TAG", "onCancelled: Error")
                }
            })


        return view
    }


}
