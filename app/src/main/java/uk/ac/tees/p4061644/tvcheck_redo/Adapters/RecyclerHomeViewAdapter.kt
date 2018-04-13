package uk.ac.tees.p4061644.tvcheck_redo.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.omertron.themoviedbapi.model.tv.TVBasic
import com.squareup.picasso.Picasso
import uk.ac.tees.p4061644.tvcheck_redo.R
import uk.ac.tees.p4061644.tvcheck_redo.ShowActivity
import uk.ac.tees.p4061644.tvcheck_redo.models.User

/**
 * Created by Craig on 21/03/2018.
 */
class RecyclerHomeViewAdapter(private val context: Context, private val shows: ArrayList<TVBasic>,private val user: User,private val size:Int): RecyclerView.Adapter<RecyclerHomeViewAdapter.ViewHolder>() {


	/**
	 * Returns the size of the list
	 * @return Size of list
	 */
	override fun getItemCount(): Int {
		return shows.size
	}

	/**
	 * Inflates a layout into the ViewHolder
	 * @return A ViewHolder Instance of an assigned layout
	 */
	override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
		var view : View? = null
		if (size == 1){
			 view = LayoutInflater.from(context).inflate(R.layout.recyclerview_item,parent,false)
		}else if (size == 2){
			 view = LayoutInflater.from(context).inflate(R.layout.recyclerview_item_small,parent,false)
		}

		return ViewHolder(view!!)
	}

	/**
	 * Binds all of the relevant information to the ViewHolders elements.
	 * @param [holder] A ViewHolder instance with an assigned Layout
	 * @param [position] The position in the list that information will be retrieved
	 */
	override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
		var show = shows[position]
		Picasso.with(context).load(context.getString(R.string.base_address_w500) + show.posterPath)
				.placeholder(R.drawable.ic_default_search_image)
				.into(holder!!.imgView)
		holder.showName!!.text = show.name
		holder.imgView!!.setOnClickListener {
		val intent = Intent(context, ShowActivity::class.java)//activity_Num 1
		intent.putExtra("Show",Gson().toJson(show))
		intent.putExtra("User", Gson().toJson(user))
		context.startActivity(intent)
			Toast.makeText(context,show.name,Toast.LENGTH_SHORT).show()
		}
	}

	/**
	 * VView Holder class to manage all of the Views Elements
	 * @param [view] View of an assigned layout
	 */
	class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
		var imgView: ImageView? = null
		var showName: TextView? = null
		init{
			imgView = view.findViewById(R.id.imageView) as ImageView?
			showName = view.findViewById(R.id.Name) as TextView?
		}


	}
}