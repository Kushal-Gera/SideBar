package kushal.application.gym.ViewHolders

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kushal.application.gym.R

class Diet_viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var title = itemView.findViewById<TextView>(R.id.diet_title)
    var logo = itemView.findViewById<ImageView>(R.id.diet_logo)

}