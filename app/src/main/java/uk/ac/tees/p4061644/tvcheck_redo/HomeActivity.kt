package uk.ac.tees.p4061644.tvcheck_redo

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.omertron.themoviedbapi.model.tv.TVBasic
import com.omertron.themoviedbapi.model.tv.TVEpisodeInfo
import com.omertron.themoviedbapi.model.tv.TVInfo
import com.omertron.themoviedbapi.model.tv.TVSeasonInfo
import uk.ac.tees.p4061644.tvcheck_redo.models.ListModel
import uk.ac.tees.p4061644.tvcheck_redo.models.Show
import uk.ac.tees.p4061644.tvcheck_redo.models.User
import uk.ac.tees.p4061644.tvcheck_redo.utils.AsyncTasker
import uk.ac.tees.p4061644.tvcheck_redo.utils.BottomNavigationBarHelper
import uk.ac.tees.p4061644.tvcheck_redo.utils.Converter
import uk.ac.tees.p4061644.tvcheck_redo.utils.DatabaseHandler

class HomeActivity : Activity(){

	private var navbar: BottomNavigationViewEx? = null
	private var TAG: String = "HomeActivity"
	private val activity_Num: Int = 0
	private var Async : AsyncTasker? = null
	private var dbh : DatabaseHandler? = null


	override fun onCreate(savedInstanceState: Bundle?) {
		Log.d(TAG,"OnCreate")
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_home)
		navbar = findViewById(R.id.bottomNavViewBar) as BottomNavigationViewEx
		setupBottomnavigatioView()
		Async = AsyncTasker(applicationContext)
		dbh = DatabaseHandler(applicationContext)
		var user = dbh!!.retrievefirst(intent.getStringExtra("UID"))
		listCheck(user!!)

		Log.d(TAG,user.checkListContainsShow("Altered Carbon","NAME").toString())
		Log.d(TAG,user.checkListContainsShow("Altered Carbon","NAME2").toString())
		Log.d(TAG,user.getShow(user.getList("NAME2")!!.list!!,"Altered Carbon").toString())

	}

	fun listCheck(user: User){

		var strings: ArrayList<String> = ArrayList()
		var string: String = ""
		var int = 0
		user!!.list!!.forEach {
			int++
			string = it.name
			it.list!!.forEach {
				string = string +" "  + it.name + " " + int.toString()
				strings.add(string)
				string = ""
			}
		}
		for (String in strings){
			Log.d("RETRIEVE CHECK",String)
		}
	}

	fun updateCheck(user: User){
		var rl: List<TVBasic>? = Async!!.searchShows("altered Carbon")
		var con = Converter(applicationContext)
		var show1: TVInfo = Async!!.getShowAsync(rl!![0].id)
		var conShow: Show = con.convert(show1)

		user.list!![0].list!!.add(conShow)
		dbh!!.update(user)
		listCheck(user)
	}


	private fun testBlock(){

		var rl: List<TVBasic>? = Async!!.searchShows("how i met")
		var con = Converter(applicationContext)

		var show1: TVInfo? = Async!!.getShowAsync(rl!![0].id)
		var season: TVSeasonInfo? = Async!!.getSeasonAsync(show1!!.seasons[8].seasonNumber,show1.id)
		var episode: TVEpisodeInfo? = Async!!.getEpisodeAsync(0,show1.id,season!!.seasonNumber)
		Log.d("SHOW",show1.toString())
		Log.d("SEASON",season.toString())
		Log.d("EPISODE",episode.toString())

		var conShow: Show = con.convert(show1)

		val user = dbh!!.retrievefirst(intent.getStringExtra("UID"))
		var arlist1 = ListModel("NAME")
		var arlist2 = ListModel("NAME2")
		arlist1.list!!.add(conShow)

		Log.d("HIMYM","HIMYM")
		rl = Async!!.searchShows("Punisher")
		show1 = Async!!.getShowAsync(rl!![0].id)
		conShow = con.convert(show1)
		arlist2.list!!.add(conShow)
		arlist1.list!!.add(conShow)
		user!!.list!!.add(arlist2)

		user!!.list!!.add(arlist1)
		dbh!!.update(user)
		//dbh!!.update(user)
		Log.d("PUNISHER","PUNISHER")


	}

	private fun setupBottomnavigatioView(){
		Log.d(TAG,"setupBottomNavigationView")
		BottomNavigationBarHelper.setupBottomNavigationBar(navbar)
		BottomNavigationBarHelper.enableNavigation(applicationContext, navbar)
		val menu: Menu? = navbar?.menu
		val menuI: MenuItem? = menu?.getItem(activity_Num)
		menuI?.isChecked = true
	}
}
