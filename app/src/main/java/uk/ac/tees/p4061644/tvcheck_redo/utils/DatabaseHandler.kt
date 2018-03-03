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

class DatabaseHandler {
	val gson = Gson()
	var user : User? = null


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
		Log.d("SAVE TEST","SAVE FUNCTIONS DONE")
	}

	inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

	fun count(UserId: String): Int{
		var query : ParseQuery<ParseObject> = getQuery("UserData")
		query.whereEqualTo("UserId",UserId)

		return query.countInBackground().result
	}

	fun retrieve(UserId: String): User{

		var query : ParseQuery<ParseObject> = getQuery("UserData")
		query.whereEqualTo("UserId",UserId)
		var obj = query.first

		query.getFirstInBackground({ `object`, _ ->
			if (`object` == null){
				Log.d("RETRIEVE", "NOTHING FOUND")
			}else{
				Log.d("CHECK RETRIEVE", `object`.get("UserId").toString())
				val list: ArrayList<ListModel> = gson.fromJson(`object`.get("Lists").toString())
				user =  User(`object`.get("UserId").toString(),list)

			}
		})
		return user!!
	}

	fun retrievefirst(UserId: String): User?{

		var query : ParseQuery<ParseObject> = getQuery("UserData")
		query.whereEqualTo("UserId",UserId)
		var obj = query.first

		if (obj == null){
			Log.d("RETRIEVE CHECK","NOTHING FOUND")
			return null
		}else{
			val list: ArrayList<ListModel> = gson.fromJson(obj.get("Lists").toString())
			return User(obj.getString("UserId"),list)
		}
	}

	fun checkUser(UserId: String): Boolean{
		return retrievefirst(UserId) != null
	}

	fun update(UserId: String){

	}

}