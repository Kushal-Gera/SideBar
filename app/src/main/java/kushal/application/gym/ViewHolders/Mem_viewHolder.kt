package kushal.application.gym.ViewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kushal.application.gym.R

class Mem_viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var icon = itemView.findViewById<ImageView>(R.id.mem_icon)
    val title = itemView.findViewById<TextView>(R.id.mem_title)
    val desc = itemView.findViewById<TextView>(R.id.mem_desc)
    val price = itemView.findViewById<TextView>(R.id.mem_price)
    val desc2 = itemView.findViewById<TextView>(R.id.mem_desc2)
    val duration = itemView.findViewById<TextView>(R.id.mem_duration)

}