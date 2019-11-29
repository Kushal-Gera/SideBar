package kushal.application.gym

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_settings.*


val IS_ON = "is_on"

class Settings : AppCompatActivity() {
    val WEB_APP_LINK =
        "http://play.google.com/store/apps/details?id=" + "kushal.application.gym"
    val GMAIL_LINK = "kushalgera1212@gmail.com"

    val sharedPreferences by lazy {
        applicationContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        window.statusBarColor = resources.getColor(R.color.backgroundDark)

        setting_name.text = sharedPreferences.getString(USER_NAME, "User")
        setting_age.text = sharedPreferences.getString(USER_AGE, "22") + " yrs"
        var TURN_ON = sharedPreferences.getBoolean(IS_ON, false)
        setting_switch.isChecked = TURN_ON


        back.setOnClickListener {
            onBackPressed()
        }
        setting_dev.setOnClickListener {
            val sBar =
                Snackbar.make(it, "Developed by Kushal Gera: ", Snackbar.LENGTH_LONG)
            sBar.setAction("Git Hub") {
                startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Kushal-Gera"))
                )
            }.setActionTextColor(resources.getColor(R.color.green_pastel))

            val params = sBar.view.layoutParams as FrameLayout.LayoutParams
            params.setMargins(
                params.leftMargin + 30,
                params.topMargin,
                params.rightMargin + 30,
                params.bottomMargin + 50
            )

            sBar.view.layoutParams = params
            sBar.view.background = resources.getDrawable(R.drawable.bg_toolbar)

            sBar.show()
        }
        setting_share.setOnClickListener { shareIT() }
        setting_rate.setOnClickListener { rateUs() }
        setting_suggestions.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("mailto:$GMAIL_LINK")
            i.putExtra(
                Intent.EXTRA_SUBJECT,
                "Suggestion for the App '${resources.getString(R.string.app_name)}'"
            )
            i.putExtra(Intent.EXTRA_TEXT, "I have a suggestion that: ")
            startActivity(i)
        }
        setting_edit.setOnClickListener {
            val i = Intent(this, DetailsAct::class.java)
            i.putExtra("allowed_back", true)
            startActivity(i)
        }
        setting_switch.setOnClickListener {
            val view = it as Switch
            TURN_ON = !TURN_ON
            view.isChecked = TURN_ON
            sharedPreferences.edit().putBoolean(IS_ON, TURN_ON).apply()
        }


    }

    private fun shareIT() {
        val i = Intent(Intent.ACTION_SEND)
        i.type = "text/plain"
        i.putExtra(Intent.EXTRA_TEXT, "${resources.getString(R.string.app_name)}\n\n$WEB_APP_LINK")
        startActivity(Intent.createChooser(i, "Share Via"))
    }

    private fun rateUs() { //        open playStore if present otherwise go to chrome
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$packageName")
                )
            )
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(WEB_APP_LINK)))
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        setting_name.text = sharedPreferences.getString(USER_NAME, "User")
        setting_age.text = sharedPreferences.getString(USER_AGE, "22") + " yrs"
    }

}
