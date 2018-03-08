package uk.ac.tees.p4061644.tvcheck_redo.utils

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
class SeasonEpisodeListAdapter(private var activity: Activity, private var seasons:List<TVSeasonBasic>?, private var episodes:List<TVEpisodeInfo>?, private var context: Context):BaseAdapter() {


	class ViewHolder(row: View?){
		var txtName: TextView? = null
		var txtEpisodes: TextView? = null
		var image: ImageView? = null
		init {
			this.txtName = row?.findViewById(R.id.season_num) as TextView?
			this.txtEpisodes = row?.findViewById(R.id.episodes_count) as TextView?
			this.image = row?.findViewById(R.id.img_view) as ImageView
		}
	}

	val base_address = "https://image.tmdb.org/t/p/w185"
	override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
		val view: View?
		val viewHolder: ViewHolder

		if (convertView == null){
			val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
			view = inflater.inflate(R.layout.season_item_layout,null)
			viewHolder = ViewHolder(view)
			view!!.tag = viewHolder
		} else{
			view = convertView
			viewHolder = view.tag as ViewHolder
		}
		if (seasons != null) {
			handleSeasons(viewHolder,position)
		}else{
			handleEpisodes(viewHolder,position)
		}

		return view
	}


	fun handleSeasons(holder:ViewHolder,position: Int): ViewHolder{
		var TVSeasonBasic = seasons!![position]
		var seasonNum = "Season " + TVSeasonBasic.seasonNumber.toString()
		var episodeCount = TVSeasonBasic.episodeCount.toString() + " Episodes"
		holder.txtName?.text = seasonNum
		holder.txtEpisodes!!.text = episodeCount
		Picasso.with(context).load(base_address + TVSeasonBasic.posterPath)
				.placeholder(R.drawable.ic_default_search_image)
				.into(holder.image)
		return holder
	}

	fun handleEpisodes(holder: ViewHolder, position: Int): ViewHolder{
		var episode = episodes!![position]
		holder.txtName!!.text = episode.name
		holder.txtEpisodes!!.text = episode.airDate
		Picasso.with(context).load(base_address + episode.posterPath)
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