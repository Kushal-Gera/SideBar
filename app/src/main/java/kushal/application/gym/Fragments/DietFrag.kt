package kushal.application.gym.Fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_diet.*
import kotlinx.android.synthetic.main.fragment_diet.view.*
import kushal.application.gym.Items.MemberItems
import kushal.application.gym.R
import kushal.application.gym.ViewHolders.Diet_viewHolder

class DietFrag : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_diet, container, false)

        val ref = FirebaseDatabase.getInstance().reference.child("diet")

        val pd = ProgressDialog(context, R.style.ProgressDialog)
        pd.setMessage("Loading Please Wait !")
        pd.show()

        view.diet_anim.setOnClickListener {
            view.diet_anim.playAnimation()
        }

        val options = FirebaseRecyclerOptions.Builder<MemberItems>()
            .setQuery(ref, MemberItems::class.java).build()

        val adapter = object : FirebaseRecyclerAdapter<MemberItems, Diet_viewHolder>(options) {
            override fun onBindViewHolder(holder: Diet_viewHolder, i: Int, model: MemberItems) {

                val nodeId = getRef(i).key ?: return

                ref.child(nodeId).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        pd.dismiss()
                        view.diet_anim.playAnimation()

                        val url = dataSnapshot.child("logo").value.toString()
                        Glide.with(context!!).load(url)
                            .placeholder(resources.getDrawable(R.drawable.healthy))
                            .into(holder.logo)

                        val title = dataSnapshot.child("name").value.toString()
                        holder.title.text = title

                        holder.itemView.setOnClickListener {
                            //new activity or may be re-direct to PDF
                            Toast.makeText(context!!, "hey", Toast.LENGTH_SHORT).show()
                        }

                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e("TAG", "onCancelled: Error")
                    }
                })
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Diet_viewHolder {
                val vieww =
                    LayoutInflater.from(context).inflate(R.layout.diet_box, parent, false)
                return Diet_viewHolder(vieww)
            }
        }

        view.diet_recView.layoutManager = GridLayoutManager(context!!, 2)
        view.diet_recView.adapter = adapter
        adapter.startListening()

        return view
    }


}
