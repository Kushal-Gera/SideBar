package kushal.application.gym.Fragments


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_perform.view.*
import kushal.application.gym.R
import java.text.SimpleDateFormat
import java.util.*


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PerformFrag : Fragment() {

    val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    val smallDateFormat = SimpleDateFormat("MMM dd yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_perform, container, false)


        view.calendar_view.setLocale(TimeZone.getDefault(), Locale.getDefault())
        view.calendar_view.setUseThreeLetterAbbreviation(true)
        view.month.text = dateFormat.format(view.calendar_view.firstDayOfCurrentMonth)
//        setValue()

        //Epoch time into long format
        val str = "NOV 13 2019"

        val event = Event(resources.getColor(R.color.green_pastel), smallDateFormat.parse(str).time)
        view.calendar_view.addEvent(event)
        view.calendar_view.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date?) {

            }

            override fun onMonthScroll(month: Date) {
                view.month.text = dateFormat.format(month)
            }

        })


        val ref = FirebaseDatabase.getInstance().reference.child("Users")

        ref.child("Kushal Gera").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val str = dataSnapshot.child("date").value.toString()
                val event = Event(
                    resources.getColor(R.color.green_pastel),
                    smallDateFormat.parse(str).time
                )
                Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
                view.calendar_view.addEvent(event)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("TAG", "onCancelled: Error")
            }
        })


        return view
    }

    private fun setValue() {
        val shared_pref = context?.getSharedPreferences("shared_pref", Context.MODE_PRIVATE)
        val date = smallDateFormat.format(Calendar.getInstance().time)

        val ref = FirebaseDatabase.getInstance().reference.child("Users")

        ref.child(shared_pref!!.getString("name", "u" + System.currentTimeMillis()).toString())
            .push().child("date").setValue(date)

    }


}
