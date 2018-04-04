package uk.ac.tees.p4061644.tvcheck_redo.models

/**
 * This class contains a String name and an Arraylist of Show objects.
 * Created by Craig on 27/02/2018.
 */
class ListModel (name: String){

	var name : String
	var list : ArrayList<Show>? = ArrayList<Show>()

	constructor(name : String, list : ArrayList<Show>) : this(name){
		this.list =list
	}
	init{
		this.name = name
	}
}