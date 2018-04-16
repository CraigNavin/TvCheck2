package uk.ac.tees.p4061644.tvcheck_redo.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.parse.*
import com.parse.ParseQuery.getQuery
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

	/**
	 * Installs  and initialises Parse's settings on this phone so its server can be used
	 * @param [context] application context for access to string values
	 */
	fun setup(context: Context){
		Parse.initialize(Parse.Configuration.Builder(context)
				.applicationId(context.resources.getString(R.string.back4app_app_id))
				.server(context.resources.getString(R.string.back4app_server_url))
				.build())
		ParseInstallation.getCurrentInstallation().saveInBackground()
		Log.d("SETUP TEST","SETUP FUNCTIONS DONE")

	}


	/**
	 * Adds a user to the database
	 * @param [user] User object that will be added to the database
	 */
	fun insert(user: User) {
		val userobj = ParseObject("UserData")
		userobj.put("UserId",user.UserID)
		userobj.put("Lists",gson.toJson(user.list))
		userobj.saveInBackground()
		Log.d("INSERT","SAVE FUNCTIONS DONE")
	}

	inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

	/**
	 * Retrieves the first record in the database that matches a userID
	 * @param [UserId] A string that is searched for inside the database
	 * @return returns the record of the user found as a User object.
	 */
	fun retrievefirst(UserId: String): User?{

		val query : ParseQuery<ParseObject> = getQuery("UserData")
		query.whereEqualTo("UserId",UserId)
		val obj = query.first

		if (obj == null){
			return null
		}else{
			val list: ArrayList<ListModel> = gson.fromJson(obj.get("Lists").toString())
			return User(obj.getString("UserId"),list)
		}
	}

	/**
	 * Updates the current users record where the there is dirty information. Then returns a new User object that contains the new User information
	 * @param [user] User object used for their id to find their record in the database by UserId
	 * @return User object with new and updated information.
	 */
	fun update(user: User): User{
		val query: ParseQuery<ParseObject> = getQuery("UserData")
		var retUser : User? = null
		query.whereEqualTo("UserId", user.UserID)
		query.getFirstInBackground({ userdata, e ->
			Log.d("UPDATE", "UPDATE START " + userdata.get("UserId"))
			if (e == null) {
				userdata.put("Lists", gson.toJson(user.list))
				userdata.saveInBackground().onSuccess { retUser = retrievefirst(user.UserID) }
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
		if (retUser != null){
			return retUser!!
		}else{
			return user
		}

	}
}