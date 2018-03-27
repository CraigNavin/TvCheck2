package uk.ac.tees.p4061644.tvcheck_redo.Adapters

import android.content.Context
import android.media.Image
import android.support.v7.widget.RecyclerView
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.omertron.themoviedbapi.model.credits.MediaCreditCast
import com.squareup.picasso.Picasso
import uk.ac.tees.p4061644.tvcheck_redo.R

/**
 * Created by Craig on 27/03/2018.
 */
class RecyclerPeopleViewAdapter(private val context: Context, private val people: ArrayList<MediaCreditCast>): RecyclerView.Adapter<RecyclerPeopleViewAdapter.ViewHolder>() {
	override fun getItemCount(): Int {
		return people.size
	}

	override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
		var person = people[position]
		Picasso.with(context).load(context.getString(R.string.base_address_w500) + person.artworkPath)
				.placeholder(R.drawable.ic_default_search_image)
				.into(holder!!.imageView)
		holder.personName!!.text = person.name
	}

	override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
		var view: View = LayoutInflater.from(context).inflate(R.layout.recyclerview_item,parent,false)
		return ViewHolder(view)
	}

	class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
		var imageView: ImageView? = null
		var personName: TextView? = null

		init {
			imageView = view.findViewById(R.id.imageView) as ImageView?
			personName = view.findViewById(R.id.Name) as TextView?
		}
	}

}