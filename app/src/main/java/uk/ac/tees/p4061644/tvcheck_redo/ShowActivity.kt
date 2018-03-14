package uk.ac.tees.p4061644.tvcheck_redo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.omertron.themoviedbapi.model.tv.TVBasic
import com.omertron.themoviedbapi.model.tv.TVSeasonBasic
import com.squareup.picasso.Picasso
import uk.ac.tees.p4061644.tvcheck_redo.models.Season
import uk.ac.tees.p4061644.tvcheck_redo.models.User
import uk.ac.tees.p4061644.tvcheck_redo.utils.AsyncTasker
import uk.ac.tees.p4061644.tvcheck_redo.utils.BottomNavigationBarHelper
import uk.ac.tees.p4061644.tvcheck_redo.utils.SeasonEpisodeListAdapter
import java.lang.reflect.Type

class ShowActivity : AppCompatActivity() {

	private val TAG: String = "ShowActivity"
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
		setView()
	}

	fun setView(){
		var basic : TVBasic = Gson().fromJson(intent.getStringExtra("Show"))
		var show = Async!!.getShowAsync(basic.id)

		NameView!!.text = show.name
		OverViewView!!.text = show.overview
		RatingView!!.text = show.voteAverage.toString()
		Picasso.with(applicationContext).load(applicationContext.getString(R.string.base_address_w185) + show.posterPath)
				.placeholder(R.drawable.ic_default_search_image)
				.into(posterView)
		setupBottomnavigatioView()
		navbar!!.bringChildToFront(findViewById(R.id.bottomNavViewBar))
		var seasons = show.seasons
		var adapter = SeasonEpisodeListAdapter(this,seasons,null,applicationContext)
		listView!!.adapter = adapter
		var user: User = Gson().fromJson(intent.getStringExtra("User"))
		listView!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
			val item = parent.getItemAtPosition(position) as String
			val season : TVSeasonBasic = Gson().fromJson(item)
			val intent = Intent(applicationContext,SeasonActivity::class.java)//activity_Num 1
			intent.putExtra("Season",item)
			intent.putExtra("User",Gson().toJson(user))
			intent.putExtra("TVID",show.id)
			//Toast.makeText(applicationContext,show.id.toString(),Toast.LENGTH_SHORT).show()
			//Toast.makeText(applicationContext,season.seasonNumber.toString(),Toast.LENGTH_SHORT).show()
			applicationContext.startActivity(intent)
		}

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

	override fun onDestroy() {
		//android.os.Process.killProcess(android.os.Process.myPid());

		super.onDestroy()
		Runtime.getRuntime().gc()

	}
}
