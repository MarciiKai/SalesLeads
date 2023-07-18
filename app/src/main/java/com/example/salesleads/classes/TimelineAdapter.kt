import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.salesleads.R
import com.example.salesleads.classes.UserData

class TimelineAdapter(
    private val context: Context,
    private val items: List<UserData>,
    private val onItemClick: ((UserData) -> Unit)?
) : RecyclerView.Adapter<TimelineAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.timeline_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder?.bind(item)

        // Set click listener for the "Rate" button
        holder.btnRate.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtFirstName: TextView = itemView.findViewById(R.id.frstNane)
        private val txtLastName: TextView = itemView.findViewById(R.id.lstName)
        val btnRate: Button = itemView.findViewById(R.id.ratingBar)

        fun bind(item: UserData) {
            txtFirstName.text = item.firstname
            txtLastName.text = item.lastname
        }
    }
}
