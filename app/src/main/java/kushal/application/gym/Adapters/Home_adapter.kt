package kushal.application.gym.Adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kushal.application.gym.R
import kushal.application.gym.ViewHolders.Home_viewHolder


class Home_adapter(val list: ArrayList<String>, val c: Context) :
    RecyclerView.Adapter<Home_viewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Home_viewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.home_rec_view, parent, false)
        return Home_viewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: Home_viewHolder, position: Int) {
        val url = list[position]
        Glide.with(holder.image.context).load(Uri.parse(url)).centerCrop().into(holder.image)

        holder.itemView.setOnClickListener {
            val uri = Uri.parse("https://www.bodybuilding.com/fun/whats-new.html")
            c.startActivity(Intent(Intent.ACTION_VIEW, uri))
        }

    }


}