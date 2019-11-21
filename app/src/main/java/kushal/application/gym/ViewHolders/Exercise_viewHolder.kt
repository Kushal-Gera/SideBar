package kushal.application.gym.ViewHolders

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kushal.application.gym.R

class Exercise_viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var title = itemView.findViewById<TextView>(R.id.exercise_title)
    var logo = itemView.findViewById<ImageView>(R.id.exercise_logo)

}