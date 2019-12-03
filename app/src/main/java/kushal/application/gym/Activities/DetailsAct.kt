package kushal.application.gym.Activities

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_details.*
import kushal.application.gym.R

class DetailsAct : AppCompatActivity() {

    var IS_MALE = true

    val auth = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        window.statusBarColor = resources.getColor(R.color.backgroundDark)


        save.setOnClickListener(View.OnClickListener {
            if (TextUtils.isEmpty(name?.editText!!.text)) {
                name?.error = "Required"
                return@OnClickListener
            }
            if (TextUtils.isEmpty(age.editText?.text)) {
                age.error = "Required"
                return@OnClickListener
            }
            if (TextUtils.isEmpty(weight.editText?.text)) {
                weight.error = "Required"
                return@OnClickListener
            }
            if (TextUtils.isEmpty(height.editText?.text)) {
                height.error = "Required"
                return@OnClickListener
            }

            saveData()

            Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show()
            finish()
        })
        female.setOnClickListener {
            female.background = resources.getDrawable(R.drawable.save_now_bg)
            male?.background = resources.getDrawable(R.drawable.save_now_bg_2)
            IS_MALE = false
            female.setTextColor(Color.WHITE)
            male?.setTextColor(Color.WHITE)
        }
        male.setOnClickListener {
            male?.background = resources.getDrawable(R.drawable.save_now_bg)
            female.background = resources.getDrawable(R.drawable.save_now_bg_2)
            IS_MALE = true
            male?.setTextColor(Color.WHITE)
            female.setTextColor(Color.WHITE)
        }

    }

    private fun saveData() {
        val shared_pref = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val editor = shared_pref.edit()


        //offline data saved
        editor.putString("name", name.editText?.text.toString()).apply()
        editor.putString("age", age.editText?.text.toString()).apply()
        editor.putString("weight", weight.editText?.text.toString()).apply()
        editor.putString("height", height.editText?.text.toString()).apply()
        if (IS_MALE)
            editor.putString("gender", "male").apply()
        else
            editor.putString("gender", "female").apply()


        //online data saving
        val db = FirebaseFirestore.getInstance()
        val user = hashMapOf(
            "name" to shared_pref!!.getString(
                USER_NAME, "u" + System.currentTimeMillis()).toString(),
            "number" to auth!!.phoneNumber.toString()
        )


        db.collection("Users").document(auth.uid).set(user as Map<String, Any>)
            .addOnSuccessListener {
                editor.putBoolean("is_prev", true).apply()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Internet Might Not Be Available", Toast.LENGTH_SHORT).show()
            }

    }

    override fun onBackPressed() {
        if (intent.getBooleanExtra("allowed_back", false))
            super.onBackPressed()
    }

}
