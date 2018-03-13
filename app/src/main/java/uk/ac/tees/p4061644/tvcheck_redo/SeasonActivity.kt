package uk.ac.tees.p4061644.tvcheck_redo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.omertron.themoviedbapi.model.tv.TVEpisodeInfo
import com.omertron.themoviedbapi.model.tv.TVSeasonBasic
import com.omertron.themoviedbapi.model.tv.TVSeasonInfo
import com.squareup.picasso.Picasso
import uk.ac.tees.p4061644.tvcheck_redo.models.User
import uk.ac.tees.p4061644.tvcheck_redo.utils.AsyncTasker
import uk.ac.tees.p4061644.tvcheck_redo.utils.BottomNavigationBarHelper
import uk.ac.tees.p4061644.tvcheck_redo.utils.SeasonEpisodeListAdapter

class SeasonActivity : AppCompatActivity() {

	private val TAG: String = "SeasonActivity"
	private var Async : AsyncTasker? = null
	private var navbar: BottomNavigationViewEx? = null
	private val activity_Num: Int = 1
	private var seasonView: TextView? = null
	private var OverViewView: TextView? = null
	private var RatingView: TextView? = null
	private var posterView: ImageView? = null
	private var listView: ListView? = null


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_season)
		setup()
	}

	fun setView(season: TVSeasonInfo){
		var seasonNum = "Season " + season.seasonNumber
		seasonView!!.text = seasonNum
		OverViewView!!.text = season.overview
		Picasso.with(applicationContext).load(applicationContext.getString(R.string.base_address_w185) + season.posterPath)
				.placeholder(R.drawable.ic_default_search_image)
				.into(posterView)
		setupBottomnavigatioView()
		navbar!!.bringChildToFront(findViewById(R.id.bottomNavViewBar))
		var adapter = SeasonEpisodeListAdapter(this,null,season.episodes,applicationContext)
		listView!!.adapter = adapter
		var user: User = Gson().fromJson(intent.getStringExtra("User"))
		listView!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
			val item = parent.getItemAtPosition(position) as String
			val episode: TVEpisodeInfo = Gson().fromJson(item)
			Toast.makeText(applicationContext,episode.name,Toast.LENGTH_SHORT).show()
			val intent = Intent(applicationContext,EpisodeActivity::class.java)//activity_Num 1
			intent.putExtra("Episode",item)
			intent.putExtra("User",Gson().toJson(user))
			applicationContext.startActivity(intent)
		}
	}

	fun getSeasonInfo(id: Int):TVSeasonInfo{
		var season: TVSeasonBasic = Gson().fromJson(intent.getStringExtra("Season"))
		return Async!!.getSeasonAsync(season!!.seasonNumber,id)
	}

	fun setup(){
		seasonView = findViewById(R.id.SeasonNum_TV) as TextView
		OverViewView = findViewById(R.id.Overview_TV) as TextView
		listView = findViewById(R.id.episodes_list) as ListView
		posterView = findViewById(R.id.PosterView) as ImageView
		Async = AsyncTasker(applicationContext)
		navbar = findViewById(R.id.bottomNavViewBar) as BottomNavigationViewEx

		var id = intent.extras.get("TVID") as Int

		setView(getSeasonInfo(id))
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
