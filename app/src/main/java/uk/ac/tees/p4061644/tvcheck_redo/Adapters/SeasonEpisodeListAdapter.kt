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

	fun handleSeasons(holder: SeasonEpisodeListAdapter.ViewHolder, position: Int): SeasonEpisodeListAdapter.ViewHolder {
		var TVSeasonBasic = seasons!![position]
		var seasonNum = "Season " + TVSeasonBasic.seasonNumber.toString()
		var episodeCount = TVSeasonBasic.episodeCount.toString() + " Episodes"
		holder.txtName?.text = seasonNum
		holder.txtEpisodes!!.text = episodeCount

		com.squareup.picasso.Picasso.with(context)
				.load(context.resources.getString(uk.ac.tees.p4061644.tvcheck_redo.R.string.base_address_w185).toString() + TVSeasonBasic.posterPath)
				.placeholder(uk.ac.tees.p4061644.tvcheck_redo.R.drawable.ic_default_search_image)
				.into(holder.image)
		return holder
	}

	fun handleEpisodes(holder: SeasonEpisodeListAdapter.ViewHolder, position: Int): SeasonEpisodeListAdapter.ViewHolder {
		var episode = episodes!![position]
		holder.txtName!!.text = episode.name
		holder.txtEpisodes!!.text = episode.airDate

		com.squareup.picasso.Picasso.with(context).load(context.resources.getString(uk.ac.tees.p4061644.tvcheck_redo.R.string.base_address_w500).toString() + episode.stillPath)
				.placeholder(R.drawable.ic_default_search_image)
				.into(holder.image)
		return holder
	}

	override fun getItem(position: Int): String {
		var gson = Gson()
		if (seasons != null){
			return gson.toJson(seasons!![position])
		}else{
			return gson.toJson(episodes!![position])
		}
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun getCount(): Int {
		if (seasons != null){
			return seasons!!.size
		}else{
			return episodes!!.size
		}
	}
}