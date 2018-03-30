package uk.ac.tees.p4061644.tvcheck_redo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.omertron.themoviedbapi.model.tv.TVEpisodeInfo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_episode.*
import kotlinx.android.synthetic.main.layout_bottom_navigation_view.*
import uk.ac.tees.p4061644.tvcheck_redo.Adapters.RecyclerPeopleViewAdapter
import uk.ac.tees.p4061644.tvcheck_redo.models.Show
import uk.ac.tees.p4061644.tvcheck_redo.models.User
import uk.ac.tees.p4061644.tvcheck_redo.utils.BottomNavigationBarHelper
import uk.ac.tees.p4061644.tvcheck_redo.utils.DatabaseHandler

class EpisodeActivity : AppCompatActivity() {

	private val TAG: String = "EpisodeActivity"
	private val activity_Num: Int = 1
	private var episode: TVEpisodeInfo? = null
	private var user: User? = null


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_episode)
		setup()
	}

	fun getShowFromLists(): Show? {
		var listolists = user!!.list
		var TVID = intent.extras.get("TVID")
		listolists!!.forEach { it.list!!.forEach { if (it.id == TVID){ return it } }}
		return null
	}

	fun setView(){
		EPName_TV!!.text = episode!!.name
		var bool: Boolean = false
		var personlayoutManager = LinearLayoutManager (this, LinearLayoutManager.HORIZONTAL,false)
		person_recycler.apply {
			layoutManager = personlayoutManager
			adapter = RecyclerPeopleViewAdapter(applicationContext,ArrayList(episode!!.credits.cast))
			visibility = View.VISIBLE
		}
		if (getShowFromLists() != null){
			bool = getShowFromLists()!!.seasons!![episode!!.seasonNumber - 1].episodes[episode!!.episodeNumber - 1].watched
			watched_txt!!.text = "From List"
		}
		watched_box!!.isChecked = bool

		if (episode!!.overview == null || episode!!.overview == ""){
			Overview_TV!!.text = "No Overview"
		}else{
			Overview_TV!!.text = episode!!.overview
		}
		Picasso.with(applicationContext).load(applicationContext.getString(R.string.base_address_original) + episode!!.stillPath)
				.placeholder(R.drawable.ic_default_search_image)
				.fit()
				.into(PosterView)
		watched_box!!.setOnCheckedChangeListener { buttonView, isChecked ->
			getShowFromLists()!!.seasons!![episode!!.seasonNumber -1].episodes[episode!!.episodeNumber -1].watched = isChecked
			DatabaseHandler(applicationContext).update(user!!)

			Toast.makeText(applicationContext,"Show Removed", Toast.LENGTH_SHORT).show()
		}



	}

	fun setup(){
		Log.d(TAG,intent.extras.get("TVID").toString())
		user = Gson().fromJson(intent.getStringExtra("User"))
		setupBottomnavigatioView()
		episode = Gson().fromJson(intent.getStringExtra("Episode"))
		setView()
	}

	inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

	private fun setupBottomnavigatioView(){
		Log.d(TAG,"setupBottomNavigationView")
		BottomNavigationBarHelper.setupBottomNavigationBar(bottomNavViewBar)
		BottomNavigationBarHelper.enableNavigation(applicationContext, bottomNavViewBar,Gson().toJson(user))
		val menu: Menu? = bottomNavViewBar?.menu
		val menuI: MenuItem? = menu?.getItem(activity_Num)
		menuI?.isChecked = true
	}

	override fun onDestroy() {
		//android.os.Process.killProcess(android.os.Process.myPid());

		super.onDestroy()
		Runtime.getRuntime().gc()

	}

}
