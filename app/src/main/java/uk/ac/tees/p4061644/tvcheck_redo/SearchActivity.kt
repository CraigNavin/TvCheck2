package uk.ac.tees.p4061644.tvcheck_redo

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewParent
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.squareup.picasso.Picasso
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
		navbar = findViewById(R.id.bottomNavViewBar) as BottomNavigationViewEx
		Async = AsyncTasker(applicationContext)
		listView = findViewById(R.id.result_list_view) as ListView
		searchButton = findViewById(R.id.search_button) as ImageButton
		searchField = findViewById(R.id.search_text_field) as EditText
		searchButton!!.setOnClickListener(this)
		setupBottomnavigatioView()
		navbar!!.bringChildToFront(findViewById(R.id.bottomNavViewBar))

	}

	fun search(term: String){
		var results = Async!!.searchShows(searchField!!.text.toString())
		var adapter = SearchListAdapter(this,results!!,null,applicationContext)
		listView!!.adapter = adapter
		//Toast.makeText(applicationContext,listView!!.getItemAtPosition(i)!!.toString(),Toast.LENGTH_LONG)
		adapter!!.notifyDataSetChanged()
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
