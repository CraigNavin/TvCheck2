package uk.ac.tees.p4061644.tvcheck_redo.utils

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.omertron.themoviedbapi.TheMovieDbApi
import com.omertron.themoviedbapi.enumeration.SearchType
import com.omertron.themoviedbapi.enumeration.TVEpisodeMethod
import com.omertron.themoviedbapi.model.person.ExternalID
import com.omertron.themoviedbapi.model.tv.TVBasic
import com.omertron.themoviedbapi.model.tv.TVEpisodeInfo
import com.omertron.themoviedbapi.model.tv.TVInfo
import com.omertron.themoviedbapi.model.tv.TVSeasonInfo
import uk.ac.tees.p4061644.tvcheck_redo.R
import uk.ac.tees.p4061644.tvcheck_redo.models.ListModel
import uk.ac.tees.p4061644.tvcheck_redo.models.Show
import uk.ac.tees.p4061644.tvcheck_redo.models.User
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Craig on 01/02/2018.
 */

class AsyncTasker(context: Context) {

	private var api : TheMovieDbApi? = null

	init {
		api = TheMovieDbApi(context.resources.getString(R.string.Api_key))
	}

	fun searchShows(search: String): ArrayList<TVBasic>? {
		var list = searchShowsTask(search).execute().get()
		if (list == null){
			return ArrayList()
		}else{
			return ArrayList(list)
		}
	}

	fun fillhome(getType:Int, TVid: Int?): ArrayList<TVBasic>?{
		var list: List<TVBasic> = populateHomeTask(getType,TVid).execute().get()
		if (list.isEmpty()){
			return ArrayList()
		}else{
			return ArrayList(list)
		}
	}


	fun getUserList(list: ArrayList<Show>):ArrayList<TVBasic>{
		var retList = ArrayList<TVBasic>()
		list.forEach { retList.add(getShowBasicAsync(it.id)) }
		return  retList
	}

	fun getShowInfoAsync(id:Int): TVInfo {
		return getShowTask(id).execute().get()
	}
	fun getShowBasicAsync(id:Int): TVBasic {
		return getShowTask(id).execute().get()
	}

	fun getSeasonAsync(num: Int, TVid: Int): TVSeasonInfo {

		return getSeasonTask(num,TVid).execute().get()
	}

	fun getEpisodeAsync(EpNum: Int,TVid: Int,seasonNum: Int): TVEpisodeInfo {
		return getEpisodeTask(EpNum,seasonNum,TVid).execute().get()
	}



	internal inner class searchShowsTask constructor(search:String) : AsyncTask<Void, Void, List<TVBasic>>() {
		private var api: TheMovieDbApi? = this@AsyncTasker.api
		var term: String? = null
		init{
			term = search
		}
		override fun doInBackground(vararg voids: Void): List<TVBasic> {
			var list: List<TVBasic> = api!!.searchTV(term,1,"en",null, SearchType.PHRASE).results
			return list
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

	internal inner class populateHomeTask(val getType: Int,val TVid: Int?)  : AsyncTask<Void, Void, List<TVBasic>>() {
		private var api: TheMovieDbApi? = this@AsyncTasker.api

		override fun doInBackground(vararg voids: Void): List<TVBasic> {
			var list: List<TVBasic>
			when(getType){
				1 -> list = api!!.getTVPopular(1,"en").results
				2 -> list = api!!.getTVTopRated(1,"en").results
				3 -> if (TVid != null){
					list = api!!.getTVSimilar(TVid,1,"en").results
				}else{
					list = Collections.emptyList()
				}
				else -> list = Collections.emptyList()
			}
			return list
		}

		override fun onPostExecute(result: List<TVBasic>?) {
			super.onPostExecute(result)
		}
	}

}