package kushal.application.gym.WorkManagers

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kushal.application.gym.DateDatabase.DateData
import kushal.application.gym.DateDatabase.DateDatabase

class LinkDatabase(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {


    val database by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            DateDatabase::class.java,
            "dates.db"
        ).allowMainThreadQueries().build()
    }
    val auth = FirebaseAuth.getInstance().currentUser
    private val DAYS_TILL_SHOW = 31


    override fun doWork(): Result {

        FirebaseDatabase.getInstance().reference.child("Users")
            .child(auth!!.uid).addValueEventListener(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        var i = 0
                        for (data in dataSnapshot.children) {
                            if (i > DAYS_TILL_SHOW)
                                break
                            val str2 = data.child("date").value.toString()

                            //adding dates to database
                            val dateData = DateData()
                            dateData.date = str2
                            database.myDAO.updateDate(dateData)

                            i++
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("TAG", "onCancelled: Error")
                }
            })

        return Result.success()
    }

}