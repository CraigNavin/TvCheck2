package uk.ac.tees.p4061644.tvcheck_redo.models

import com.omertron.themoviedbapi.model.tv.TVEpisodeInfo


/**
 * Created by Craig on 01/02/2018.
 */

data class Episode (override var watched: Boolean): Watched{
}