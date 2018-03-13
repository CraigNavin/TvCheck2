package uk.ac.tees.p4061644.tvcheck_redo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.omertron.themoviedbapi.model.tv.TVEpisodeInfo
import com.squareup.picasso.Picasso
import uk.ac.tees.p4061644.tvcheck_redo.utils.AsyncTasker
import uk.ac.tees.p4061644.tvcheck_redo.utils.BottomNavigationBarHelper

class EpisodeActivity : AppCompatActivity() {

	private val TAG: String = "SeasonActivity"
	private var navbar: BottomNavigationViewEx? = null
	private val activity_Num: Int = 1
	private var EpisodeView: TextView? = null
	private var OverViewView: TextView? = null
	private var posterView: ImageView? = null


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_episode)
		setup()
	}

	fun setView(episode: TVEpisodeInfo){
		EpisodeView!!.text = episode.name
		OverViewView!!.text = episode.overview
		Picasso.with(applicationContext).load(applicationContext.getString(R.string.base_address_original) + episode.stillPath)
				.placeholder(R.drawable.ic_default_search_image)
				.fit()
				.into(posterView)


	}

	fun setup(){
		EpisodeView = findViewById(R.id.EPName_TV) as TextView
		OverViewView = findViewById(R.id.Overview_TV) as TextView
		posterView = findViewById(R.id.PosterView) as ImageView
		navbar = findViewById(R.id.bottomNavViewBar) as BottomNavigationViewEx
		setupBottomnavigatioView()
		var episode: TVEpisodeInfo = Gson().fromJson(intent.getStringExtra("Episode"))
		setView(episode)
	}

	inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

	private fun setupBottomnavigatioView(){
		Log.d(TAG,"setupBottomNavigationView")
		BottomNavigationBarHelper.setupBottomNavigationBar(navbar)
		BottomNavigationBarHelper.enableNavigation(applicationContext, navbar,intent.getStringExtra("User"))
		val menu: Menu? = navbar?.menu
		val menuI: MenuItem? = menu?.getItem(activity_Num)
		menuI?.isChecked = true
	}

}