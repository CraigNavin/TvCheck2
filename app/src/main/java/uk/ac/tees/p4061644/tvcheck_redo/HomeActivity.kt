package uk.ac.tees.p4061644.tvcheck_redo

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.layout_bottom_navigation_view.*
import uk.ac.tees.p4061644.tvcheck_redo.Adapters.RecyclerViewAdapter
import uk.ac.tees.p4061644.tvcheck_redo.models.Show
import uk.ac.tees.p4061644.tvcheck_redo.models.User
import uk.ac.tees.p4061644.tvcheck_redo.utils.AsyncTasker
import uk.ac.tees.p4061644.tvcheck_redo.utils.BottomNavigationBarHelper
import uk.ac.tees.p4061644.tvcheck_redo.utils.DatabaseHandler
import java.util.*

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
		setupBottomnavigatioView()
	}

	inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

	fun setup(){
		Async = AsyncTasker(applicationContext)
		dbh = DatabaseHandler(applicationContext)
		user = gson.fromJson(intent.getStringExtra("User"))
		var poplayoutManager = LinearLayoutManager (this,LinearLayoutManager.HORIZONTAL,false)
		var toplayoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
		recycle_popular.apply {
			layoutManager = poplayoutManager
			adapter = RecyclerViewAdapter(applicationContext,Async!!.fillhome(1,null)!!)
		}
		var show = getRandomShowFromLists()
		top_rated_TV.text = "Because you watched " + Async!!.getShowBasicAsync(show.id).name

		recycle_top_rated.apply {
			layoutManager = toplayoutManager
			adapter= RecyclerViewAdapter(applicationContext,Async!!.fillhome(3,show.id)!!)
		}
	}

	fun getRandomShowFromLists():Show{
		var listindex = Random().nextInt(user!!.list!!.size)
		var showindex = Random().nextInt(user!!.list!![listindex].list!!.size)
		return user!!.list!![listindex].list!![showindex]
	}



	private fun setupBottomnavigatioView(){
		Log.d(TAG,"setupBottomNavigationView")
		BottomNavigationBarHelper.setupBottomNavigationBar(bottomNavViewBar)
		BottomNavigationBarHelper.enableNavigation(applicationContext, bottomNavViewBar,intent.getStringExtra("User"))
		val menu: Menu? = bottomNavViewBar.menu
		val menuI: MenuItem? = menu?.getItem(activity_Num)
		menuI?.isChecked = true
	}

	override fun onDestroy() {
		super.onDestroy()
		Runtime.getRuntime().gc()
	}
}
