package kushal.application.gym.Activities

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.zxing.Result
import kotlinx.android.synthetic.main.mark_dialog.*
import kushal.application.gym.R
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.text.SimpleDateFormat
import java.util.*


@Suppress("SENSELESS_COMPARISON")
class Scanner : AppCompatActivity(), ZXingScannerView.ResultHandler {

    lateinit var scannerView: ZXingScannerView
    private val CHECKING_NAME = "Kushal"
    private val sharedPreferences by lazy {
        getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (sharedPreferences.getBoolean(IS_THEME_DARK, true))
            setTheme(R.style.MyDarkTheme)
        else
            setTheme(R.style.AppTheme)
        scannerView = ZXingScannerView(this)
        setContentView(scannerView)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        requestPermissions(arrayOf(Manifest.permission.CAMERA), 101)


//        /checking for internet
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        if (!(activeNetworkInfo != null && activeNetworkInfo.isConnected)) {
            AlertDialog.Builder(this, R.style.AlertDialogCustom)
                .setTitle("Internet May Not Be Available")
                .setMessage("Please retry !")
                .setPositiveButton("Retry") { dialogInterface: DialogInterface, i: Int ->
                    startActivity(Intent(this, Scanner::class.java))
                    finish()
                }
                .setNegativeButton("Return") { dialogInterface: DialogInterface, i: Int ->
                    finish()
                }
                .setCancelable(false)
                .create().show()
        }


    }

    private fun showDialog() {
        AlertDialog.Builder(
            this,
            R.style.AlertDialogCustom
        )
            .setTitle("Permissions are not Granted")
            .setPositiveButton("Grant Now") { dialogInterface: DialogInterface, i: Int ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", packageName, null)
                startActivity(intent)
            }
            .setNegativeButton("Return") { dialogInterface: DialogInterface, i: Int ->
                finish()
            }
            .setCancelable(false)
            .create().show()
    }

    override fun onResume() {
        super.onResume()

        if (scannerView == null) {
            scannerView = ZXingScannerView(this)
            setContentView(scannerView)
        }
        scannerView.setResultHandler(this)
        scannerView.startCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        scannerView.stopCamera()
    }

    override fun handleResult(result: Result) {
        val myText = result.text

        if (myText.contains(CHECKING_NAME))
            setValue()
        else {
            AlertDialog.Builder(
                this,
                R.style.AlertDialogCustom
            )
                .setTitle("Not the right QR Code")
                .setMessage("Please retry !")
                .setNegativeButton("Return") { dialogInterface: DialogInterface, i: Int ->
                    finish()
                }
                .setPositiveButton("Retry") { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                    scannerView.resumeCameraPreview(this)
                }
                .setCancelable(false)
                .create().show()
        }

    }

    private fun setValue() {
        val auth = FirebaseAuth.getInstance().currentUser
        val smallDateFormat = SimpleDateFormat("MMM dd yyyy", Locale.getDefault())

        val date = smallDateFormat.format(Calendar.getInstance().time)

        FirebaseDatabase.getInstance().reference.child("Users")
            .child(auth!!.uid)
            .push().child("date").setValue(date)
            .addOnSuccessListener {
                val dialog = Dialog(this)
                dialog.setContentView(R.layout.mark_dialog)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()

                dialog.dialog_return.setOnClickListener {
                    dialog.dismiss()
                    finish()
                }
            }


    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.isNotEmpty()) {
            scannerView.startCamera()
            scannerView.setResultHandler(this)
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                //denied
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 101)
            } else {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    //allowed
                    onResume()
                } else {
                    //set to never ask again
                    showDialog()
                }
            }
        }
    }


}
