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
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView


class Scanner : AppCompatActivity(), ZXingScannerView.ResultHandler {

    lateinit var scannerView: ZXingScannerView

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
        }
        else {
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
        val myResult = result.text
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Scan Result")
        builder.setPositiveButton(
            "OK"
        ) { dialog, which -> scannerView.resumeCameraPreview(this) }
        builder.setNeutralButton(
            "Visit"
        ) { dialog, which ->
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(myResult))
            startActivity(browserIntent)
        }
        builder.setMessage(result.text)
        val alert1 = builder.create()
        alert1.show()
    }


}
