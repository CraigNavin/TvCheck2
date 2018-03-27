package uk.ac.tees.p4061644.tvcheck_redo.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.omertron.themoviedbapi.model.tv.TVBasic
import com.squareup.picasso.Picasso
import uk.ac.tees.p4061644.tvcheck_redo.R

/**
 * Created by Craig on 21/03/2018.
 */
class RecyclerHomeViewAdapter(private val context: Context, private val shows: ArrayList<TVBasic>, private val listener: OnItemClickListener): RecyclerView.Adapter<RecyclerHomeViewAdapter.ViewHolder>() {

	interface OnItemClickListener {
		fun onItemClick(position: Int)
	}

	override fun getItemCount(): Int {
		return shows.size
	}

	override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
		var view: View = LayoutInflater.from(context).inflate(R.layout.recyclerview_item,parent,false)
		return ViewHolder(view)
	}

	override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
		var show = shows!![position]
		holder!!.itemView.setOnClickListener { View.OnClickListener { listener.onItemClick(position) } }
		holder!!.bind(show,listener)
	}

	class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
		var imgView: ImageView? = null
		var showName: TextView? = null
		init{
			imgView = view.findViewById(R.id.imageView) as ImageView?
			showName = view.findViewById(R.id.Name) as TextView?
		}

		fun bind(show:TVBasic, listener: OnItemClickListener){
			Picasso.with(itemView.context).load(itemView.context.getString(R.string.base_address_w500) + show.posterPath)
					.placeholder(R.drawable.ic_default_search_image)
					.into(imgView)
			showName!!.text = show.name
		}

	}
}