package kushal.application.sidebar

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var IS_SHORT = false

    val menuItemList by lazy {
        arrayListOf<TextView>(
            home_tv, membership_tv,
            perform_tv, exercise_tv,
            diet_tv, gallery_tv
        )
    }

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        fab.setOnClickListener {

            menu.visibility = View.VISIBLE

            layout
                .animate().translationX(400f)
                .translationY(0f)
                .scaleX(0.6f)
                .scaleY(0.6f)
                .duration = 1000

            window.statusBarColor = resources.getColor(R.color.backgroundDark)
            container.setBackgroundColor(resources.getColor(R.color.backgroundDark))

            fab.visibility = View.GONE
            IS_SHORT = true

        }
        settings.setOnClickListener {
            startActivity(Intent(this, Settings::class.java))
        }

        // color set up for menu
        setAllGray()
        home_tv.compoundDrawableTintList =
            resources.getColorStateList(R.color.white)
        home_tv.setTextColor(resources.getColor(R.color.white))

        setUpMenuItems()


//        FragmentManager()

    }

    private fun setUpMenuItems() {

        home_tv.setOnClickListener {
            colorChanges(it)
        }
        membership_tv.setOnClickListener {
            colorChanges(it)
            // further changes specific to membership plans
        }
        perform_tv.setOnClickListener {
            colorChanges(it)
        }
        exercise_tv.setOnClickListener {
            colorChanges(it)
        }
        diet_tv.setOnClickListener {
            colorChanges(it)
        }
        gallery_tv.setOnClickListener {
            colorChanges(it)
        }

    }

    private fun colorChanges(view: View) {
        setAllGray()
        val it = view as TextView
        it.compoundDrawableTintList =
            resources.getColorStateList(R.color.white)
        it.setTextColor(resources.getColor(R.color.white))

    }

    private fun setAllGray() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            menuItemList.forEach {
                it.compoundDrawableTintList =
                    resources.getColorStateList(R.color.gray)

                it.setTextColor(resources.getColor(R.color.gray))
            }

        }


    }

    @SuppressLint("RestrictedApi")
    override fun onBackPressed() {
        if (IS_SHORT) {
            layout.animate().translationX(0f)
                .translationY(0f)
                .scaleX(1f)
                .scaleY(1f)
                .duration = 800

            Handler().postDelayed({
                fab.visibility = View.VISIBLE
                menu.visibility = View.INVISIBLE
                window.statusBarColor = resources.getColor(R.color.background)
                container.setBackgroundColor(resources.getColor(R.color.background))
            }, 800)

            IS_SHORT = !IS_SHORT
        } else
            super.onBackPressed()
    }

}
