package kushal.application.sidebar.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.view.*
import kushal.application.sidebar.R


class HomeFrag : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_main, container, false)

        view.img.setOnClickListener {
            Toast.makeText(activity, "hey", Toast.LENGTH_SHORT).show()
        }

        return view
    }


}
