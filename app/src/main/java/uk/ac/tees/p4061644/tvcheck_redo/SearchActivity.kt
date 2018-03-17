package uk.ac.tees.p4061644.tvcheck_redo

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewParent
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.omertron.themoviedbapi.model.tv.TVBasic
import com.squareup.picasso.Picasso
import uk.ac.tees.p4061644.tvcheck_redo.models.User
import uk.ac.tees.p4061644.tvcheck_redo.utils.AsyncTasker
import uk.ac.tees.p4061644.tvcheck_redo.utils.BottomNavigationBarHelper
import uk.ac.tees.p4061644.tvcheck_redo.utils.SearchListAdapter
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.layout_bottom_navigation_view.*


class SearchActivity : AppCompatActivity(), View.OnClickListener {

	private var Async : AsyncTasker? = null
	private val activity_Num: Int = 1
	private val TAG: String = "SearchActivity"

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_search)
		setup()
		setupBottomnavigatioView()
	}

	fun setup(){

		Async = AsyncTasker(applicationContext)



		search_button.setOnClickListener(this)
		bottomNavViewBar.bringChildToFront(this.bottomNavViewBar)
	}
	inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)
	fun search(term: String){
		var user: User = Gson().fromJson(intent.getStringExtra("User"))
		var results = Async!!.searchShows(search_text_field.text.toString())
		var adapter = SearchListAdapter(this,Async!!.getCustomList(user!!.list!![0].list!!),applicationContext)
		result_list_view.adapter = adapter
		result_list_view.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
			val item = parent.getItemAtPosition(position) as String
			val show : TVBasic = Gson().fromJson(item)
			var seasons = Async!!.getShowInfoAsync(show.id).seasons
			//Toast.makeText(applicationContext,seasons.toString(),Toast.LENGTH_SHORT).show()
			val intent = Intent(applicationContext,ShowActivity::class.java)//activity_Num 1
			intent.putExtra("Show",item)
			intent.putExtra("User",Gson().toJson(user))
			applicationContext.startActivity(intent)
		}
		adapter.notifyDataSetChanged()
	}

	private fun setupBottomnavigatioView(){
		Log.d(TAG,"setupBottomNavigationView")
		BottomNavigationBarHelper.setupBottomNavigationBar(bottomNavViewBar)
		BottomNavigationBarHelper.enableNavigation(applicationContext, bottomNavViewBar,intent.getStringExtra("User"))
		val menu: Menu? = bottomNavViewBar.menu
		val menuI: MenuItem? = menu?.getItem(activity_Num)
		menuI?.isChecked = true
	}

	override fun onClick(v: View?) {
		when(v!!.id){
			R.id.search_button -> if (search_text_field.text.toString() == ""){
				Toast.makeText(applicationContext,"No Search Entered",Toast.LENGTH_LONG).show()
			}else{
				search(search_text_field.text.toString())
				val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

				inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
			}
		}
	}
	override fun onDestroy() {
		//android.os.Process.killProcess(android.os.Process.myPid());

		super.onDestroy()
		Runtime.getRuntime().gc()

	}
}
