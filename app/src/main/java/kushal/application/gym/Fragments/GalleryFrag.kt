package kushal.application.gym.Fragments


import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_gallery.view.*
import kushal.application.gym.Items.MemberItems
import kushal.application.gym.R
import kushal.application.gym.ViewHolders.Gal_viewHolder

class GalleryFrag : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_gallery, container, false)

        val ref = FirebaseDatabase.getInstance().reference
            .child("gallery").child("images")

        val pd = ProgressDialog(context, R.style.ProgressDialog)
        pd.setMessage("Loading Please Wait !")
        pd.show()

        val options = FirebaseRecyclerOptions.Builder<MemberItems>()
            .setQuery(ref, MemberItems::class.java).build()

        val adapter = object : FirebaseRecyclerAdapter<MemberItems, Gal_viewHolder>(options) {
            override fun onBindViewHolder(holder: Gal_viewHolder, i: Int, model: MemberItems) {

                val node_id = getRef(i).key ?: return

                ref.child(node_id).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        pd.dismiss()

                        val url = dataSnapshot.child("img").value.toString()
                        Glide.with(holder.img.context).load(url).dontAnimate().into(holder.img)

                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e("TAG", "onCancelled: Error")
                    }
                })
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Gal_viewHolder {
                val vieww =
                    LayoutInflater.from(context).inflate(R.layout.gal_box, parent, false)
                return Gal_viewHolder(vieww)
            }
        }

        view.gal_recView.layoutManager = LinearLayoutManager(context)
        view.gal_recView.adapter = adapter
        adapter.startListening()


        return view
    }


}
