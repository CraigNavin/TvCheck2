package uk.ac.tees.p4061644.tvcheck_redo.models

import com.omertron.themoviedbapi.model.artwork.Artwork

/**
 * Created by Craig on 01/02/2018.
 */
data class Episode (val name: String, val seasonNum: Int, val episodeNum: Int, val images: List<Artwork>, override var watched: Boolean): Watched{

}