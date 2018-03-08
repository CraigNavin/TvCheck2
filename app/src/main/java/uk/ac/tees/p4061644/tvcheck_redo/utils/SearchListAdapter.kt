package uk.ac.tees.p4061644.tvcheck_redo.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import com.omertron.themoviedbapi.model.tv.TVBasic
import com.squareup.picasso.Picasso
import uk.ac.tees.p4061644.tvcheck_redo.R
import uk.ac.tees.p4061644.tvcheck_redo.models.Show
import java.io.InputStream
import java.net.URL

/**
 * Created by Craig on 05/03/2018.
 */
class SearchListAdapter(private var activity: Activity, private var results: ArrayList<TVBasic>?, private var items: ArrayList<Show>?,private var context: Context): BaseAdapter(){
	val gson = Gson()
	class ViewHolder(row: View?){
		var txtName: TextView? = null
		var txtComment: TextView? = null
		var imgView: ImageView? = null


		init {
			this.txtName = row?.findViewById(R.id.txtName) as TextView?
			this.txtComment = row?.findViewById(R.id.txtComment) as TextView?
			this.imgView = row?.findViewById(R.id.img_view) as ImageView
		}
	}

	val base_address = "https://image.tmdb.org/t/p/w185"
	override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
		val view: View?
		val viewHolder: ViewHolder

		if (convertView == null){
			val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
			view = inflater.inflate(R.layout.search_item_layout,null)
			viewHolder = ViewHolder(view)
			view!!.tag = viewHolder
		} else{
			view = convertView
			viewHolder = view.tag as ViewHolder
		}
		if (results != null) {
			handleResults(viewHolder,position)
		}else{
			handleItems(viewHolder,position)
		}

		viewHolder

		return view
	}

	fun handleItems(holder:ViewHolder,position: Int): ViewHolder{
		var Show = items!![position]
		holder.txtName!!.text = Show.name
		Picasso.with(context).load(base_address + Show.PosterPath)
				.placeholder(R.drawable.ic_default_search_image)
				.into(holder.imgView!!)
		return holder

	}

	fun handleResults(holder: ViewHolder,position: Int): ViewHolder{
		var TVBasic = results!![position]

		var rating = "User Rating: " + TVBasic.voteAverage
		holder.txtName!!.text = TVBasic.name
		holder.txtComment!!.text = rating
		Picasso.with(context).load(base_address + TVBasic.posterPath)
				.placeholder(R.drawable.ic_default_search_image)
				.into(holder.imgView!!)
		return holder
	}


	override fun getItem(position: Int): String{
		if (results != null) {
			return gson.toJson(results!![position])
		}else{
			return gson.toJson(items!![position])
		}
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun getCount(): Int {
		if (results != null) {
			return results!!.size
		}else{
			return items!!.size
		}

	}

}