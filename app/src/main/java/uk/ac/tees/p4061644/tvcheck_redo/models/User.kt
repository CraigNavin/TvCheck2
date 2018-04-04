package uk.ac.tees.p4061644.tvcheck_redo.models

import com.parse.ParseClassName

/**
 * This class holds the user information, UserId and the users lists.
 * It containts methods that are used to manipulate that lists for checks in addition to retriving lists and list information
 * @param [UserID] The userId from firebase authentication. Used for identification in parse database
 * @constructor Creates a User object with passed UserId and an empty ArrayList
 * Created by Craig on 27/02/2018.
 */
@ParseClassName("UserData")
class User (val UserID: String){

	var list: ArrayList<ListModel>? = ArrayList<ListModel>()

	/**
	 *
	 * @constructor Creates a User with passed UserID and passed ArrayList
	 */
	constructor(UserID: String, list:ArrayList<ListModel>): this(UserID){
		this.list = list
	}

	/**
	 *  Retrieves a list with a matching name
	 *  @param [string] List name that will be retrieved
	 *  @return ListModel with matching name as passed parameter
	 */

	fun getList(string:String): ListModel?{
		return list!!.find { it.name == string }
	}

	fun getShow(list: ArrayList<Show>, id: Int): Show?{
		return list.find { it.id == id }
	}

	/**
	 * Checks if a list with desired name already exists
	 *
	 * @param [string] users desired list name that will be checked against current list
	 * @return Boolean that represents if a list exists with a name that matches passed string
	 */
	fun checkNameTaken(string: String): Boolean{
		return list!!.any { it.name == string }
	}

	/**
	 * Retrieves a list of all of the users list names
	 * @return Arraylist of all of the users list names
	 */
	fun getListNames():ArrayList<String>{
		var retlist = ArrayList<String>()
		list!!.forEach { retlist.add(it.name) }
		return retlist
	}

	/**
	 * Checks if a list contains a show
	 * @param [id] the id of the Show that is being searched for
	 * @param [listname] the name of the list that is being searched for the show id
	 * @return Boolean to represent if the list contains a show with passed ID
	 */
	fun checkListContainsShow(id: Int,listname: String): Boolean{
		var ListModel: ListModel = list!!.find { it.name == listname}!!
		return ListModel.list!!.any { it.id == id }

	}
}