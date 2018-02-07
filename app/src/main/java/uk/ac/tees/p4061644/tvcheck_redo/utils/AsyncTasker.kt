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
import android.util.Log
import uk.ac.tees.p4061644.tvcheck_redo.R


/**
 * Created by Craig on 01/02/2018.
 */

class AsyncTasker {

	private val api: TheMovieDbApi = TheMovieDbApi("484e1ff4932e21df205102c405aaf440")
	private var list: List<TVBasic>? = null
	private var show: TVInfo? = null
	private var season: TVSeasonInfo? = null
	private var episode: TVEpisodeInfo? = null
	private var showModel: Show? = null

	fun searchShows(search: String): List<TVBasic>? {
		reset()
		setList(searchShowsTask(search).execute().get())
		Log.d("ListCheck1", list.toString())
		return list
	}

	fun getShow(id : Int): TVInfo {
		show = null
		setShow(getShowTask(id).execute().get())
		return show!!
	}

	fun getSeason(num: Int, TVid: Int): TVSeasonInfo {

		setSeason(getSeasonTask(num,TVid).execute().get())
		return season!!
	}

	fun getEpisode(EpNum: Int,TVid: Int,seasonNum: Int): TVEpisodeInfo {
		episode = null
		setEpisode(getEpisodeTask(EpNum,seasonNum,TVid).execute().get())
		return episode!!
	}

	private fun setList(list: List<TVBasic>?){
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


	internal inner class searchShowsTask constructor(search:String) : AsyncTask<Void, Void, List<TVBasic>>() {
		private var api: TheMovieDbApi? = this@AsyncTasker.api
		var term: String? = null
		init{
			term = search
		}
		override fun doInBackground(vararg voids: Void): List<TVBasic> {
			var list: List<TVBasic> = api!!.searchTV(term,1,"en",null, SearchType.PHRASE).results
			return list!!
		}

		override fun onPostExecute(result: List<TVBasic>?) {
			super.onPostExecute(result)
		}
	}

	internal inner class getShowTask constructor(TVid: Int) : AsyncTask<Void, Void, TVInfo>() {
		private var api:  TheMovieDbApi? = this@AsyncTasker.api
		var info : TVInfo? = null

		var TVid: Int? = null

		init{
			this.TVid = TVid
		}

		override fun doInBackground(vararg voids: Void): TVInfo? {
			info = api!!.getTVInfo(TVid!!,"en")

			return info
		}

		override fun onPostExecute(result: TVInfo) {
			super.onPostExecute(result)
		}
	}

	internal inner class getSeasonTask constructor(seasonNum: Int, TVid: Int) : AsyncTask<Void, Void, TVSeasonInfo>() {
		private var api:  TheMovieDbApi? = this@AsyncTasker.api
		var info : TVSeasonInfo? = null

		var seasonNum: Int? = null
		var TVid: Int? = null

		init{
			this.seasonNum = seasonNum
			this.TVid = TVid
		}

		override fun doInBackground(vararg voids: Void): TVSeasonInfo? {
			info = api!!.getSeasonInfo(TVid!!,seasonNum!!,"en")
			return info
		}

		override fun onPostExecute(result: TVSeasonInfo) {
			super.onPostExecute(result)
		}
	}

	internal inner class getEpisodeTask constructor(epNum: Int,seasonNum: Int,TVid: Int) : AsyncTask<Void, Void, TVEpisodeInfo>() {
		private var api:  TheMovieDbApi? = this@AsyncTasker.api
		var info : TVEpisodeInfo? = null

		var epNum: Int? = null
		var seasonNum: Int? = null
		var TVid: Int? = null

		init{
			this.epNum = epNum
			this.seasonNum = seasonNum
			this.TVid = TVid
		}

		override fun doInBackground(vararg voids: Void): TVEpisodeInfo? {
			info = api!!.getEpisodeInfo(TVid!!,seasonNum!!,epNum!!,"en")
			return info
		}

		override fun onPostExecute(result: TVEpisodeInfo) {
			super.onPostExecute(result)
		}
	}

}