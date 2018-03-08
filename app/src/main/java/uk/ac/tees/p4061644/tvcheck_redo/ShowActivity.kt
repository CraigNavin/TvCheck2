package uk.ac.tees.p4061644.tvcheck_redo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.omertron.themoviedbapi.model.tv.TVBasic
import com.omertron.themoviedbapi.model.tv.TVInfo
import com.squareup.picasso.Picasso
import org.w3c.dom.Text
import uk.ac.tees.p4061644.tvcheck_redo.models.Show
import uk.ac.tees.p4061644.tvcheck_redo.utils.AsyncTasker
import uk.ac.tees.p4061644.tvcheck_redo.utils.BottomNavigationBarHelper
import uk.ac.tees.p4061644.tvcheck_redo.utils.SeasonEpisodeListAdapter

class ShowActivity : AppCompatActivity() {

	private val TAG: String = "SearchActivity"
	private var Async : AsyncTasker? = null
	private var navbar: BottomNavigationViewEx? = null
	private val activity_Num: Int = 1
	private var NameView: TextView? = null
	private var OverViewView: TextView? = null
	private var RatingView: TextView? = null
	private var posterView: ImageView? = null
	private var listView: ListView? = null


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_show)

		setup()
		var basic : TVBasic = Gson().fromJson(intent.getStringExtra("Show"))
		var show = Async!!.getShowAsync(basic.id)
		NameView!!.text = show.name
		OverViewView!!.text = show.overview
		RatingView!!.text = show.voteAverage.toString()
		Picasso.with(applicationContext).load(applicationContext.getString(R.string.base_address) + show.posterPath)
				.placeholder(R.drawable.ic_default_search_image)
				.into(posterView)
		listView = findViewById(R.id.season_list) as ListView
		setupBottomnavigatioView()
		navbar!!.bringChildToFront(findViewById(R.id.bottomNavViewBar))
		var seasons = show.seasons
		var adapter = SeasonEpisodeListAdapter(this,seasons,null,applicationContext)
		listView!!.adapter = adapter
		adapter.notifyDataSetChanged()
	}

	fun setup(){
		NameView = findViewById(R.id.ShowName_TV) as TextView
		OverViewView = findViewById(R.id.Overview_TV) as TextView
		RatingView = findViewById(R.id.Rating_TV) as TextView
		listView = findViewById(R.id.season_list) as ListView
		posterView = findViewById(R.id.PosterView) as ImageView
		Async = AsyncTasker(applicationContext)
		navbar = findViewById(R.id.bottomNavViewBar) as BottomNavigationViewEx
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
