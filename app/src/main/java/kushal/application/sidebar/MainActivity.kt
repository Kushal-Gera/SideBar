package kushal.application.sidebar

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat.animate
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var IS_SHORT = false

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

        img.setOnClickListener {
            Toast.makeText(this, "hey", Toast.LENGTH_SHORT).show()
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
