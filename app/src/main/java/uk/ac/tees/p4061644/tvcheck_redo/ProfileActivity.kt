package uk.ac.tees.p4061644.tvcheck_redo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import uk.ac.tees.p4061644.tvcheck_redo.models.User
import uk.ac.tees.p4061644.tvcheck_redo.utils.BottomNavigationBarHelper
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.layout_bottom_navigation_view.*
import uk.ac.tees.p4061644.tvcheck_redo.utils.DatabaseHandler

class ProfileActivity : AppCompatActivity() {

	private val TAG = "ProfileActivity"
	private val activity_Num: Int = 2
	private var user : User? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		Log.d(TAG, "OnCreate")
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_profile)
		setup()

		Log.d("USERGSONCHECK",user!!.UserID)
	}

	/**
	 * Sets up variable and calls method that handles View elements
	 */
	fun setup(){
		user = Gson().fromJson(intent.getStringExtra("User"))
		user = DatabaseHandler(applicationContext).retrievefirst(user!!.UserID)
		setView()
		setupBottomnavigatioView()
	}

	/**
	 * populates the view elements on this activity with the correct information
	 */
	fun setView(){
		var name_list = user!!.getListNames()

		List_LV.adapter = ArrayAdapter<String>(applicationContext,R.layout.mytextview,name_list)
		List_LV.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
			val listName = parent.getItemAtPosition(position) as String
			val intent = Intent(applicationContext,SearchActivity::class.java)//activity_Num 1
			intent.putExtra("List",listName)
			intent.putExtra("User",Gson().toJson(user))
			applicationContext.startActivity(intent)
		}

		newList_btn.setOnClickListener {
			val intent = Intent(applicationContext,NewListActivity::class.java)
			intent.putExtra("User",Gson().toJson(user))
			applicationContext.startActivity(intent)

		}
	}


	inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

	/**
	 * Instantiates the bottom navigation view and sets the menu values to the navigation bar
	 */
	private fun setupBottomnavigatioView(){
		Log.d(TAG,"setupBottomNavigationView")
		BottomNavigationBarHelper.setupBottomNavigationBar(bottomNavViewBar)
		BottomNavigationBarHelper.enableNavigation(applicationContext, bottomNavViewBar,Gson().toJson(user))
		val menu: Menu? = bottomNavViewBar.menu
		val menuI: MenuItem? = menu?.getItem(activity_Num)
		menuI?.setChecked(true)
	}
}
