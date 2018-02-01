package uk.ac.tees.p4061644.tvcheck_redo.models

/**
 * Created by Craig on 01/02/2018.
 */
interface Watched {
	var watched: Boolean
	fun switchWatched(){
		watched = !watched
	}
}