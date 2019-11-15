package kushal.application.sidebar.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.view.*
import kushal.application.sidebar.LoginAct
import kushal.application.sidebar.R


class HomeFrag : Fragment() {

    var IS_DOWN = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_main, container, false)
        view.desc.animate().translationY(-100f)
        view.desc.visibility = View.GONE

        view.drop_down.setOnClickListener {
            if (IS_DOWN) {
                view.desc.animate().alpha(1f).translationY(0f).duration = 800
                view.desc.visibility = View.VISIBLE
                view.drop_down.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up, 0)
            } else {
                view.desc.animate().translationY(-100f).alpha(0f).duration = 800
                view.desc.visibility = View.GONE
                view.drop_down.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down, 0)
            }
            IS_DOWN = !IS_DOWN

        }

        view.img.setOnClickListener {
            Toast.makeText(activity, "hey", Toast.LENGTH_SHORT).show()
        }
        view.fab.setOnClickListener {
            it.animate().rotationBy(360f).duration = 800
            //do work here
            startActivity(Intent(activity, LoginAct::class.java))
        }

        return view
    }


}
