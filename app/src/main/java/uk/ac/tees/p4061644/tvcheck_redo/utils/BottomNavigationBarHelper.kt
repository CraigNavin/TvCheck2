package uk.ac.tees.p4061644.tvcheck_redo.utils

import android.content.Context
import android.content.Intent
import android.support.design.widget.BottomNavigationView
import android.util.Log
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import uk.ac.tees.p4061644.tvcheck_redo.HomeActivity
import uk.ac.tees.p4061644.tvcheck_redo.ProfileActivity
import uk.ac.tees.p4061644.tvcheck_redo.R
import uk.ac.tees.p4061644.tvcheck_redo.SearchActivity

/**
 * Created by Craig on 01/02/2018.
 */
object BottomNavigationBarHelper {
	private val TAG = "BottomNavigationBarHelp"

	/**
	 * Sets up navigation bar properties
	 */
	fun setupBottomNavigationBar(NavBar: BottomNavigationViewEx?) {
		Log.d(TAG, "setupBottomNavigationBar")
		NavBar?.enableAnimation(true)
		NavBar?.enableItemShiftingMode(true)
		NavBar?.enableShiftingMode(false)
		NavBar?.setTextVisibility(false)

	}

	/**
	 * Handles where each button on the navigation bar will take the user
	 * @param [context] application context from the current activity
	 * @param [viewEx] The navigation bar that is going to be assigned activity navigation
	 * @param [User] A Json string of the user that is going to be passed to each activity for access across the application.
	 */
	fun enableNavigation(context: Context, viewEx: BottomNavigationViewEx?, User: String) {
		viewEx?.onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
			var intent : Intent? = null
			when (item.itemId) {
				R.id.action_home ->{
					intent = Intent(context, HomeActivity::class.java) //activity_Num 0
				}

				R.id.action_Search -> {
					intent = Intent(context,SearchActivity::class.java)//activity_Num 1
				}

				R.id.action_profile -> {
					intent = Intent(context, ProfileActivity::class.java) //activity_Num 2
				}
			}
			intent!!.putExtra("User", User)
			context.startActivity(intent)
			false
		}
	}
}