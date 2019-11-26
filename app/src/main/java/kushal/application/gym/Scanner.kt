package kushal.application.gym

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.text.SimpleDateFormat
import java.util.*


@Suppress("SENSELESS_COMPARISON")
class Scanner : AppCompatActivity(), ZXingScannerView.ResultHandler {

    lateinit var scannerView: ZXingScannerView
    private val CHECKING_NAME = "Kushal"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scannerView = ZXingScannerView(this)
        setContentView(scannerView)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            scannerView.startCamera()
            scannerView.setResultHandler(this)
        } else {
            AlertDialog.Builder(this, R.style.AlertDialogCustom)
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

        if (myText.contains(CHECKING_NAME)) {
            setValue()
//            add custom dialog
            Toast.makeText(this, "Marked : )", Toast.LENGTH_SHORT).show()
        }
        else {
            AlertDialog.Builder(this, R.style.AlertDialogCustom)
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

    }

}
