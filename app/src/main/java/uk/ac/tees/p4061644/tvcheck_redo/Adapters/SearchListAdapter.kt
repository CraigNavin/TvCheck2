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
class SearchListAdapter(private var activity: Activity, private var results: ArrayList<TVBasic>?, private var context: Context): BaseAdapter(){

	/**
	 * View Holder class to manage all of the Views Elements
	 * @param [row] View of an assigned layout
	 */
	class ViewHolder(row:View?){
		var txtName: TextView? = null
		var txtComment: TextView? = null
		var imgView: ImageView? = null


		init {
			this.txtName = row?.findViewById(R.id.txtName) as TextView?
			this.txtComment = row?.findViewById(R.id.txtComment) as TextView?
			this.imgView = row?.findViewById(R.id.img_view) as ImageView
		}
	}

	/**
	 * Assigns a Layout to a ViewHolder and calls Method to populate the ViewHolder with data
	 * @return A View with a ViewHolder as its tag
	 */
	override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
		val view: android.view.View?
		val viewHolder: SearchListAdapter.ViewHolder

		if (convertView == null){
			val inflater = activity.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
			view = inflater.inflate(R.layout.search_item_layout,null)
			viewHolder = SearchListAdapter.ViewHolder(view)
			view!!.tag = viewHolder
		} else{
			view = convertView
			viewHolder = view.tag as SearchListAdapter.ViewHolder
		}

		handleResults(viewHolder,position)

		return view
	}

	/**
	 * Assigns data to the elements of the ViewHolder passed
	 * @param [holder] ViewHolder Instance with an assigned layout
	 * @param [position] Position of the chosen item in the list
	 * @return ViewHolder Instance with data assigned to layouts elements
	 */
	fun handleResults(holder: SearchListAdapter.ViewHolder, position: Int): SearchListAdapter.ViewHolder {
		val TVBasic = results!![position]

		val rating = "User Rating: " + TVBasic.voteAverage
		holder.txtName!!.text = TVBasic.name
		holder.txtComment!!.text = rating
		Picasso.with(context).load(context.resources.getString(uk.ac.tees.p4061644.tvcheck_redo.R.string.base_address_w185).toString() + TVBasic.posterPath)
				.placeholder(uk.ac.tees.p4061644.tvcheck_redo.R.drawable.ic_default_search_image)
				.into(holder.imgView!!)
		return holder
	}

	/**
	 * Returns a Json string of element at Position
	 * @param [position] Position of chosen item in list
	 * @return JSON String of element at position
	 */
	override fun getItem(position: Int): String{
		return Gson().toJson(results!![position])

	}

	/**
	 * Returns a the Long of the position passed
	 * @param [position] Position of chosen item in list
	 */
	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	/**
	 * Returns the size of the list
	 * @return Size of the list
	 */
	override fun getCount(): Int {
		return results!!.size
	}

}