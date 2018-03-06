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


	fun setupBottomNavigationBar(NavBar: BottomNavigationViewEx?) {
		Log.d(TAG, "setupBottomNavigationBar")
		NavBar?.enableAnimation(true)
		NavBar?.enableItemShiftingMode(true)
		NavBar?.enableShiftingMode(false)
		NavBar?.setTextVisibility(false)

	}


	fun enableNavigation(context: Context, viewEx: BottomNavigationViewEx?, User: String) {
		viewEx?.onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
			when (item.itemId) {
				R.id.action_home ->{
					val intent = Intent(context, HomeActivity::class.java) //activity_Num 0
					intent.putExtra("User", User)
					context.startActivity(intent)
				}

				R.id.action_Search -> {
					val intent = Intent(context,SearchActivity::class.java)//activity_Num 1
					intent.putExtra("User",User)
					context.startActivity(intent)
				}

				R.id.action_profile -> {
					val intent = Intent(context, ProfileActivity::class.java) //activity_Num 2
					intent.putExtra("User", User)
					context.startActivity(intent)
				}
			}
			false
		}
	}
}