package uk.ac.tees.p4061644.tvcheck_redo.utils

import android.os.AsyncTask
import com.omertron.themoviedbapi.TheMovieDbApi
import com.omertron.themoviedbapi.enumeration.SearchType
import com.omertron.themoviedbapi.model.tv.TVBasic
import com.omertron.themoviedbapi.model.tv.TVEpisodeInfo
import com.omertron.themoviedbapi.model.tv.TVInfo
import com.omertron.themoviedbapi.model.tv.TVSeasonInfo
import com.omertron.themoviedbapi.results.ResultList
import uk.ac.tees.p4061644.tvcheck_redo.models.Show
import android.provider.Settings.Global.getString
import uk.ac.tees.p4061644.tvcheck_redo.R


/**
 * Created by Craig on 01/02/2018.
 */
class AsyncTasker {

	private val api: TheMovieDbApi = TheMovieDbApi("484e1ff4932e21df205102c405aaf440")
	private var list: ResultList<TVBasic>? = null
	private var show: TVInfo? = null
	private var season: TVSeasonInfo? = null
	private var episode: TVEpisodeInfo? = null
	private var showModel: Show? = null

	fun searchShows(search: String){
		reset()
		searchShowsTask(search).execute()
	}

	fun getShow(id : Int): TVInfo {
		show = null
		getShowTask(id).execute()
		return show!!
	}

	fun getSeason(id: Int): TVSeasonInfo {
		season = null
		getSeasonTask(id).execute()
		return season!!
	}

	fun getEpisode(EpNum: Int?): TVEpisodeInfo {
		episode = null
		getEpisodeTask(EpNum).execute()
		return episode!!
	}

	private fun setList(list: ResultList<TVBasic>?){
		this.list = list
	}

	private fun setShow(showInfo: TVInfo?){
		this.show = showInfo
	}

	private fun setSeason(seasonInfo: TVSeasonInfo){
		this.season = seasonInfo
	}

	private fun setEpisode(episodeInfo: TVEpisodeInfo){
		this.episode = episodeInfo
	}


	fun reset(){
		list = null
		show = null
		season = null
		episode = null
		showModel = null
	}

	internal inner class searchShowsTask constructor(search:String) : AsyncTask<Void, Void, ResultList<TVBasic>>() {
		private var api: TheMovieDbApi? = this@AsyncTasker.api
		var list : ResultList<TVBasic>? = null


		var term: String? = null

		init{
			term = search
		}

		override fun doInBackground(vararg voids: Void): ResultList<TVBasic>? {
			list = api!!.searchTV(term,1,"en",1950, SearchType.PHRASE)

			return list
		}

		override fun onPostExecute(result: ResultList<TVBasic>?) {
			super.onPostExecute(result)
			this@AsyncTasker.setList(result)
		}
	}

	internal inner class getShowTask constructor(id: Int) : AsyncTask<Void, Void, TVInfo>() {
		private var api:  TheMovieDbApi? = this@AsyncTasker.api
		var info : TVInfo? = null

		var id: Int? = null

		init{
			this.id = id
		}

		override fun doInBackground(vararg voids: Void): TVInfo? {
			info = api!!.getTVInfo(id!!,"en")

			return info
		}

		override fun onPostExecute(result: TVInfo) {
			super.onPostExecute(result)
			this@AsyncTasker.setShow(result)

		}
	}

	internal inner class getSeasonTask constructor(seasonNum: Int?) : AsyncTask<Void, Void, TVSeasonInfo>() {
		private var api:  TheMovieDbApi? = this@AsyncTasker.api
		var info : TVSeasonInfo? = null

		var seasonNum: Int? = null

		init{
			this.seasonNum = seasonNum
		}

		override fun doInBackground(vararg voids: Void): TVSeasonInfo? {
			info = api!!.getSeasonInfo(show!!.id,seasonNum!!,"en")
			return info
		}

		override fun onPostExecute(result: TVSeasonInfo) {
			super.onPostExecute(result)
			this@AsyncTasker.setSeason(result)

		}
	}

	internal inner class getEpisodeTask constructor(epNum: Int?) : AsyncTask<Void, Void, TVEpisodeInfo>() {
		private var api:  TheMovieDbApi? = this@AsyncTasker.api
		var info : TVEpisodeInfo? = null

		var epNum: Int? = null

		init{
			this.epNum = epNum
		}

		override fun doInBackground(vararg voids: Void): TVEpisodeInfo? {
			info = api!!.getEpisodeInfo(show!!.id,season!!.id,epNum!!,"en")
			return info
		}

		override fun onPostExecute(result: TVEpisodeInfo) {
			super.onPostExecute(result)
			this@AsyncTasker.setEpisode(result)

		}
	}

}