package uk.ac.tees.p4061644.tvcheck_redo

import android.content.Intent
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
		val TVID = intent.extras.get("TVID")
		user!!.list!!.forEach { it.list!!.forEach { if (it.id == TVID){ return it } }}
		return null
	}

	/**
	 * Sets up view elements with all of the episode information
	 */
	fun setView(){
		if (episode != null){
			/* Sets up all text fields on the screen */
			EPName_TV!!.text = episode!!.name
			airDate_tv.text = episode!!.airDate
			val voteString = episode!!.voteAverage.toString() + "(" + episode!!.voteCount + ")"
			Vote_tv.text = voteString
			var seasonNum :Int = 0
			if (getShowFromLists() != null){
				if(getShowFromLists()!!.seasons!![0].seasonNumber == 0){
					seasonNum = episode!!.seasonNumber
				}else{
					seasonNum = episode!!.seasonNumber -1
				}
				watched_box!!.isChecked = getShowFromLists()!!.seasons!![seasonNum].episodes[episode!!.episodeNumber - 1].watched
			}

			if (episode!!.overview.isNullOrEmpty()){
				Bio_TV!!.text = "No Overview"
			}else {
				Bio_TV!!.text = episode!!.overview
				Bio_TV!!.setOnClickListener {
					val intent = Intent(applicationContext, ReadMoreActivity::class.java)
					intent.putExtra("ReadMore", episode!!.overview)
					startActivity(intent)
				}
			}
			/* Retrives the image from path provided and inserts it in to imageView */
			Picasso.with(applicationContext).load(applicationContext.getString(R.string.base_address_original) + episode!!.stillPath)
					.placeholder(R.drawable.ic_default_search_image)
					.fit()
					.into(PosterView)

			/* Sets onCheckedChangeListener to watched checkbox */
			setupCheckbox(seasonNum)

		}else{
			EPName_TV.text = "Invalid Episode"
		}
	}

	/**
	 * Sets a OnCheckedChangeListener to the activities checkbox and handles what happens when checkBox value changes
	 * @param [seasonNum] Season num which will be used to gather episode inforamtion
	 */
	fun setupCheckbox(seasonNum: Int){
		watched_box!!.setOnCheckedChangeListener { _, isChecked ->
			Log.d(TAG,id.toString())
			if (user!!.inLists(id)){
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
		BottomNavigationBarHelper.enableNavigation(applicationContext, bottomNavViewBar,Gson().toJson(user),3,this)
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
