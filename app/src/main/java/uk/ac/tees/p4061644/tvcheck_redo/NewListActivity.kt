package uk.ac.tees.p4061644.tvcheck_redo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_new_list.*
import kotlinx.android.synthetic.main.layout_bottom_navigation_view.*
import uk.ac.tees.p4061644.tvcheck_redo.models.ListModel
import uk.ac.tees.p4061644.tvcheck_redo.models.User
import uk.ac.tees.p4061644.tvcheck_redo.utils.BottomNavigationBarHelper
import uk.ac.tees.p4061644.tvcheck_redo.utils.DatabaseHandler

class NewListActivity : AppCompatActivity() {

	private val TAG: String = "NewListActivity"
	private val activity_Num: Int = 2
	private var user : User? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_new_list)
		setup()
	}


	inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)


	/**
	 * Sets up variable and calls method that handles View elements
	 */
	fun setup(){
		user = Gson().fromJson(intent.getStringExtra("User"))
		setView()
		setupBottomnavigatioView()

	}

	/**
	 * Sets onClickListeners to buttons and handles data validation when create button pressed
	 */
	fun setView(){
		create_btn.setOnClickListener {
			if (listName_txt.text.toString().isNullOrEmpty()){
				listName_txt.error = "List name is cannot be empty"
				listName_txt.requestFocus()
			}else if(user!!.checkNameTaken(listName_txt.text.toString())){
				listName_txt.text.clear()
				Toast.makeText(applicationContext,"List with this name already exists. Please choose another",Toast.LENGTH_SHORT).show()
				listName_txt.requestFocus()
			}else{
				user!!.list!!.add(ListModel(listName_txt.text.toString()))
				DatabaseHandler(applicationContext).update(user!!)
				val intent = Intent(applicationContext,ProfileActivity::class.java)
				intent.putExtra("User",Gson().toJson(user))
				applicationContext.startActivity(intent)

			}
		}

		cancel_btn.setOnClickListener {
			val intent = Intent(applicationContext,ProfileActivity::class.java)
			intent.putExtra("User",Gson().toJson(user))
			applicationContext.startActivity(intent)

		}
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
		menuI?.setChecked(true)
	}
}
