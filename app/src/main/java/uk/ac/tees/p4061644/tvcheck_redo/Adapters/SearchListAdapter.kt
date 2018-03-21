package uk.ac.tees.p4061644.tvcheck_redo.Adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import com.omertron.themoviedbapi.model.tv.TVBasic
import com.squareup.picasso.Picasso
import uk.ac.tees.p4061644.tvcheck_redo.R

/**
 * Created by Craig on 05/03/2018.
 */
class SearchListAdapter(private var activity: android.app.Activity, private var results: ArrayList<com.omertron.themoviedbapi.model.tv.TVBasic>?, private var context: android.content.Context): android.widget.BaseAdapter(){

	class ViewHolder(row: android.view.View?){
		var txtName: android.widget.TextView? = null
		var txtComment: android.widget.TextView? = null
		var imgView: android.widget.ImageView? = null


		init {
			this.txtName = row?.findViewById(uk.ac.tees.p4061644.tvcheck_redo.R.id.txtName) as android.widget.TextView?
			this.txtComment = row?.findViewById(uk.ac.tees.p4061644.tvcheck_redo.R.id.txtComment) as android.widget.TextView?
			this.imgView = row?.findViewById(uk.ac.tees.p4061644.tvcheck_redo.R.id.img_view) as android.widget.ImageView
		}
	}

	override fun getView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup?): android.view.View {
		val view: android.view.View?
		val viewHolder: uk.ac.tees.p4061644.tvcheck_redo.Adapters.SearchListAdapter.ViewHolder

		if (convertView == null){
			val inflater = activity.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE) as android.view.LayoutInflater
			view = inflater.inflate(uk.ac.tees.p4061644.tvcheck_redo.R.layout.search_item_layout,null)
			viewHolder = uk.ac.tees.p4061644.tvcheck_redo.Adapters.SearchListAdapter.ViewHolder(view)
			view!!.tag = viewHolder
		} else{
			view = convertView
			viewHolder = view.tag as uk.ac.tees.p4061644.tvcheck_redo.Adapters.SearchListAdapter.ViewHolder
		}

		handleResults(viewHolder,position)

		return view
	}

	fun handleResults(holder: uk.ac.tees.p4061644.tvcheck_redo.Adapters.SearchListAdapter.ViewHolder, position: Int): uk.ac.tees.p4061644.tvcheck_redo.Adapters.SearchListAdapter.ViewHolder {
		var TVBasic = results!![position]

		var rating = "User Rating: " + TVBasic.voteAverage
		holder.txtName!!.text = TVBasic.name
		holder.txtComment!!.text = rating
		com.squareup.picasso.Picasso.with(context).load(context.resources.getString(uk.ac.tees.p4061644.tvcheck_redo.R.string.base_address_w185).toString() + TVBasic.posterPath)
				.placeholder(uk.ac.tees.p4061644.tvcheck_redo.R.drawable.ic_default_search_image)
				.into(holder.imgView!!)
		return holder
	}


	override fun getItem(position: Int): String{
		return com.google.gson.Gson().toJson(results!![position])

	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun getCount(): Int {
		return results!!.size
	}

}