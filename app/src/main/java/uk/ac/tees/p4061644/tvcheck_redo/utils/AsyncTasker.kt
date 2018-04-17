package uk.ac.tees.p4061644.tvcheck_redo.utils

import android.content.Context
import android.os.AsyncTask
import com.omertron.themoviedbapi.TheMovieDbApi
import com.omertron.themoviedbapi.enumeration.PeopleMethod
import com.omertron.themoviedbapi.enumeration.SearchType
import com.omertron.themoviedbapi.enumeration.TVMethod
import com.omertron.themoviedbapi.model.person.PersonInfo
import com.omertron.themoviedbapi.model.tv.TVBasic
import com.omertron.themoviedbapi.model.tv.TVInfo
import com.omertron.themoviedbapi.model.tv.TVSeasonInfo
import uk.ac.tees.p4061644.tvcheck_redo.R
import uk.ac.tees.p4061644.tvcheck_redo.models.Show
import java.util.*
import kotlin.collections.ArrayList


/**
 * This class handles all of the calls to the API. It contains methods that retrieve different
 * data from the API.
 * @param [context] Current activities context for access to string values
 * @constructor Creates a AsyncTasker object for retrieval of data
 * Created by Craig on 01/02/2018.
 */
class AsyncTasker(context: Context) {

	private var api : TheMovieDbApi? = null

	/**
	 * Assigns api value to a TheMovieDbApi object so its methods can be used within this class
	 */
	init {
		api = TheMovieDbApi(context.resources.getString(R.string.Api_key))
	}

	/**
	 * Retrieves a list of TVBasic objects that match or closely match the term passed
	 * @param [search] the search term that will be used to search for Tv Shows
	 * @return ArrayList of TVBasic objects that match or closely match the search term
	 */
	fun searchShows(search: String): ArrayList<TVBasic>? {
		val list = searchShowsTask(search).execute().get()
		if (list == null){
			return ArrayList()
		}else{
			return ArrayList(list)
		}
	}

	/**
	 * Retrives different data from API depending on the getType passed. Can return a list of
	 * popular Tv Shows, Top rated Tv Shows or Similar Tv Shows to a passed TV ID
	 * @param [getType] will change what the contents of the returning Arraylist is
	 * @param [TVid] Id of a show that will be used to get similar shows to show with this id
	 * @return Arraylist containing TVBasic objects. These can be popular shows, Top rated shows or
	 * similar shows to the show with the id passed as a parameter
	 */
	fun fillhome(getType:Int, TVid: Int?): ArrayList<TVBasic>?{
		val list: List<TVBasic> = populateHomeTask(getType,TVid).execute().get()
		if (list.isEmpty()){
			return ArrayList()
		}else{
			return ArrayList(list)
		}
	}

	/**
	 * Retrieves each show inside of a users list using each shows id
	 * @param [list] A list containing Show Objects.
	 * @return ArrayList containing TVBasic objects.
	 */
	fun getUserList(list: ArrayList<Show>):ArrayList<TVBasic>{
		val retList = ArrayList<TVBasic>()
		list.forEach { retList.add(getShowBasicAsync(it.id)) }
		return  retList
	}

	/**
	 * Retrieves a shows TVInfo object that matches an ID
	 * @param [id] The id that will be used to retrieve the TVInfo object
	 * @return TVInfo object that matched with ID passed
	 */
	fun getShowInfoAsync(id:Int): TVInfo {
		return getShowTask(id).execute().get()
	}

	/**
	 * Retrieves a shows TVBasic object that matches an ID
	 * @param [id] The id that will be used to retrieve the TVBasic object
	 * @return TVBasic object that matched with ID passed
	 */
	fun getShowBasicAsync(id:Int): TVBasic {
		return getShowTask(id).execute().get()
	}

	/**
	 * Retrieves a TVSeasonInfo object
	 * @param [num] The season number of the show that is to be returned
	 * @param [TVid] The id of the show that the season is from.
	 * @return A TvSeasonInfo object
	 */
	fun getSeasonAsync(num: Int, TVid: Int): TVSeasonInfo {
		return getSeasonTask(num,TVid).execute().get()
	}

	fun getPerson(id: Int):PersonInfo{
		return getPersonTask(id).execute().get()
	}

	/**
	 * Class used to retrieve a List of TVBasic objects on a background thread to avoid clashes on UI thread
	 * @param [search] The term that will be used to search for TV Shows using the API
	 * @constructor Creates a searchShowsTask object that performs its methods on a background thread
	 */
	internal inner class searchShowsTask constructor(val search:String) : AsyncTask<Void, Void, List<TVBasic>>() {
		private var api: TheMovieDbApi? = this@AsyncTasker.api

		override fun doInBackground(vararg voids: Void): List<TVBasic> {
			val list: List<TVBasic> = api!!.searchTV(search,1,"en",null, SearchType.PHRASE).results
			return list
		}

		override fun onPostExecute(result: List<TVBasic>?) {
			super.onPostExecute(result)
		}
	}

	/**
	 * Class used to retrieve a TVInfo object in the background to avoid clashes on UI Thread
	 * @param [TVid] The ID that will be used to retrieve a shows Info
	 * @constructor Creates a getShowTask object that performs its methods on a background thread
	 */
	internal inner class getShowTask constructor(val TVid: Int) : AsyncTask<Void, Void, TVInfo>() {
		private var api:  TheMovieDbApi? = this@AsyncTasker.api
		var info : TVInfo? = null


		override fun doInBackground(vararg voids: Void): TVInfo? {
			info = api!!.getTVInfo(TVid,"en",TVMethod.CREDITS.propertyString)

			return info
		}

		override fun onPostExecute(result: TVInfo) {
			super.onPostExecute(result)
		}
	}

	/**
	 * Class used to retrieve a TVSeasonInfo object in the background to avoid clashes on UI Thread
	 * @param [seasonNum] The season number of the season that is required to be retrieved
	 * @param [TVid] The ID of the show that the season Info will be from
	 * @constructor Creates a getSeasonTask object that performs its methods on a background thread
	 */
	internal inner class getSeasonTask constructor(val seasonNum: Int, val TVid: Int) : AsyncTask<Void, Void, TVSeasonInfo>() {
		private var api:  TheMovieDbApi? = this@AsyncTasker.api
		var info : TVSeasonInfo? = null


		override fun doInBackground(vararg voids: Void): TVSeasonInfo? {
			info = api!!.getSeasonInfo(TVid,seasonNum,"en")
			return info
		}

		override fun onPostExecute(result: TVSeasonInfo) {
			super.onPostExecute(result)
		}
	}

	/**
	 * Class used to retrieve a List of TVBasic objects in the background to avoid clashes on UI Thread
	 * @param [getType] Integer that changes the data that is retrieved from the API
	 * @param [TVid] Nullable Integer that will be used to get a list of similar shows to the show that this ID matches with
	 * @constructor Creates a populateHomeTask object that performs its methods on a background thread
	 */
	internal inner class populateHomeTask constructor(val getType: Int, val TVid: Int?)  : AsyncTask<Void, Void, List<TVBasic>>() {
		private var api: TheMovieDbApi? = this@AsyncTasker.api

		override fun doInBackground(vararg voids: Void): List<TVBasic> {
			val list: List<TVBasic>
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

	/**
	 *
	 */
	internal inner class getPersonTask constructor(val id: Int) :AsyncTask<Void,Void,PersonInfo>(){
		private var api: TheMovieDbApi? = this@AsyncTasker.api

		override fun doInBackground(vararg params: Void?): PersonInfo {
			return api!!.getPersonInfo(id,PeopleMethod.TV_CREDITS.propertyString)
		}

		override fun onPostExecute(result: PersonInfo?) {
			super.onPostExecute(result)
		}

	}


}