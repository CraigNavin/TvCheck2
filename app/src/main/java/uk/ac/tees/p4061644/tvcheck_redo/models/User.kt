package uk.ac.tees.p4061644.tvcheck_redo.models

import com.parse.ParseClassName

/**
 * Created by Craig on 27/02/2018.
 */
@ParseClassName("UserData")
class User (val UserID: String){

	var list: ArrayList<ListModel>? = ArrayList<ListModel>()

	constructor(UserID: String, list:ArrayList<ListModel>): this(UserID){
		this.list = list
	}

	//Some of these are for convenience for now. Will know what i need when im near completion

	fun getList(string:String): ListModel?{
		return list!!.find { it.name == string }
	}

	fun getShow(list: ArrayList<Show>, name: String): Show?{
		return list.find { it.name == name }
	}

	//Not sure if I will need this. Put this here for convenience for the time being
	fun checkListExists(string: String): Boolean{
		return list!!.any { it.name == string }
	}

	fun checkNameTaken(string: String): Boolean{
		return list!!.any { it.name == string }
	}

	fun checkListContainsShow(showname: String,listname: String): Boolean{
		var ListModel: ListModel = list!!.find { it.name == listname}!!
		return ListModel.list!!.any { it.name == showname }

	}
}