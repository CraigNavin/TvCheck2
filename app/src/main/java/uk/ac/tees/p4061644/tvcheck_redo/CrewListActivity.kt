package uk.ac.tees.p4061644.tvcheck_redo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.omertron.themoviedbapi.model.credits.MediaCreditCast
import kotlinx.android.synthetic.main.activity_crew_list.*
import kotlinx.android.synthetic.main.layout_bottom_navigation_view.*
import uk.ac.tees.p4061644.tvcheck_redo.Adapters.CastAdapter
import uk.ac.tees.p4061644.tvcheck_redo.models.User
import uk.ac.tees.p4061644.tvcheck_redo.utils.BottomNavigationBarHelper

class CrewListActivity : AppCompatActivity() {

	private val TAG: String = "CrewListActivity"
	private val activity_Num: Int = 1
	private var user: User? = null
	private var castList: ArrayList<MediaCreditCast>? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_crew_list)
		setup()
	}

	/**
	 * Sets up variables and calls method that handles View elements
	 */
	fun setup(){
		user = Gson().fromJson(intent.getStringExtra("User"))
		castList = Gson().fromJson(intent.getStringExtra("CastList"))
		setView()
		setupBottomnavigatioView()
	}

	/**
	 * Populates the elements of the view with data of list of cast
	 */
	fun setView(){
		val adapter = CastAdapter(this,castList!!.toList(),applicationContext)
		val castOf  = "Core cast of " + intent.getStringExtra("showName")
		castof_txt.text = castOf
		castList_lv.adapter = adapter
		castList_lv.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
			load_pgrbar.visibility = View.VISIBLE
			val item = parent.getItemAtPosition(position) as String
			val intent = Intent(applicationContext,CrewActivity::class.java)
			intent.putExtra("Member",item)
			intent.putExtra("User", Gson().toJson(user))
			intent.putExtra("CastID",castList!![position].id.toString())
			intent.putExtra("PicPath",castList!![position].artworkPath)
			applicationContext.startActivity(intent)
			load_pgrbar.visibility = View.GONE
		}
	}



	inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

	/**
	 * Instantiates the bottom navigation view and sets the menu values to the navigation bar
	 */
	private fun setupBottomnavigatioView(){
		Log.d(TAG,"setupBottomNavigationView")
		BottomNavigationBarHelper.setupBottomNavigationBar(bottomNavViewBar)
		BottomNavigationBarHelper.enableNavigation(applicationContext, bottomNavViewBar, Gson().toJson(user),3,this)
		val menu: Menu? = bottomNavViewBar.menu
		val menuI: MenuItem? = menu?.getItem(activity_Num)
		menuI?.isChecked = true
	}
}
