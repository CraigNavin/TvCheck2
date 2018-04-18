package uk.ac.tees.p4061644.tvcheck_redo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.omertron.themoviedbapi.model.tv.TVBasic
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.layout_bottom_navigation_view.*
import uk.ac.tees.p4061644.tvcheck_redo.models.User
import uk.ac.tees.p4061644.tvcheck_redo.utils.AsyncTasker
import uk.ac.tees.p4061644.tvcheck_redo.utils.BottomNavigationBarHelper
import uk.ac.tees.p4061644.tvcheck_redo.Adapters.SearchListAdapter


class SearchActivity : AppCompatActivity(){

	private var Async : AsyncTasker? = null
	private val activity_Num: Int = 1
	private val TAG: String = "SearchActivity"
	private var user: User? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_search)
		setup()

	}
	inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

	/**
	 * Sets up variable and calls method that handles View elements
	 */
	fun setup(){
		user = Gson().fromJson(intent.getStringExtra("User"))
		Async = AsyncTasker(applicationContext)
		setView()
		setupBottomnavigatioView()
	}

	/**
	 * Sets up all elements that are on the activity. Assigns onClickListeners to the search button
	 */
	fun setView(){
		/* Sets list to users list that was selected if intent content is not null */
		if (intent.extras.get("List") != null){
			search_button.visibility = View.GONE
			search_text_field.visibility = View.GONE
			val list = Async!!.getUserList(user!!.getList(intent.getStringExtra("List"))!!.list!!)
			val adapter = SearchListAdapter(this,list,applicationContext)
			searchList_lv.adapter = adapter
		}
		/* Sets onClickListener to search result list */
		searchList_lv.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
			val item = parent.getItemAtPosition(position) as String
			val intent = Intent(applicationContext,ShowActivity::class.java)//activity_Num 1
			intent.putExtra("Show",item)
			intent.putExtra("User",Gson().toJson(user))
			applicationContext.startActivity(intent)
		}
		/* Sets onClickListener to Search button */
		search_button.setOnClickListener({
			when(it!!.id){
				R.id.search_button -> if (search_text_field.text.toString() == ""){
					Toast.makeText(applicationContext,"No Search Entered",Toast.LENGTH_LONG).show()
				}else{
					search(search_text_field.text.toString())
					val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

					inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
				}
			}
		})
		bottomNavViewBar.bringChildToFront(this.bottomNavViewBar)
	}

	/**
	 * Is called when the search button is pressed. Performs the search and sets the adapter to the search result list.
	 */
	fun search(term: String){
		var results : ArrayList<TVBasic> = ArrayList()
		results.addAll(Async!!.searchShows(term)!!)
		val adapter = SearchListAdapter(this,results,applicationContext)
		searchList_lv.adapter = adapter
		adapter.notifyDataSetChanged()

	}

	/**
	 * Instantiates the bottom navigation view and sets the menu values to the navigation bar
	 */
	private fun setupBottomnavigatioView(){
		Log.d(TAG,"setupBottomNavigationView")
		BottomNavigationBarHelper.setupBottomNavigationBar(bottomNavViewBar)
		BottomNavigationBarHelper.enableNavigation(applicationContext, bottomNavViewBar,Gson().toJson(user),activity_Num)
		val menu: Menu? = bottomNavViewBar.menu
		val menuI: MenuItem? = menu?.getItem(activity_Num)
		menuI?.isChecked = true
	}

	/**
	 * Overridden onDestroy function runs garbage collection to help keep RAM usage as low as possible
	 */
	override fun onDestroy() {
		//android.os.Process.killProcess(android.os.Process.myPid());

		super.onDestroy()
		Runtime.getRuntime().gc()

	}
}
