package kushal.application.gym.WorkManagers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RemoveDates(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    val DAYS_TILL_SAVING = 31
    val auth = FirebaseAuth.getInstance().currentUser


    override fun doWork(): Result {

        if (auth != null)
            return removeValues()
        else
            return Result.success()

    }

    private fun removeValues(): Result {
        var r: Result = Result.success()

        FirebaseDatabase.getInstance().reference.child("Users").child(auth!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val data = dataSnapshot.children.toMutableList()
                    if (data.size > DAYS_TILL_SAVING) {
                        var i = data.size - DAYS_TILL_SAVING
                        while (i >= 0) {
                            data[i].ref.removeValue()
                            i--
                        }
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    Log.i("TAG", "ERROR")
                    r = Result.retry()
                }
            })

        return r
    }


}