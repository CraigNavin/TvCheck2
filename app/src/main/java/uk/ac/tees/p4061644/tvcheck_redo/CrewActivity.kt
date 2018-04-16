package uk.ac.tees.p4061644.tvcheck_redo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.omertron.themoviedbapi.model.person.PersonInfo
import com.omertron.themoviedbapi.model.tv.TVBasic
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_crew.*
import kotlinx.android.synthetic.main.layout_bottom_navigation_view.*
import uk.ac.tees.p4061644.tvcheck_redo.Adapters.RecyclerHomeViewAdapter
import uk.ac.tees.p4061644.tvcheck_redo.models.User
import uk.ac.tees.p4061644.tvcheck_redo.utils.AsyncTasker
import uk.ac.tees.p4061644.tvcheck_redo.utils.BottomNavigationBarHelper

class CrewActivity : AppCompatActivity() {

	private val TAG: String = "CrewActivity"
	private val activity_Num: Int = 1
	private var user: User? = null
	private var person: PersonInfo? = null
	private var Async: AsyncTasker? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_crew)
		setup()
	}

	/**
	 * Sets up variable and calls method that handles View elements
	 */
	fun setup(){
		user = Gson().fromJson(intent.getStringExtra("User"))
		Async = AsyncTasker(applicationContext)
		person = AsyncTasker(applicationContext).getPerson(intent.getStringExtra("CastID").toInt())
		Log.d(TAG,"SETUP")
		setView()
		setupBottomnavigatioView()
	}

	/**
	 * Sets up view elements with the crew members TV Credits and their details
	 */
	fun setView(){
		val tvCreditManager = LinearLayoutManager (this, LinearLayoutManager.HORIZONTAL,false)
		Name_TV.text = person!!.name
		Bio_TV.text = person!!.biography
		DPoB_tv.text = person!!.birthday + "," + person!!.placeOfBirth

		Picasso.with(applicationContext).load(getString(R.string.base_address_w185) + intent.getStringExtra("PicPath"))
				.placeholder(R.drawable.ic_default_search_image)
				.into(PosterView)
		val tvCreditList = ArrayList<TVBasic>()
		for (credit in person!!.tvCredits.cast){
			tvCreditList.add(Async!!.getShowBasicAsync(credit.id))
		}

		tvCreditList.apply {
			sortBy { it.popularity }
			reverse()
		}


		tvCredit_recycler.apply {
			layoutManager = tvCreditManager
			adapter = RecyclerHomeViewAdapter(applicationContext,tvCreditList,user!!,1)
		}
	}

	inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

	/**
	 * Instantiates the bottom navigation view and sets the menu values to the navigation bar
	 */
	private fun setupBottomnavigatioView(){
		Log.d(TAG,"setupBottomNavigationView")
		BottomNavigationBarHelper.setupBottomNavigationBar(bottomNavViewBar)
		BottomNavigationBarHelper.enableNavigation(applicationContext, bottomNavViewBar, Gson().toJson(user))
		val menu: Menu? = bottomNavViewBar.menu
		val menuI: MenuItem? = menu?.getItem(activity_Num)
		menuI?.isChecked = true
	}
}
