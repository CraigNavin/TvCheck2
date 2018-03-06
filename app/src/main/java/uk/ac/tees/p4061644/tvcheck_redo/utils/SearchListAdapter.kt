package uk.ac.tees.p4061644.tvcheck_redo.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.omertron.themoviedbapi.model.tv.TVBasic
import uk.ac.tees.p4061644.tvcheck_redo.R
import java.io.InputStream
import java.net.URL

/**
 * Created by Craig on 05/03/2018.
 */
class SearchListAdapter(private var activity: Activity,private var items: ArrayList<TVBasic>): BaseAdapter(){

	private class ViewHolder(row: View?){
		var txtName: TextView? = null
		var txtComment: TextView? = null
		var imgView: ImageView? = null
		init {
			this.txtName = row?.findViewById(R.id.txtName) as TextView?
			this.txtComment = row?.findViewById(R.id.txtComment) as TextView?
			this.imgView = row?.findViewById(R.id.img_view) as ImageView
		}
	}

	val base_address = "https://image.tmdb.org/t/p/w185"
	override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
		val view: View?
		val viewHolder: ViewHolder

		if (convertView == null){
			val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
			view = inflater.inflate(R.layout.search_item_layout,null)
			viewHolder = ViewHolder(view)
			view!!.tag = viewHolder
		} else{
			view = convertView
			viewHolder = view.tag as ViewHolder
		}

		var TVBasic = items[position]
		var rating = "User Rating: " + TVBasic.voteAverage
		viewHolder.txtName!!.text = TVBasic.name
		viewHolder.txtComment!!.text = rating
		if (TVBasic.posterPath!= null){
			DownloadImageTask(viewHolder.imgView!!).execute(TVBasic.posterPath)
			//TRY THIS TOMORROW FOO
			//Glide.with(activity).load(base_address + TVBasic.posterPath).into(viewHolder.imgView)
		}


		return view
	}

	override fun getItem(position: Int): TVBasic{
		return items[position]
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun getCount(): Int {
		return items.size
	}

	private class DownloadImageTask(bmImage: ImageView): AsyncTask<String, Void, Bitmap>() {
		override fun doInBackground(vararg params: String?): Bitmap {
			val base_address = "https://image.tmdb.org/t/p/w185"
			var urlDisplay = params[0]
			var mIcon : Bitmap? = null

			try{
				var input: InputStream = URL(base_address+urlDisplay).openStream()
				mIcon = BitmapFactory.decodeStream(input)
				input.close()
			} catch (e: Exception){
				Log.e("IMAGETASKERROR",e.message)
			}
			return mIcon!!
		}

		var bmImage: ImageView?

		init {
			this.bmImage = bmImage
		}

		override fun onPostExecute(result: Bitmap?) {
			bmImage!!.setImageBitmap(result)
		}




	}
}