package uk.ac.tees.p4061644.tvcheck_redo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.omertron.themoviedbapi.model.tv.TVBasic
import com.omertron.themoviedbapi.model.tv.TVInfo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.layout_bottom_navigation_view.*
import uk.ac.tees.p4061644.tvcheck_redo.Adapters.RecyclerHomeViewAdapter
import uk.ac.tees.p4061644.tvcheck_redo.models.Show
import uk.ac.tees.p4061644.tvcheck_redo.models.User
import uk.ac.tees.p4061644.tvcheck_redo.utils.AsyncTasker
import uk.ac.tees.p4061644.tvcheck_redo.utils.BottomNavigationBarHelper
import uk.ac.tees.p4061644.tvcheck_redo.utils.DatabaseHandler
import java.util.*
import kotlin.collections.ArrayList

class HomeActivity : Activity(),RecyclerHomeViewAdapter.OnItemClickListener{


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
		setupBottomnavigatioView()
	}

	inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

	fun setup(){
		Async = AsyncTasker(applicationContext)
		dbh = DatabaseHandler(applicationContext)
		user = gson.fromJson(intent.getStringExtra("User"))
		var poplayoutManager = LinearLayoutManager (this,LinearLayoutManager.HORIZONTAL,false)
		var toplayoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
		var popular = Async!!.fillhome(1,null)!!
		var show :Show? = null
		var tvInfo : TVInfo? = null
		var similar : ArrayList<TVBasic>? = null
		if (!user!!.list!!.any{ it.list!!.count() == 0}){
			show = getRandomShowFromLists()
			similar = Async!!.fillhome(3,show.id)!!
			tvInfo = Async!!.getShowInfoAsync(similar[Random().nextInt(similar.size)].id)
			Similar_txt.text = "Because you watched " + Async!!.getShowBasicAsync(show.id).name
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
			top_rated_TV.text = "Top Rated"
			similar = Async!!.fillhome(2,null)!!
		}

		recycle_popular.apply {
			layoutManager = poplayoutManager
			adapter = RecyclerHomeViewAdapter(applicationContext,popular,this@HomeActivity)
		}

		recycle_top_rated.apply {
			layoutManager = toplayoutManager
			adapter= RecyclerHomeViewAdapter(applicationContext,similar!!,this@HomeActivity)
		}
	}

	fun getRandomShowFromLists():Show{
		var listindex = Random().nextInt(user!!.list!!.size)
		var showindex = Random().nextInt(user!!.list!![listindex].list!!.size)
		return user!!.list!![listindex].list!![showindex]
	}

	override fun onItemClick(position: Int) {
		val show = Async!!.fillhome(1,null)!![position]
		val showString = gson.toJson(show)
		val intent = Intent(applicationContext,ShowActivity::class.java)//activity_Num 1
		intent.putExtra("Show",showString)
		intent.putExtra("User",Gson().toJson(user))
		applicationContext.startActivity(intent)
	}

	private fun setupBottomnavigatioView(){
		Log.d(TAG,"setupBottomNavigationView")
		BottomNavigationBarHelper.setupBottomNavigationBar(bottomNavViewBar)
		BottomNavigationBarHelper.enableNavigation(applicationContext, bottomNavViewBar,Gson().toJson(user))
		val menu: Menu? = bottomNavViewBar.menu
		val menuI: MenuItem? = menu?.getItem(activity_Num)
		menuI?.isChecked = true
	}

	override fun onDestroy() {
		super.onDestroy()
		Runtime.getRuntime().gc()
	}
}
