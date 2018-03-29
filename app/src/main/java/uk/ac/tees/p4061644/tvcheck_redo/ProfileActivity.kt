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

class ProfileActivity : AppCompatActivity() {

	private val TAG = "ProfileActivity"
	private val activity_Num: Int = 2
	private var user : User? = null
	private var gson = Gson()

	override fun onCreate(savedInstanceState: Bundle?) {
		Log.d(TAG, "OnCreate")
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_profile)
		setupBottomnavigatioView()
		setup()
		Log.d("USERGSONCHECK",user!!.UserID)

	}

	fun setup(){
		user = gson.fromJson(intent.getStringExtra("User"))
		var name_list = user!!.getListNames()
		//user!!.list!!.forEach { name_list.add(it.name) }

		List_LV.adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1,name_list)
		List_LV.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
			val listName = parent.getItemAtPosition(position) as String
			val intent = Intent(applicationContext,SearchActivity::class.java)//activity_Num 1
			intent.putExtra("List",listName)
			intent.putExtra("User",Gson().toJson(user))
			applicationContext.startActivity(intent)
		}
	}


	inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

	private fun setupBottomnavigatioView(){
		Log.d(TAG,"setupBottomNavigationView")
		BottomNavigationBarHelper.setupBottomNavigationBar(bottomNavViewBar)
		BottomNavigationBarHelper.enableNavigation(applicationContext, bottomNavViewBar,intent.getStringExtra("User"))
		val menu: Menu? = bottomNavViewBar.menu
		val menuI: MenuItem? = menu?.getItem(activity_Num)
		menuI?.setChecked(true)
	}
}
