package kushal.application.gym.Fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_member.view.*
import kushal.application.gym.Items.MemberItems
import kushal.application.gym.R
import kushal.application.gym.ViewHolders.Mem_viewHolder


class MemberFrag : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_member, container, false)
//        setVal()

        val ref = FirebaseDatabase.getInstance().reference.child("membership")

        val pd = ProgressDialog(context, R.style.ProgressDialog)
        pd.setMessage("Loading Please Wait !")
        pd.show()

        val options = FirebaseRecyclerOptions.Builder<MemberItems>()
            .setQuery(ref, MemberItems::class.java).build()

        val adapter = object : FirebaseRecyclerAdapter<MemberItems, Mem_viewHolder>(options) {

            override fun onBindViewHolder(holder: Mem_viewHolder, i: Int, model: MemberItems) {

                val node_id = getRef(i).key ?: return

                ref.child(node_id).addValueEventListener(object : ValueEventListener {
                    @SuppressLint("SetTextI18n")
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        pd.dismiss()

                        val name = dataSnapshot.child("name").value.toString()

                        val title = dataSnapshot.child("title").value.toString()
                        val desc = dataSnapshot.child("desc").value.toString()
                        val price = dataSnapshot.child("price").value.toString()
                        val duration = dataSnapshot.child("duration").value.toString()
                        val icon = dataSnapshot.child("star").value.toString()

                        if (icon == "1") {
                            holder.icon.setPadding(100, 100, 100, 100)
                        } else if (icon == "2") {
                            holder.icon.setImageDrawable(resources.getDrawable(R.drawable.plane2))
                        } else if (icon == "3") {
                            holder.icon.setImageDrawable(resources.getDrawable(R.drawable.plane3))
                        }

                        holder.title.text = name

                        holder.duration.text = "/$duration"
                        holder.desc.text = title
                        holder.desc2.text = desc
                        holder.price.text = "    $price"

                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e(
                            "TAG",
                            "onCancelled: Error"
                        )
                    }
                })
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Mem_viewHolder {
                val vieww =
                    LayoutInflater.from(context).inflate(R.layout.membership_box, parent, false)
                return Mem_viewHolder(vieww)
            }
        }

        view.member_recView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        view.member_recView.adapter = adapter
        adapter.startListening()


        return view
    }

    private fun setVal() {

        val ref = FirebaseDatabase.getInstance().reference.child("diet").child("cut")
        ref.child("name").setValue("Cutting Diet")
        ref.push().child("breakfast").setValue("Silver")
        ref.push().child("lunch").setValue("Thisjcsjs")
        ref.push().child("snack").setValue("Thisnnss")
        ref.push().child("dinner").setValue("hcnsnsn")

    }

}
