package kushal.application.gym

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.TimeUnit

class LoginAct : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()
    lateinit var VERIFICATION_ID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.backgroundDark, resources.newTheme())
        }

        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        progressBar3.visibility = View.INVISIBLE

        getCode.alpha = 0f;     getCode.translationY = 50f
        login_btn.alpha = 0f;   login_btn.translationY = 50f
        getCode.animate().alpha(1f).translationY(0f).duration = 800

        getCode.setOnClickListener {
            val number = phone.editText!!.text.toString().trim { it <= ' ' }
            if (!TextUtils.isEmpty(number) && number.length > 9) {

//                getVerificationCode("+91$number")

                progressBar3.visibility = View.VISIBLE
                login_btn.animate().alpha(1f).translationY(0f).duration = 800
                getCode.visibility = View.INVISIBLE
            } else {
                phone.editText!!.error = "Phone Number Required"
                phone.requestFocus()
            }
        }
        login_btn.setOnClickListener {
            val userCode = otp.editText!!.text.toString().trim { it <= ' ' }
            if (!TextUtils.isEmpty(userCode))
                verifyCode(userCode)
        }


    }

    private fun verifyCode(userCode: String?) {
        try {
            val credential = PhoneAuthProvider.getCredential(VERIFICATION_ID, userCode!!)
            signInWith(credential)
        } catch (e: Exception) {
            Toast.makeText(this, "Please Try Again\nOTP Might be Incorrect", Toast.LENGTH_LONG)
                .show()
            startActivity(Intent(this, LoginAct::class.java))
            finish()
            e.printStackTrace()
        }

    }

    private fun signInWith(credential: PhoneAuthCredential) {
        Toast.makeText(this, "Hold on, Just There...", Toast.LENGTH_SHORT).show()

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "ERROR OCCURRED",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }

    private fun getVerificationCode(phoneNumber: String) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            120,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            mCallbacks
        )
    }

    private val mCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                val userCode = credential.smsCode
                if (userCode != null) {
                    otp.editText!!.setText(userCode)
                    verifyCode(userCode)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@LoginAct, e.message, Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                VERIFICATION_ID = s
            }

        }


}