package uk.ac.tees.p4061644.tvcheck_redo.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.parse.*
import com.parse.ParseQuery.getQuery
import org.w3c.dom.UserDataHandler
import uk.ac.tees.p4061644.tvcheck_redo.R
import uk.ac.tees.p4061644.tvcheck_redo.models.ListModel
import uk.ac.tees.p4061644.tvcheck_redo.models.User


/**
 * Created by Craig on 27/02/2018.
 */

class DatabaseHandler(context: Context) {
	val gson = Gson()
	var user : User? = null

	init {
		setup(context)
	}

	fun setup(context: Context){
		Parse.initialize(Parse.Configuration.Builder(context)
				.applicationId(context.resources.getString(R.string.back4app_app_id))
				.server(context.resources.getString(R.string.back4app_server_url))
				.build())
		ParseInstallation.getCurrentInstallation().saveInBackground()
		Log.d("SETUP TEST","SETUP FUNCTIONS DONE")

	}

	fun insert(user: User) {
		var userobj = ParseObject("UserData")
		userobj.put("UserId",user.UserID)
		userobj.put("Lists",gson.toJson(user.list))
		userobj.saveInBackground()
		Log.d("INSERT","SAVE FUNCTIONS DONE")
	}

	inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

	fun retrievefirst(UserId: String): User?{

		var query : ParseQuery<ParseObject> = getQuery("UserData")
		query.whereEqualTo("UserId",UserId)
		var obj = query.first

		if (obj == null){
			Log.d("RETRIEVEFIRST","NOTHING FOUND")
			return null
		}else{
			Log.d("RETRIEVEFIRST","GOT USER")
			val list: ArrayList<ListModel> = gson.fromJson(obj.get("Lists").toString())
			return User(obj.getString("UserId"),list)
		}
	}

	fun userExists(UserId: String): Boolean{
		var exists = false
		var query : ParseQuery<ParseObject> = getQuery("UserData")
		query.whereEqualTo("UserId",UserId)
		query.getFirstInBackground({ UserData, e ->
			if (e == null) {
				exists = true
				Log.d("CHECKUSER", "USER EXISTS")
			}else{
				exists = false
				Log.d("CHECKUSER", "USER DOES NOT EXIST " + e.message)
				}
			}
		)
		return exists
	}

	fun update(user: User):User {
		var query: ParseQuery<ParseObject> = getQuery("UserData")
		query.whereEqualTo("UserId", user.UserID)
		query.getFirstInBackground({ userdata, e ->
			Log.d("UPDATE", "UPDATE START " + userdata.get("UserId"))
			if (e == null) {
				userdata.put("Lists", gson.toJson(user.list))
				userdata.saveInBackground()
				Log.d("UPDATE ", "UPDATE DONE")
			}
			else {
				if (e.code == ParseException.OBJECT_NOT_FOUND) {
					Log.d("UPDATE", "USER NOT FOUND " + e.message)
				}
				else {
					Log.d("UPDATE", e.code.toString() + " OTHER ERROR " + e.message)
				}
			}
		})
		return retrievefirst(user.UserID)!!
	}


}