package uk.ac.tees.p4061644.tvcheck_redo.models

import com.parse.ParseClassName

/**
 * Created by Craig on 27/02/2018.
 */
@ParseClassName("UserData")
data class User (val UserID: String){

	var list: ArrayList<ListModel>? = ArrayList<ListModel>()

	constructor(UserID: String, list:ArrayList<ListModel>): this(UserID){
		this.list = list
	}
}