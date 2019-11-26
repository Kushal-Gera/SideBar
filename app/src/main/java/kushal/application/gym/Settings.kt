package kushal.application.gym

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*


class Settings : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        window.statusBarColor = resources.getColor(R.color.backgroundDark)

        back.setOnClickListener {
            onBackPressed()
        }

        val sharedPreferences = baseContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)

        setting_name.text = sharedPreferences.getString(USER_NAME, "User")
        setting_age.text = sharedPreferences.getString(USER_AGE, "22") + " yrs"

    }
}
