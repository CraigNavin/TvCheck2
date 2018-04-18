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
import com.omertron.themoviedbapi.model.credits.MediaCreditCast
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import uk.ac.tees.p4061644.tvcheck_redo.R

/**
 * Created by Craig on 07/04/2018.
 */
class CastAdapter (private var activity: Activity,private var cast:List<MediaCreditCast>,private var context: Context):BaseAdapter() {

	/**
	 * View Holder class to manage all of the Views Elements
	 * @param [row] View of an assigned layout
	 */
	class ViewHolder(row: View?){
		var txtName: TextView? = null
		var txtEpisodes: TextView? = null
		var image: ImageView? = null
		init {
			this.txtName = row?.findViewById(R.id.season_num) as TextView?
			this.txtEpisodes = row?.findViewById(R.id.episodes_count) as TextView?
			this.image = row?.findViewById(R.id.img_view) as ImageView?
		}
	}

	override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
		val view: View?
		val viewHolder: CastAdapter.ViewHolder

		if (convertView == null){
			val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
			view = inflater.inflate(R.layout.season_item_layout,null)
			viewHolder = ViewHolder(view)
			view!!.tag = viewHolder
		}else{
			view = convertView
			viewHolder = view.tag as ViewHolder
		}
		populateView(viewHolder,position)

		return view
	}

	fun populateView(holder: ViewHolder,position: Int): ViewHolder{
		val castMember = cast[position]
		holder.txtName!!.text = castMember.name
		holder.txtEpisodes!!.text = castMember.character

		Picasso.with(context).load(context.getString(R.string.base_address_w185) + castMember.artworkPath).memoryPolicy(MemoryPolicy.NO_STORE).memoryPolicy(MemoryPolicy.NO_CACHE)
				.placeholder(R.drawable.ic_default_search_image)
				.into(holder.image)
		return holder
	}

	override fun getItem(position: Int): Any {
		return Gson().toJson(cast[position])
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun getCount(): Int {
		return cast.size
	}
}