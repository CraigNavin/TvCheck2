package uk.ac.tees.p4061644.tvcheck_redo.models


/**
 * Created by Craig on 01/02/2018.
 */

data class Show (val id: Int,val seasons: ArrayList<Season>?,override var watched: Boolean): Watched{
}