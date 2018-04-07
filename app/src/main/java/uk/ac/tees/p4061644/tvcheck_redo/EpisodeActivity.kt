package uk.ac.tees.p4061644.tvcheck_redo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.omertron.themoviedbapi.model.tv.TVEpisodeInfo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_episode.*
import kotlinx.android.synthetic.main.layout_bottom_navigation_view.*
import uk.ac.tees.p4061644.tvcheck_redo.models.Show
import uk.ac.tees.p4061644.tvcheck_redo.models.User
import uk.ac.tees.p4061644.tvcheck_redo.utils.BottomNavigationBarHelper
import uk.ac.tees.p4061644.tvcheck_redo.utils.DatabaseHandler

class EpisodeActivity : AppCompatActivity() {

	private val TAG: String = "EpisodeActivity"
	private val activity_Num: Int = 1
	private var episode: TVEpisodeInfo? = null
	private var user: User? = null
	private var id : Int = 0


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_episode)
		setup()
	}

	/**
	 * Retrieves the Show with matching ID from intent from the users List.
	 * Show is then used to determine if episode has been watched or not
	 * @return Show object from user list
	 */
	fun getShowFromLists(): Show? {
		var TVID = intent.extras.get("TVID")
		user!!.list!!.forEach { it.list!!.forEach { if (it.id == TVID){ return it } }}
		return null
	}

	/**
	 * Sets up view elements with all of the episode information
	 */
	fun setView(){
		EPName_TV!!.text = episode!!.name
		var seasonNum :Int = 0
		if (getShowFromLists() != null){
			if(getShowFromLists()!!.seasons!![0].seasonNumber == 0){
				seasonNum = episode!!.seasonNumber
			}else{
				seasonNum = episode!!.seasonNumber -1
			}
			watched_box!!.isChecked = getShowFromLists()!!.seasons!![seasonNum].episodes[episode!!.episodeNumber - 1].watched
		}


		if (episode!!.overview == null || episode!!.overview == ""){
			Bio_TV!!.text = "No Overview"
		}else{
			Bio_TV!!.text = episode!!.overview
		}
		Picasso.with(applicationContext).load(applicationContext.getString(R.string.base_address_original) + episode!!.stillPath)
				.placeholder(R.drawable.ic_default_search_image)
				.fit()
				.into(PosterView)

		watched_box!!.setOnCheckedChangeListener { buttonView, isChecked ->
			if (inLists()){
				getShowFromLists()!!.seasons!![seasonNum].episodes[episode!!.episodeNumber -1].watched = isChecked
				user = DatabaseHandler(applicationContext).update(user!!)

				Toast.makeText(applicationContext,"Episode Updated", Toast.LENGTH_SHORT).show()
			}else{
				watched_box!!.isChecked = false
				Toast.makeText(applicationContext,"Please add this show to a list before marking episodes as watched",Toast.LENGTH_SHORT).show()
			}

		}
	}

	/**
	 * Checks if any of the users lists contain a show matching id passed
	 * @return Boolean that represents if any of the users list contain a show with a matching id
	 * to the currently selected show
	 */
	fun inLists(): Boolean{
		for (list in user!!.list!!){
			if (user!!.checkListContainsShow(id!!,list.name)){
				return true
			}else{
				return false
			}
		}
		return false
	}

	/**
	 * Sets up variable and calls method that handles View elements
	 */
	fun setup(){
		Log.d(TAG,intent.extras.get("TVID").toString())
		id = intent.extras.get("TVID") as Int
		user = Gson().fromJson(intent.getStringExtra("User"))
		episode = Gson().fromJson(intent.getStringExtra("Episode"))
		setView()
		setupBottomnavigatioView()
	}

	inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

	/**
	 * Instantiates the bottom navigation view and sets the menu values to the navigation bar
	 */
	private fun setupBottomnavigatioView(){
		Log.d(TAG,"setupBottomNavigationView")
		BottomNavigationBarHelper.setupBottomNavigationBar(bottomNavViewBar)
		BottomNavigationBarHelper.enableNavigation(applicationContext, bottomNavViewBar,Gson().toJson(user))
		val menu: Menu? = bottomNavViewBar?.menu
		val menuI: MenuItem? = menu?.getItem(activity_Num)
		menuI?.isChecked = true
	}

	/**
	 * Overridden onDestroy function runs garbage collection to help keep RAM usage as low as possible
	 */
	override fun onDestroy() {
		//android.os.Process.killProcess(android.os.Process.myPid());

		super.onDestroy()
		Runtime.getRuntime().gc()

	}

}
