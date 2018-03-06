package uk.ac.tees.p4061644.tvcheck_redo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import uk.ac.tees.p4061644.tvcheck_redo.models.User
import uk.ac.tees.p4061644.tvcheck_redo.utils.BottomNavigationBarHelper

class ProfileActivity : AppCompatActivity() {

	private val TAG = "ProfileActivity"
	private var navbar: BottomNavigationViewEx? = null
	private val activity_Num: Int = 2
	private var user : User? = null
	private var gson = Gson()

	override fun onCreate(savedInstanceState: Bundle?) {
		Log.d(TAG, "OnCreate")
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_profile)
		navbar = findViewById(R.id.bottomNavViewBar) as BottomNavigationViewEx
		setupBottomnavigatioView()
		setUser()
		Log.d("USERGSONCHECK",user!!.UserID)

	}

	inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)
	fun setUser(){
		user = gson.fromJson(intent.getStringExtra("User"))
	}

	private fun setupBottomnavigatioView(){
		Log.d(TAG,"setupBottomNavigationView")
		BottomNavigationBarHelper.setupBottomNavigationBar(navbar)
		BottomNavigationBarHelper.enableNavigation(applicationContext, navbar,intent.getStringExtra("User"))
		val menu: Menu? = navbar?.menu
		val menuI: MenuItem? = menu?.getItem(activity_Num)
		menuI?.setChecked(true)
	}
}
