package uk.ac.tees.p4061644.tvcheck_redo.Adapters

import android.app.Activity
import android.content.Context
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import com.omertron.themoviedbapi.model.tv.TVEpisodeInfo
import com.omertron.themoviedbapi.model.tv.TVSeasonBasic
import com.squareup.picasso.Picasso
import org.apache.commons.lang3.mutable.Mutable
import org.w3c.dom.Text
import uk.ac.tees.p4061644.tvcheck_redo.R
import uk.ac.tees.p4061644.tvcheck_redo.models.*

/**
 * Created by Craig on 08/03/2018.
 */
class SeasonEpisodeListAdapter(private var activity: Activity, private var seasons:List<TVSeasonBasic>?, private var episodes:List<TVEpisodeInfo>?, private var context: Context,private var user:User, private var TVID:Int):BaseAdapter() {

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

	/**
	 * Assigns a Layout to a ViewHolder and calls Method to populate the ViewHolder with data
	 * depending if a passed list is null or not
	 * @return A View with a ViewHolder as its tag
	 */
	override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
		val view: android.view.View?
		val viewHolder: SeasonEpisodeListAdapter.ViewHolder

		if (convertView == null){
			val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as android.view.LayoutInflater
			view = inflater.inflate(R.layout.season_item_layout,null)
			viewHolder = SeasonEpisodeListAdapter.ViewHolder(view)
			view!!.tag = viewHolder
		} else{
			view = convertView
			viewHolder = view.tag as SeasonEpisodeListAdapter.ViewHolder
		}
		if (seasons != null) {
			handleSeasons(viewHolder,position)
		}else{
			handleEpisodes(viewHolder,position)
		}

		return view
	}

	/**
	 * Assigns data to the elements of the ViewHolder passed with season data
	 * @param [holder] ViewHolder Instance with an assigned layout
	 * @param [position] Position of the chosen item in the list
	 * @return ViewHolder Instance with data assigned to layouts elements
	 */
	fun handleSeasons(holder: SeasonEpisodeListAdapter.ViewHolder, position: Int): SeasonEpisodeListAdapter.ViewHolder {
		var TVSeasonBasic = seasons!![position]
		if(TVSeasonBasic.seasonNumber == 0){
			holder.txtName?.text = "Specials"
		}else{
			holder.txtName?.text = "Season " + TVSeasonBasic.seasonNumber.toString()
		}
		var episodeCount = TVSeasonBasic.episodeCount.toString() + " Episodes"
		holder.txtEpisodes!!.text = episodeCount

		com.squareup.picasso.Picasso.with(context)
				.load(context.resources.getString(uk.ac.tees.p4061644.tvcheck_redo.R.string.base_address_w185).toString() + TVSeasonBasic.posterPath)
				.placeholder(R.drawable.ic_default_search_image)
				.into(holder.image)
		return holder
	}

	/**
	 * Assigns data to the elements of the ViewHolder passed with episode data
	 * @param [holder] ViewHolder Instance with an assigned layout
	 * @param [position] Position of the chosen item in the list
	 * @return ViewHolder Instance with data assigned to layouts elements
	 */
	fun handleEpisodes(holder: SeasonEpisodeListAdapter.ViewHolder, position: Int): SeasonEpisodeListAdapter.ViewHolder {
		var episode = episodes!![position]
		holder.txtName!!.text = episode.name
		holder.txtEpisodes!!.text = episode.airDate

		Picasso.with(context).load(context.resources.getString(uk.ac.tees.p4061644.tvcheck_redo.R.string.base_address_w500).toString() + episode.stillPath)
				.placeholder(R.drawable.ic_default_search_image)
				.into(holder.image)
		return holder
	}

	/**
	 * Returns a Json string of element at Position.
	 * @param [position] Position of chosen item in list
	 * @return JSON String of element at position
	 */
	override fun getItem(position: Int): String {
		if (seasons != null){
			return Gson().toJson(seasons!![position])
		}else{
			return Gson().toJson(episodes!![position])
		}
	}

	/**
	 * Returns a the Long of the position passed
	 * @param [position] Position of chosen item in list
	 */
	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	/**
	 * Returns the list of list.
	 * @return Size of list that is not null
	 */
	override fun getCount(): Int {
		if (seasons != null){
			return seasons!!.size
		}else{
			return episodes!!.size
		}
	}
}