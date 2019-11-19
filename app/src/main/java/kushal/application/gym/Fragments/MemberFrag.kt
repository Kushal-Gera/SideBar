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

        val options = FirebaseRecyclerOptions.Builder<MemberItems>()
            .setQuery(ref, MemberItems::class.java).build()

        val pd = ProgressDialog(context, R.style.AlertDialogCustom)
        pd.setMessage("Loading Please Wait!")
        pd.show()

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
                            holder.icon.setPadding(30, 30, 30, 30)
                        } else if (icon == "2") {
                            holder.icon.setImageDrawable(resources.getDrawable(R.drawable.plane2))
                        } else if (icon == "3") {
                            holder.icon.setImageDrawable(resources.getDrawable(R.drawable.plane3))
                        }

                        holder.title.text = name

                        holder.duration.text = "/$duration"
                        holder.desc.text = title
                        holder.desc2.text = desc
                        holder.price.text = price

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
                val view =
                    LayoutInflater.from(context).inflate(R.layout.membership_box, parent, false)
                return Mem_viewHolder(view)
            }
        }

        view.member_recView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        view.member_recView.adapter = adapter
        adapter.startListening()


        return view
    }

    private fun setVal() {

        val ref = FirebaseDatabase.getInstance().reference.child("membership")
        ref.child("1silver").child("name").setValue("Silver")
        ref.child("1silver").child("title").setValue("This is the basic plan")
        ref.child("1silver").child("desc").setValue("This plan is monthly based and cost")
        ref.child("1silver").child("price").setValue(1000)
        ref.child("1silver").child("duration").setValue("Monthly")
        ref.child("1silver").child("star").setValue(1)

        ref.child("2gold").child("name").setValue("Gold")
        ref.child("2gold").child("title").setValue("This is the cost effective plan")
        ref.child("2gold").child("desc").setValue("This plan is great for regular gym visitors.")
        ref.child("2gold").child("price").setValue(2500)
        ref.child("2gold").child("duration").setValue("Quaterly")
        ref.child("2gold").child("star").setValue(2)

        ref.child("3platinum").child("name").setValue("Platinum")
        ref.child("3platinum").child("title").setValue("This is the yearly and most effective plan")
        ref.child("3platinum").child("desc")
            .setValue("This plan is the executive plan.\nPlan for the hardworkers")
        ref.child("3platinum").child("price").setValue(6000)
        ref.child("3platinum").child("duration").setValue("Yearly")
        ref.child("3platinum").child("star").setValue(3)

    }

}
