package uk.ac.tees.p4061644.tvcheck_redo.models


/**
 * Created by Craig on 01/02/2018.
 */

data class Episode (val name: String, val seasonNum: Int, val overview: String, val episodeNum: Int,val PosterPath : String?, override var watched: Boolean): Watched{

}