package uk.ac.tees.p4061644.tvcheck_redo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.omertron.themoviedbapi.model.tv.TVBasic
import com.omertron.themoviedbapi.model.tv.TVInfo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.layout_bottom_navigation_view.*
import kotlinx.android.synthetic.main.recyclerview_item.*
import uk.ac.tees.p4061644.tvcheck_redo.Adapters.RecyclerHomeViewAdapter
import uk.ac.tees.p4061644.tvcheck_redo.models.Show
import uk.ac.tees.p4061644.tvcheck_redo.models.User
import uk.ac.tees.p4061644.tvcheck_redo.utils.AsyncTasker
import uk.ac.tees.p4061644.tvcheck_redo.utils.BottomNavigationBarHelper
import uk.ac.tees.p4061644.tvcheck_redo.utils.DatabaseHandler
import java.util.*
import javax.crypto.spec.GCMParameterSpec
import kotlin.collections.ArrayList

class HomeActivity : Activity(){


	private var TAG: String = "HomeActivity"
	private val activity_Num: Int = 0
	private var Async : AsyncTasker? = null
	private var dbh : DatabaseHandler? = null
	private var gson = Gson()
	private var user : User? = null


	override fun onCreate(savedInstanceState: Bundle?) {
		Log.d(TAG,"OnCreate")
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_home)
		setup()

	}

	inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

	/**
	 * Sets up variable and calls method that handles View elements
	 */
	fun setup(){
		Async = AsyncTasker(applicationContext)
		dbh = DatabaseHandler(applicationContext)
		user = gson.fromJson(intent.getStringExtra("User"))
		Log.d(TAG,Gson().toJson(user))
		setView()
		setupBottomnavigatioView()

	}

	/**
	 * Sets up view elements with top rated,Popular shows. As well as a similar show to ones in the users lists
	 */
	fun setView(){
		var poplayoutManager = LinearLayoutManager (this,LinearLayoutManager.HORIZONTAL,false)
		var toplayoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
		var popular = Async!!.fillhome(1,null)!!
		var topRated = Async!!.fillhome(2,null)!!
		var tvInfo : TVInfo? = null
		var similar : ArrayList<TVBasic>? = null
		if (!user!!.list!!.any{ it.list!!.count() == 0}){
			var show :Show = getRandomShowFromLists()
			similar = Async!!.fillhome(3,show.id)!!
			tvInfo = Async!!.getShowInfoAsync(similar[Random().nextInt(similar.size)].id)
			Similar_txt.text = "Because you followed " + Async!!.getShowBasicAsync(show.id).name
			Name_txt.text = tvInfo.name
			OverView_txt.text = tvInfo.overview
			Picasso.with(applicationContext).load(getString(R.string.base_address_w185) + tvInfo.posterPath)
					.placeholder(R.drawable.ic_default_search_image)
					.into(img_view)
			relLayout2.setOnClickListener {
				val intent = Intent(applicationContext,ShowActivity::class.java)
				intent.putExtra("Show",Gson().toJson(tvInfo))
				intent.putExtra("User",Gson().toJson(user))
				applicationContext.startActivity(intent)
			}

		}else{
			HideSimilar()

		}

		recycle_popular.apply {
			layoutManager = poplayoutManager
			adapter = RecyclerHomeViewAdapter(applicationContext,popular,user!!)
		}

		recycle_top_rated.apply {
			layoutManager = toplayoutManager
			adapter= RecyclerHomeViewAdapter(applicationContext,topRated,user!!)
		}
	}

	fun HideSimilar(){
		img_view.visibility = View.GONE
		Name_txt.visibility = View.GONE
		OverView_txt.visibility = View.GONE
		Similar_txt.visibility = View.GONE
		emptyLists_txt.visibility = View.VISIBLE
	}

	/**
	 * Retrieves a random show from the users lists
	 * @return Show object to be used to get similar Shows from API
	 */
	fun getRandomShowFromLists():Show{
		var listindex = Random().nextInt(user!!.list!!.size)
		var showindex = Random().nextInt(user!!.list!![listindex].list!!.size)
		return user!!.list!![listindex].list!![showindex]
	}

	/**
	 * Instantiates the bottom navigation view and sets the menu values to the navigation bar
	 */
	private fun setupBottomnavigatioView(){
		Log.d(TAG,"setupBottomNavigationView")
		BottomNavigationBarHelper.setupBottomNavigationBar(bottomNavViewBar)
		BottomNavigationBarHelper.enableNavigation(applicationContext, bottomNavViewBar,Gson().toJson(user))
		val menu: Menu? = bottomNavViewBar.menu
		val menuI: MenuItem? = menu?.getItem(activity_Num)
		menuI?.isChecked = true
	}

	/**
	 * Overridden onDestroy function runs garbage collection to help keep RAM usage as low as possible
	 */
	override fun onDestroy() {
		super.onDestroy()
		Runtime.getRuntime().gc()

	}
}
