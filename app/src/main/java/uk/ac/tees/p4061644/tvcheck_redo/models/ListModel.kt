package uk.ac.tees.p4061644.tvcheck_redo.models

/**
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