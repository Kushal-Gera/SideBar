package kushal.application.gym

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kushal.application.gym.Fragments.*

val SHARED_PREF = "shared_pref"
val USER_NAME = "name"
val USER_AGE = "age"

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private var IS_SHORT = false
    private var ON_HOME = true
    private val number = 8588910153

    val fManager by lazy {
        supportFragmentManager
    }

    private val menuItemList by lazy {
        arrayListOf<TextView>(
            home_tv, membership_tv,
            perform_tv, exercise_tv,
            diet_tv, gallery_tv
        )
    }

    val auth = FirebaseAuth.getInstance()


    @SuppressLint("RestrictedApi", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 101)
            }
        }

        val sharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
//        sharedPreferences.edit().putBoolean("is_prev", false).apply()

        if (!sharedPreferences.getBoolean("is_prev", false)) {
            startActivity(Intent(this, DetailsAct::class.java))
        }

        main_name.text = sharedPreferences.getString(USER_NAME, "User")
        main_age.text = sharedPreferences.getString(USER_AGE, "25") + " yrs"

        /**
         * Can be used with firebase recycler adapter to fetch list of number and name
         */
        FirebaseFirestore.getInstance().collection("Users").get()
            .addOnCompleteListener{
            for (i in it.result!!){
                val s = i.data["name"]
                Toast.makeText(this, "hello $s", Toast.LENGTH_SHORT).show()
            }
        }


        drawer.setOnClickListener {
            menu.visibility = View.VISIBLE
            layout
                .animate().translationX(350f)
                .translationY(0f)
                .scaleX(0.6f)
                .scaleY(0.6f)
                .duration = 1000

            window.statusBarColor = resources.getColor(R.color.backgroundDark)
            container.setBackgroundColor(resources.getColor(R.color.backgroundDark))

            drawer.visibility = View.INVISIBLE
            IS_SHORT = true

        }
        settings.setOnClickListener {
            startActivity(Intent(this, Settings::class.java))
        }
        // color set up for menu -> home
        setAllGray()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            home_tv.compoundDrawableTintList =
                resources.getColorStateList(R.color.white)
        }
        home_tv.setTextColor(resources.getColor(R.color.white))

        setUpMenuItems()
        fManager.beginTransaction().replace(R.id.layout, HomeFrag()).commit()


    }

    private fun setUpMenuItems() {

        home_tv.setOnClickListener {
            header_text.text = getString(R.string.home)
            colorToWhite(it)
            fManager.beginTransaction().replace(R.id.layout, HomeFrag()).commit()
            ON_HOME = true

            onBackPressed()
        }
        membership_tv.setOnClickListener {
            colorToWhite(it)
            header_text.text = getString(R.string.memberships)
            // further changes specific to membership plans
            fManager.beginTransaction().replace(R.id.layout, MemberFrag()).commit()
            ON_HOME = false

            onBackPressed()
        }
        perform_tv.setOnClickListener {
            colorToWhite(it)
            header_text.text = getString(R.string.performance)
            fManager.beginTransaction().replace(R.id.layout, PerformFrag()).commit()
            ON_HOME = false

            onBackPressed()
        }
        exercise_tv.setOnClickListener {
            colorToWhite(it)
            header_text.text = getString(R.string.exercises)
            fManager.beginTransaction().replace(R.id.layout, ExerciseFrag()).commit()
            ON_HOME = false

            onBackPressed()
        }
        diet_tv.setOnClickListener {
            colorToWhite(it)
            header_text.text = getString(R.string.diet_guide)
            fManager.beginTransaction().replace(R.id.layout, DietFrag()).commit()
            ON_HOME = false

            onBackPressed()
        }
        gallery_tv.setOnClickListener {
            colorToWhite(it)
            header_text.text = getString(R.string.gallery)
            fManager.beginTransaction().replace(R.id.layout, GalleryFrag()).commit()
            ON_HOME = false

            onBackPressed()
        }


        contact.setOnClickListener {
            //            onBackPressed()
            val builder = AlertDialog.Builder(this, R.style.AlertDialogGreen)
            builder.setTitle("Contact Us Here :")
                .setMessage("D-2 FF, Moti Nagar, New Delhi\n+91 $number")
                .setPositiveButton("WhatsApp") { dialogInterface: DialogInterface, pos: Int ->
                    //whatsApp
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse("https://wa.me/+91$number")
                    startActivity(i)
                }
                .setNegativeButton("Call") { dialogInterface, pos ->
                    //dont call, just take to dialer
                    val i = Intent(Intent.ACTION_DIAL)
                    i.data = Uri.parse("tel:$number")
                    startActivity(i)
                }
            builder.create().show()
        }
        logout.setOnClickListener {
            //            onBackPressed()
            val builder = AlertDialog.Builder(this, R.style.AlertDialogCustom)
            builder.setTitle("Do you really want to Logout ?")
                .setMessage("Don't worry! All progress will be saved")
                .setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
                    Toast.makeText(this, "Logging Out...", Toast.LENGTH_SHORT).show()
                    auth.signOut()
                    startActivity(Intent(this, LoginAct::class.java))
                    finish()
                }
                .setNegativeButton("No") { dialogInterface, i ->
                    //do nothing
                }
            builder.create().show()
        }

    }

    private fun colorToWhite(view: View) {
        setAllGray()
        val it = view as TextView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            it.compoundDrawableTintList =
                resources.getColorStateList(R.color.white)
        }
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
                .duration = 600

            Handler().postDelayed({
                drawer.visibility = View.VISIBLE
                menu.visibility = View.INVISIBLE
                window.statusBarColor = resources.getColor(R.color.background)
                container.setBackgroundColor(resources.getColor(R.color.background))
            }, 600)

            IS_SHORT = !IS_SHORT

        }
        else if (!ON_HOME) {
            fManager.beginTransaction().replace(R.id.layout, HomeFrag()).commit()
            header_text.text = getString(R.string.home)
            colorToWhite(home_tv)
            ON_HOME = !ON_HOME
        }
        else
            super.onBackPressed()

    }

}
