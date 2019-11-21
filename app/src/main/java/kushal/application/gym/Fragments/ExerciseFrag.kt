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
import kotlinx.android.synthetic.main.fragment_exercise.view.*
import kushal.application.gym.Items.MemberItems
import kushal.application.gym.R
import kushal.application.gym.ViewHolders.Exercise_viewHolder

class ExerciseFrag : Fragment() {

    var IS_DOWN = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exercise, container, false)

        val ref = FirebaseDatabase.getInstance().reference.child("exercise")

        val pd = ProgressDialog(context, R.style.ProgressDialog)
        pd.setMessage("Loading Please Wait !")
        pd.show()

        view.exercise_anim.setOnClickListener {
            view.exercise_anim.visibility = View.VISIBLE
            view.exercise_anim.playAnimation()
        }

        view.exercise_drop_down.setOnClickListener {
            if (IS_DOWN) {
                view.exercise_desc.animate().alpha(1f).translationY(0f).duration = 400
                view.exercise_desc.visibility = View.VISIBLE
                view.exercise_drop_down.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.up,
                    0
                )
            } else {
                view.exercise_desc.animate().translationY(-30f).alpha(0f).duration = 400
                view.exercise_desc.visibility = View.GONE
                view.exercise_drop_down.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.down,
                    0
                )
            }
            IS_DOWN = !IS_DOWN

        }

        val options = FirebaseRecyclerOptions.Builder<MemberItems>()
            .setQuery(ref, MemberItems::class.java).build()

        val adapter = object : FirebaseRecyclerAdapter<MemberItems, Exercise_viewHolder>(options) {
            override fun onBindViewHolder(holder: Exercise_viewHolder, i: Int, model: MemberItems) {

                val nodeId = getRef(i).key ?: return

                ref.child(nodeId).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        pd.dismiss()
                        view.exercise_anim.visibility = View.VISIBLE
                        view.exercise_anim.playAnimation()

                        val url = dataSnapshot.child("logo").value.toString()
                        Glide.with(context!!).load(url)
                            .placeholder(resources.getDrawable(R.drawable.weight_loss))
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

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Exercise_viewHolder {
                val vieww =
                    LayoutInflater.from(context).inflate(R.layout.exercise_box, parent, false)
                return Exercise_viewHolder(vieww)
            }
        }

        view.exercise_recView.layoutManager = GridLayoutManager(context!!, 2)
        view.exercise_recView.adapter = adapter
        adapter.startListening()

        return view
    }


}
