package uk.ac.tees.p4061644.tvcheck_redo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.omertron.themoviedbapi.model.tv.TVBasic
import com.omertron.themoviedbapi.model.tv.TVInfo
import com.omertron.themoviedbapi.model.tv.TVSeasonBasic
import com.squareup.picasso.Picasso
import uk.ac.tees.p4061644.tvcheck_redo.models.User
import kotlinx.android.synthetic.main.activity_show.*
import kotlinx.android.synthetic.main.layout_bottom_navigation_view.*
import uk.ac.tees.p4061644.tvcheck_redo.Adapters.SeasonEpisodeListAdapter
import uk.ac.tees.p4061644.tvcheck_redo.models.ListModel
import uk.ac.tees.p4061644.tvcheck_redo.utils.*


class ShowActivity : AppCompatActivity() {

	private val TAG: String = "ShowActivity"
	private var Async : AsyncTasker? = null
	private var navbar: BottomNavigationViewEx? = null
	private val activity_Num: Int = 1
	private var basic : TVBasic? = null
	private var user: User? = null
	private var converter: Converter? = null
	var show : TVInfo? = null
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_show)

		setup()
		setView()
	}

	fun setView(){



		ShowName_TV!!.text = show!!.name
		Overview_TV!!.text = show!!.overview
		Rating_TV!!.text = show!!.voteAverage.toString()
		Picasso.with(applicationContext).load(applicationContext.getString(R.string.base_address_w185) + show!!.posterPath)
				.placeholder(R.drawable.ic_default_search_image)
				.into(PosterView)
		setupBottomnavigatioView()
		//navbar!!.bringChildToFront(findViewById(R.id.bottomNavViewBar))
		var seasons = show!!.seasons
		var adapter = SeasonEpisodeListAdapter(this,seasons,null,applicationContext)
		season_list!!.adapter = adapter

		season_list!!.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
			val item = parent.getItemAtPosition(position) as String
			val season : TVSeasonBasic = Gson().fromJson(item)
			val intent = Intent(applicationContext,SeasonActivity::class.java)//activity_Num 1
			intent.putExtra("Season",item)
			intent.putExtra("User",Gson().toJson(user))
			intent.putExtra("TVID",show!!.id)
			//Toast.makeText(applicationContext,show.id.toString(),Toast.LENGTH_SHORT).show()
			//Toast.makeText(applicationContext,season.seasonNumber.toString(),Toast.LENGTH_SHORT).show()
			applicationContext.startActivity(intent)
		}

		adapter.notifyDataSetChanged()
	}



	fun setup(){
		Async = AsyncTasker(applicationContext)
		user = Gson().fromJson(intent.getStringExtra("User"))
		basic = Gson().fromJson(intent.getStringExtra("Show"))
		converter = Converter(applicationContext)
		save_progress_bar.visibility = View.GONE
		show = Async!!.getShowInfoAsync(basic!!.id)
		if (user!!.checkListsContainsShow(basic!!.id)){
			save_float_btn.setImageDrawable(getDrawable(R.drawable.ic_love))
		}else{
			save_float_btn.setImageDrawable(getDrawable(R.drawable.ic_addlist_icon))
		}
		registerForContextMenu(save_float_btn)

		save_float_btn.setOnClickListener {
			if (!user!!.checkListsContainsShow(basic!!.id)){
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
	override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
		super.onCreateContextMenu(menu, v, menuInfo)
		menu!!.setHeaderTitle("Choose a list to add")
		user!!.list!!.forEach { menu.add(it.name) }
		menu.add(applicationContext.getString(R.string.New_List___))
	}

	override fun onContextItemSelected(item: MenuItem?): Boolean {
		var listName : String = ""
		var nameList = user!!.getListNames()
		when(item!!.title){
			applicationContext.getString(R.string.New_List___) -> {
				do{
					listName = createinputDialog(user!!)
				}while(user!!.checkNameTaken(listName) || listName.isNullOrEmpty())

				user!!.list!!.add(ListModel(listName))

			}
		}
		if (nameList.contains(item.title)){
			var chosenList = user!!.list!!.find { it.name == item.title }
			if (user!!.checkListContainsShow(show!!.id,chosenList!!.name)){
				Toast.makeText(applicationContext,"This list already contains this show. Choose another list",Toast.LENGTH_SHORT).show()
			}else{
				save_progress_bar.visibility = View.VISIBLE
				save_progress_bar.bringToFront()
				chosenList.list!!.add(converter!!.convert(show!!))
				user = DatabaseHandler(applicationContext).update(user!!)
				save_progress_bar.visibility = View.GONE
				Toast.makeText(applicationContext,show!!.id.toString() + " added to list " + chosenList.name,Toast.LENGTH_SHORT).show()
			}
		}else{
			Toast.makeText(applicationContext,"List does not exist",Toast.LENGTH_SHORT).show()
		}
		return super.onContextItemSelected(item)
	}

	fun createinputDialog(user: User):String{
		var layoutInf = LayoutInflater.from(applicationContext)
		var prompt = layoutInf.inflate(R.layout.input_prompt,null)
		var builder = AlertDialog.Builder(applicationContext)
		var result: String = ""

		builder.setView(prompt)

		val promptInput = prompt.findViewById(R.id.editTextDialogUserInput) as EditText

		builder.setCancelable(false)
				.setPositiveButton("Create", { dialog, which ->
					result = promptInput.text.toString()
					 if (result.isNullOrEmpty()){
						 Toast.makeText(applicationContext, "Please enter a list name",Toast.LENGTH_SHORT).show()
						 result =""
						 promptInput.setText("")
					 }else if(user.checkNameTaken(result)){
						 Toast.makeText(applicationContext,"A list with this name already exists. Please choose a different name",Toast.LENGTH_SHORT).show()
						 result = ""
						 promptInput.setText("")
					 }else{
						 dialog.dismiss()
					 }
				}
				).setNegativeButton("Cancel", { dialog, which -> dialog.cancel() })

		val dialog = builder.create()

		dialog.show()

		return result
	}

	inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

	private fun setupBottomnavigatioView(){
		Log.d(TAG,"setupBottomNavigationView")
		BottomNavigationBarHelper.setupBottomNavigationBar(bottomNavViewBar)
		BottomNavigationBarHelper.enableNavigation(applicationContext, bottomNavViewBar,intent.getStringExtra("User"))
		val menu: Menu? = bottomNavViewBar?.menu
		val menuI: MenuItem? = menu?.getItem(activity_Num)
		menuI?.isChecked = true
	}

	override fun onDestroy() {
		//android.os.Process.killProcess(android.os.Process.myPid());
		super.onDestroy()
		Runtime.getRuntime().gc()
	}
}
