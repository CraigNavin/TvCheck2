package uk.ac.tees.p4061644.tvcheck_redo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import uk.ac.tees.p4061644.tvcheck_redo.utils.BottomNavigationBarHelper

class ProfileActivity : AppCompatActivity() {

	private val TAG = "ProfileActivity"
	private var navbar: BottomNavigationViewEx? = null
	private val activity_Num: Int = 2

	override fun onCreate(savedInstanceState: Bundle?) {
		Log.d(TAG, "OnCreate")
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_profile)
		navbar = findViewById(R.id.bottomNavViewBar) as BottomNavigationViewEx
		setupBottomnavigatioView()

	}

	private fun setupBottomnavigatioView(){
		Log.d(TAG,"setupBottomNavigationView")
		BottomNavigationBarHelper.setupBottomNavigationBar(navbar)
		BottomNavigationBarHelper.enableNavigation(applicationContext, navbar)
		val menu: Menu? = navbar?.menu
		val menuI: MenuItem? = menu?.getItem(activity_Num)
		menuI?.setChecked(true)
	}
}
