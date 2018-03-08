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


class SearchActivity : AppCompatActivity(), View.OnClickListener {


	private var navbar: BottomNavigationViewEx? = null
	private var Async : AsyncTasker? = null
	private val activity_Num: Int = 1
	private val TAG: String = "SearchActivity"
	private var listView: ListView? = null
	private var searchButton: ImageButton? = null
	private var searchField: EditText? = null


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_search)
		setup()
		setupBottomnavigatioView()
	}

	fun setup(){
		navbar = findViewById(R.id.bottomNavViewBar) as BottomNavigationViewEx
		Async = AsyncTasker(applicationContext)
		listView = findViewById(R.id.result_list_view) as ListView
		searchButton = findViewById(R.id.search_button) as ImageButton
		searchField = findViewById(R.id.search_text_field) as EditText
		searchButton!!.setOnClickListener(this)
		navbar!!.bringChildToFront(findViewById(R.id.bottomNavViewBar))
	}
	inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)
	fun search(term: String){
		var results = Async!!.searchShows(searchField!!.text.toString())
		var adapter = SearchListAdapter(this,results!!,null,applicationContext)
		listView!!.adapter = adapter
		var user: User = Gson().fromJson(intent.getStringExtra("User"))
		Toast.makeText(applicationContext,user.UserID,Toast.LENGTH_SHORT).show()
		listView!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
			val item = parent.getItemAtPosition(position) as String
			val show : TVBasic = Gson().fromJson(item)
			var seasons = Async!!.getShowAsync(show.id).seasons
			Toast.makeText(applicationContext,seasons.toString(),Toast.LENGTH_SHORT).show()
			val intent = Intent(applicationContext,ShowActivity::class.java)//activity_Num 1
			intent.putExtra("Show",item)
			intent.putExtra("User",Gson().toJson(user))
			applicationContext.startActivity(intent)
			//Toast.makeText(applicationContext,item.name,Toast.LENGTH_SHORT).show()
		}
		adapter.notifyDataSetChanged()
	}

	private fun setupBottomnavigatioView(){
		Log.d(TAG,"setupBottomNavigationView")
		BottomNavigationBarHelper.setupBottomNavigationBar(navbar)
		BottomNavigationBarHelper.enableNavigation(applicationContext, navbar,intent.getStringExtra("User"))
		val menu: Menu? = navbar?.menu
		val menuI: MenuItem? = menu?.getItem(activity_Num)
		menuI?.isChecked = true
	}

	override fun onClick(v: View?) {
		when(v!!.id){
			R.id.search_button -> if (searchField!!.text.toString() == ""){
				Toast.makeText(applicationContext,"No Search Entered",Toast.LENGTH_LONG).show()
			}else{
				search(searchField!!.text.toString())
				val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

				inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
			}
		}
	}
}
