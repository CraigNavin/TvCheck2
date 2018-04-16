package uk.ac.tees.p4061644.tvcheck_redo.models


/**
 * This class is a data class an only contains properties and not logic.
 * @constructor Creates a Season object with a  showID, Arraylist of Episode objects, a seasonNumber and a watched boolean
 * Created by Craig on 01/02/2018.
 */

data class Season (val showID: Int, val episodes: ArrayList<Episode>,val seasonNumber: Int,  var watched: Boolean)