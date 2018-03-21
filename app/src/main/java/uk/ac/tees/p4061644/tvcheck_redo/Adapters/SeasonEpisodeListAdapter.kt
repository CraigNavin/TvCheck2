package uk.ac.tees.p4061644.tvcheck_redo.Adapters

import android.app.Activity
import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import com.omertron.themoviedbapi.model.tv.TVEpisodeInfo
import com.omertron.themoviedbapi.model.tv.TVSeasonBasic
import com.squareup.picasso.Picasso
import org.apache.commons.lang3.mutable.Mutable
import org.w3c.dom.Text
import uk.ac.tees.p4061644.tvcheck_redo.R

/**
 * Created by Craig on 08/03/2018.
 */
class SeasonEpisodeListAdapter(private var activity: android.app.Activity, private var seasons:List<com.omertron.themoviedbapi.model.tv.TVSeasonBasic>?, private var episodes:List<com.omertron.themoviedbapi.model.tv.TVEpisodeInfo>?, private var context: android.content.Context): android.widget.BaseAdapter() {


	class ViewHolder(row: android.view.View?){
		var txtName: android.widget.TextView? = null
		var txtEpisodes: android.widget.TextView? = null
		var image: android.widget.ImageView? = null
		init {
			this.txtName = row?.findViewById(uk.ac.tees.p4061644.tvcheck_redo.R.id.season_num) as android.widget.TextView?
			this.txtEpisodes = row?.findViewById(uk.ac.tees.p4061644.tvcheck_redo.R.id.episodes_count) as android.widget.TextView?
			this.image = row?.findViewById(uk.ac.tees.p4061644.tvcheck_redo.R.id.img_view) as android.widget.ImageView
		}
	}

	override fun getView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup?): android.view.View {
		val view: android.view.View?
		val viewHolder: uk.ac.tees.p4061644.tvcheck_redo.Adapters.SeasonEpisodeListAdapter.ViewHolder

		if (convertView == null){
			val inflater = activity.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE) as android.view.LayoutInflater
			view = inflater.inflate(uk.ac.tees.p4061644.tvcheck_redo.R.layout.season_item_layout,null)
			viewHolder = uk.ac.tees.p4061644.tvcheck_redo.Adapters.SeasonEpisodeListAdapter.ViewHolder(view)
			view!!.tag = viewHolder
		} else{
			view = convertView
			viewHolder = view.tag as uk.ac.tees.p4061644.tvcheck_redo.Adapters.SeasonEpisodeListAdapter.ViewHolder
		}
		if (seasons != null) {
			handleSeasons(viewHolder,position)
		}else{
			handleEpisodes(viewHolder,position)
		}

		return view
	}


	fun handleSeasons(holder: uk.ac.tees.p4061644.tvcheck_redo.Adapters.SeasonEpisodeListAdapter.ViewHolder, position: Int): uk.ac.tees.p4061644.tvcheck_redo.Adapters.SeasonEpisodeListAdapter.ViewHolder {
		var TVSeasonBasic = seasons!![position]
		var seasonNum = "Season " + TVSeasonBasic.seasonNumber.toString()
		var episodeCount = TVSeasonBasic.episodeCount.toString() + " Episodes"
		holder.txtName?.text = seasonNum
		holder.txtEpisodes!!.text = episodeCount
		com.squareup.picasso.Picasso.with(context).load(context.resources.getString(uk.ac.tees.p4061644.tvcheck_redo.R.string.base_address_w185).toString() + TVSeasonBasic.posterPath)
				.placeholder(uk.ac.tees.p4061644.tvcheck_redo.R.drawable.ic_default_search_image)
				.into(holder.image)
		return holder
	}

	fun handleEpisodes(holder: uk.ac.tees.p4061644.tvcheck_redo.Adapters.SeasonEpisodeListAdapter.ViewHolder, position: Int): uk.ac.tees.p4061644.tvcheck_redo.Adapters.SeasonEpisodeListAdapter.ViewHolder {
		var episode = episodes!![position]
		holder.txtName!!.text = episode.name
		holder.txtEpisodes!!.text = episode.airDate
		com.squareup.picasso.Picasso.with(context).load(context.resources.getString(uk.ac.tees.p4061644.tvcheck_redo.R.string.base_address_w500).toString() + episode.stillPath)
				.placeholder(uk.ac.tees.p4061644.tvcheck_redo.R.drawable.ic_default_search_image)
				.into(holder.image)
		return holder
	}

	override fun getItem(position: Int): String {
		var gson = com.google.gson.Gson()
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