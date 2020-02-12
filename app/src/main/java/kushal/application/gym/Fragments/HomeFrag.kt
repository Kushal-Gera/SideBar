package kushal.application.gym.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_main.view.*
import kushal.application.gym.Activities.PAID
import kushal.application.gym.Activities.SHARED_PREF
import kushal.application.gym.Activities.Scanner
import kushal.application.gym.Adapters.CirclePagerIndicatorDecoration
import kushal.application.gym.Adapters.Home_adapter
import kushal.application.gym.R


class HomeFrag : Fragment() {

    var IS_DOWN = true
    val sharedPreferences by lazy {
        activity!!.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_main, container, false)
        view.desc.animate().translationY(-30f)
        view.desc.visibility = View.GONE


        view.drop_down.setOnClickListener {
            if (IS_DOWN) {
                view.desc.animate().alpha(1f).translationY(0f).duration = 400
                view.desc.visibility = View.VISIBLE
                view.drop_down.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up, 0)
            } else {
                view.desc.animate().translationY(-30f).alpha(0f).duration = 400
                view.desc.visibility = View.GONE
                view.drop_down.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down, 0)
            }
            IS_DOWN = !IS_DOWN

        }
        view.fab.setOnClickListener {
            it.animate().rotationBy(360f).duration = 800
            //do work here
            startActivity(Intent(activity, Scanner::class.java))
        }


        val list =
            arrayListOf("https://firebasestorage.googleapis.com/v0/b/gymapp-b70b9.appspot.com/o/2.jpg?alt=media&token=333a646e-2ee7-40bd-b63c-20e146549c13")
        val adapter = Home_adapter(list, context!!)
        val ref = FirebaseStorage.getInstance().reference

        ref.child("4.jpg").downloadUrl.addOnCompleteListener {
            it.addOnSuccessListener {
                list.add(it.toString())
                adapter.notifyDataSetChanged()
            }
        }
        ref.child("3.jpg").downloadUrl.addOnCompleteListener {
            it.addOnSuccessListener {
                list.add(it.toString())
                adapter.notifyDataSetChanged()
            }
        }
        ref.child("5.jpg").downloadUrl.addOnCompleteListener {
            it.addOnSuccessListener {
                list.add(it.toString())
                adapter.notifyDataSetChanged()
            }
        }
        ref.child("1.jpeg").downloadUrl.addOnCompleteListener {
            it.addOnSuccessListener {
                list.add(it.toString())
                adapter.notifyDataSetChanged()
            }
        }

        view.home_recView.adapter = adapter
        view.home_recView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        view.home_recView.addItemDecoration(CirclePagerIndicatorDecoration())


        if (sharedPreferences.getBoolean(PAID, false)) {
            view.paid.visibility = View.VISIBLE
            view.due.visibility = View.GONE
        }


        return view
    }


}
