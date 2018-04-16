package uk.ac.tees.p4061644.tvcheck_redo.models


/**
 * This class is a data class an only contains properties and not logic.
 * @constructor Creates a Show object with an ID, Arraylist of Season objects and a watched boolean
 * Created by Craig on 01/02/2018.
 */

data class Show (val id: Int,val seasons: ArrayList<Season>?, var watched: Boolean)