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

    private val auth = FirebaseAuth.getInstance().currentUser


    override fun doWork(): Result {

        if (auth != null)
            return linkdata()
        else
            return Result.success()

    }

    private fun linkdata(): Result {
        var r = Result.success()

        FirebaseDatabase.getInstance().reference.child("Users")
            .child(auth!!.uid).addValueEventListener(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.hasChildren()) {

                        for (data in dataSnapshot.children) {
                            val str2 = data.child("date").value.toString()

                            //adding dates to database
                            val dateData = DateData()
                            dateData.date = str2

                            database.myDAO.insertDate(dateData)

                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("TAG", "onCancelled: Error")
                    r = Result.retry()
                }
            })

        return r

    }

}