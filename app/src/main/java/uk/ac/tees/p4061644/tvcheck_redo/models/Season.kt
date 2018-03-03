package uk.ac.tees.p4061644.tvcheck_redo.models


/**
 * Created by Craig on 01/02/2018.
 */

data class Season (val overview: String, val episodes: ArrayList<Episode>,val seasonNumber: Int,val PosterPath : String?, override var watched: Boolean) : Watched{
}