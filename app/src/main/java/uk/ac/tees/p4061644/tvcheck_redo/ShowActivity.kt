package uk.ac.tees.p4061644.tvcheck_redo

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.omertron.themoviedbapi.model.tv.TVBasic
import com.omertron.themoviedbapi.model.tv.TVInfo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_show.*
import kotlinx.android.synthetic.main.layout_bottom_navigation_view.*
import uk.ac.tees.p4061644.tvcheck_redo.Adapters.SeasonEpisodeListAdapter
import uk.ac.tees.p4061644.tvcheck_redo.models.Show
import uk.ac.tees.p4061644.tvcheck_redo.models.User
import uk.ac.tees.p4061644.tvcheck_redo.utils.AsyncTasker
import uk.ac.tees.p4061644.tvcheck_redo.utils.BottomNavigationBarHelper
import uk.ac.tees.p4061644.tvcheck_redo.utils.Converter
import uk.ac.tees.p4061644.tvcheck_redo.utils.DatabaseHandler


class ShowActivity : AppCompatActivity() {

	private val TAG: String = "ShowActivity"
	private var Async : AsyncTasker? = null
	private val activity_Num: Int = 1
	private var basic : TVBasic? = null
	private var user: User? = null
	private var converter: Converter? = null
	var show : TVInfo? = null
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_show)
		setup()
	}

	/**
	 * Retrieves the Show with matching ID from intent from the users List.
	 * Show is then used to determine if episode has been watched or not
	 * @return Show object from user list
	 */
	fun getShow(): Show?{
		user!!.list!!.forEach{ it.list!!.forEach {if(it.id == show!!.id){ return it } } }
		return null
	}

	/**
	 * Sets an onClickListener to the floating action button. T
	 */
	fun setupFloatBtn(){
		registerForContextMenu(save_float_btn)
		save_float_btn.setOnClickListener {
			if (!inLists()){
				openContextMenu(save_float_btn)
				save_float_btn.setImageDrawable(getDrawable(R.drawable.ic_love))
			}else{
				AlertDialog.Builder(this)
						.setTitle("Confirm Remove")
						.setMessage("Are you sure you want to remove " + basic!!.name  +" from your lists?")
						.setPositiveButton("Remove", { dialog, which ->
							user!!.list!!.forEach { it.list!!.removeIf { it.id == basic!!.id } }
							user = DatabaseHandler(applicationContext).update(user!!)
							save_float_btn.setImageDrawable(getDrawable(R.drawable.ic_addlist_icon))
							Toast.makeText(applicationContext,"Show Removed",Toast.LENGTH_SHORT).show()

						})
						.setNegativeButton("Cancel",null).show()
			}
		}
	}

	/**
	 * Sets a OnCheckedChangeListener to the activities checkbox and handles what happens when checkBox value changes
	 */
	fun setupCheckbox(){
		watched_box.setOnCheckedChangeListener { buttonView, isChecked ->
			if (inLists()){
				var showInList = getShow()
				showInList!!.watched = isChecked
				AlertDialog.Builder(this)
						.setTitle("Watched Show?")
						.setMessage("Do you want to set all this seasons and episodes to watched?")
						.setPositiveButton(android.R.string.yes,
								DialogInterface.OnClickListener { dialog, which ->
									showInList!!.seasons!!.forEach { it.watched = isChecked
										it.episodes.forEach { it.watched = isChecked
										}
									}
									user = DatabaseHandler(applicationContext).update(user!!)
									Toast.makeText(applicationContext,"Show,seasons and episodes Updated",Toast.LENGTH_SHORT).show()
								}

						)
						.setNegativeButton(android.R.string.no,
								DialogInterface.OnClickListener { dialog, which ->
									user = DatabaseHandler(applicationContext).update(user!!)
									Toast.makeText(applicationContext,"Show Updated",Toast.LENGTH_SHORT).show()
								}

						).show()
			}else{
				watched_box.isChecked = false
				Toast.makeText(applicationContext,"Please add this show to a list before marking episodes as watched",Toast.LENGTH_SHORT).show()
			}

		}
	}

	/**
	 * Sets the listView's adapter and assigns an onClickListener that will handle all clicks on the listView
	 */
	fun setupList(){
		var seasons = show!!.seasons
		var adapter = SeasonEpisodeListAdapter(this,seasons,null,applicationContext)
		season_list!!.adapter = adapter
		season_list!!.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
			val item = parent.getItemAtPosition(position) as String
			val intent = Intent(applicationContext,SeasonActivity::class.java)//activity_Num 1
			intent.putExtra("Season",item)
			intent.putExtra("User",Gson().toJson(user))
			intent.putExtra("TVID",show!!.id)
			applicationContext.startActivity(intent)
		}
		adapter.notifyDataSetChanged()
	}

	/**
	 * Populates the view elements on this activity with the correct information. Calls
	 * methods that set up parts of the activity
	 *
	 */
	fun setView(){
		show = Async!!.getShowInfoAsync(basic!!.id)
		Name_TV!!.text = show!!.name
		Bio_TV!!.text = show!!.overview

		if (getShow() != null){
			watched_box.isChecked = getShow()!!.watched
		}
		Picasso.with(applicationContext).load(applicationContext.getString(R.string.base_address_w185) + show!!.posterPath)
				.placeholder(R.drawable.ic_default_search_image)
				.into(PosterView)

		bottomNavViewBar.bringChildToFront(findViewById(R.id.bottomNavViewBar))


		save_progress_bar.visibility = View.GONE


		for (list in user!!.list!!){
			if (user!!.checkListContainsShow(basic!!.id,list.name)){
				save_float_btn.setImageDrawable(getDrawable(R.drawable.ic_love))
				break
			}else{
				save_float_btn.setImageDrawable(getDrawable(R.drawable.ic_addlist_icon))
			}
		}

		cast_btn.setOnClickListener {
			val intent = Intent(applicationContext,CrewListActivity::class.java)
			intent.putExtra("User",Gson().toJson(user))
			intent.putExtra("showName",show!!.name)
			intent.putExtra("CastList", Gson().toJson(show!!.credits.cast))
			applicationContext.startActivity(intent)
		}

		setupFloatBtn()
		setupCheckbox()
		setupList()
	}

	/**
	 * Sets up variable and calls method that handles View elements
	 */
	fun setup(){
		Async = AsyncTasker(applicationContext)
		user = Gson().fromJson(intent.getStringExtra("User"))
		basic = Gson().fromJson(intent.getStringExtra("Show"))
		converter = Converter(applicationContext)
		setView()
		setupBottomnavigatioView()
		Log.d(TAG,user.toString())

	}

	/**
	 * Checks if any of the users lists contain a show matching id passed
	 * @return Boolean that represents if any of the users list contain a show with a matching id
	 * to the currently selected show
	 */
	fun inLists(): Boolean{
		for (list in user!!.list!!){
			if (user!!.checkListContainsShow(basic!!.id,list.name)){
				return true
			}else{
				return false
			}
		}
		return false
	}

	/**
	 * Overridden function that adds each lists name to the menu list
	 */
	override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
		super.onCreateContextMenu(menu, v, menuInfo)
		menu!!.setHeaderTitle("Choose a list to add")
		user!!.list!!.forEach { menu.add(it.name) }
	}

	/**
	 * Overridden function that performs validation to ensure that the user cannot add
	 * a show to a list that already contains the same show. If validation passes, the show
	 * is added to the selected list.
	 * @param [item] A menuItem that has a Title that represents one of the users lists
	 * @return super.onContextItemSelected(item)
	 *  */
	override fun onContextItemSelected(item: MenuItem?): Boolean {
		var nameList = user!!.getListNames()
		if (nameList.contains(item!!.title)){
			var chosenList = user!!.list!!.find { it.name == item.title }
			if (user!!.checkListContainsShow(show!!.id,chosenList!!.name)){
				Toast.makeText(applicationContext,"This list already contains this show. Choose another list",Toast.LENGTH_SHORT).show()
			}else{
				save_progress_bar.visibility = View.VISIBLE
				save_progress_bar.bringToFront()
				chosenList.list!!.add(converter!!.convert(show!!))
				DatabaseHandler(applicationContext).update(user!!)
				save_progress_bar.visibility = View.GONE
				Toast.makeText(applicationContext,show!!.name + " added to list " + chosenList.name,Toast.LENGTH_SHORT).show()
			}
		}else{
			Toast.makeText(applicationContext,"List does not exist",Toast.LENGTH_SHORT).show()
		}
		return super.onContextItemSelected(item)
	}

	inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

	/**
	 * Instantiates the bottom navigation view and sets the menu values to the navigation bar
	 */
	private fun setupBottomnavigatioView(){
		Log.d(TAG,"setupBottomNavigationView")
		BottomNavigationBarHelper.setupBottomNavigationBar(bottomNavViewBar)
		BottomNavigationBarHelper.enableNavigation(applicationContext, bottomNavViewBar,Gson().toJson(user))
		val menu: Menu? = bottomNavViewBar?.menu
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
