package uk.ac.tees.p4061644.tvcheck_redo

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.omertron.themoviedbapi.model.tv.TVEpisodeInfo
import com.omertron.themoviedbapi.model.tv.TVSeasonBasic
import com.omertron.themoviedbapi.model.tv.TVSeasonInfo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_season.*
import kotlinx.android.synthetic.main.layout_bottom_navigation_view.*
import uk.ac.tees.p4061644.tvcheck_redo.models.User
import uk.ac.tees.p4061644.tvcheck_redo.utils.AsyncTasker
import uk.ac.tees.p4061644.tvcheck_redo.utils.BottomNavigationBarHelper
import uk.ac.tees.p4061644.tvcheck_redo.Adapters.SeasonEpisodeListAdapter
import uk.ac.tees.p4061644.tvcheck_redo.models.Show
import uk.ac.tees.p4061644.tvcheck_redo.utils.DatabaseHandler

class SeasonActivity : AppCompatActivity() {

	private val TAG: String = "SeasonActivity"
	private var Async : AsyncTasker? = null
	private val activity_Num: Int = 1
	private var id: Int? = null
	private var user: User? = null


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_season)
		setup()
	}

	/**
	 * Retrieves the Show with matching ID from intent from the users List.
	 * Show is then used to determine if episode has been watched or not
	 * @return Show object from user list
	 */
	fun getShow(): Show?{
		user!!.list!!.forEach { it.list!!.forEach { if (it.id == id){ return it} } }
		return null
	}


	/**
	 * Populates the view elements on this activity with the correct information
	 * Handles what happens when the watched checkbox status changes.
	 * @param [season] TVSeasonInfo object that is used to populate elements with information
	 */
	fun setView(season: TVSeasonInfo){
		var seasonNum = "Season " + season.seasonNumber

		if(getShow() != null){
			if(getShow()!!.seasons!![0].seasonNumber == 0){
				watched_box!!.isChecked = getShow()!!.seasons!![season.seasonNumber].watched
			}else{
				watched_box!!.isChecked = getShow()!!.seasons!![season.seasonNumber-1].watched
			}

		}

		SeasonNum_TV.text = seasonNum
		Bio_TV.text = season.overview
		Picasso.with(applicationContext).load(applicationContext.getString(R.string.base_address_w185) + season.posterPath)
				.placeholder(R.drawable.ic_default_search_image)
				.into(PosterView)

		bottomNavViewBar.bringChildToFront(bottomNavViewBar)
		var adapter = SeasonEpisodeListAdapter(this,null,season.episodes,applicationContext)
		episodes_list.adapter = adapter

		episodes_list.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, ids ->
			val item = parent.getItemAtPosition(position) as String
			val episode: TVEpisodeInfo = Gson().fromJson(item)
			Toast.makeText(applicationContext,episode.name,Toast.LENGTH_SHORT).show()
			val intent = Intent(applicationContext,EpisodeActivity::class.java)//activity_Num 1
			intent.putExtra("Episode",item)
			intent.putExtra("User",Gson().toJson(user))
			intent.putExtra("TVID",id!!)
			applicationContext.startActivity(intent)
		}

		watched_box!!.setOnCheckedChangeListener { buttonView, isChecked ->
			if (inLists()){
				var season = getShow()!!.seasons!![season.seasonNumber]
				season.watched = isChecked

				AlertDialog.Builder(this)
						.setTitle("Watched Season?")
						.setMessage("Do you want to set all this seasons episodes to watched?")
						.setPositiveButton(android.R.string.yes,
								DialogInterface.OnClickListener { dialog, which ->
									season.episodes.forEach { it.watched = isChecked }
									user =DatabaseHandler(applicationContext).update(user!!)
									Toast.makeText(applicationContext,"Season and episodes Updated",Toast.LENGTH_SHORT).show()
								}
						)
						.setNegativeButton(android.R.string.no,
								DialogInterface.OnClickListener { dialog, which ->
									user =DatabaseHandler(applicationContext).update(user!!)
									Toast.makeText(applicationContext,"Season Updated",Toast.LENGTH_SHORT).show()
								}
						)
						.show()
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
	 * Retrieves more information on the current season of show
	 * @param [id] id of current chosen tv show
	 * @return TVSeasonInfo object
	 */
	fun getSeasonInfo(id: Int):TVSeasonInfo{
		var season: TVSeasonBasic = Gson().fromJson(intent.getStringExtra("Season"))
		return Async!!.getSeasonAsync(season!!.seasonNumber,id)
	}

	/**
	 * Sets up variable and calls method that handles View elements
	 */
	fun setup(){
		Async = AsyncTasker(applicationContext)
		id = intent.extras.get("TVID") as Int
		user = Gson().fromJson(intent.getStringExtra("User"))
		setView(getSeasonInfo(id!!))
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
