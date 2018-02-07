package uk.ac.tees.p4061644.tvcheck_redo.models

import android.util.Log
import com.omertron.themoviedbapi.model.artwork.Artwork

/**
 * Created by Craig on 01/02/2018.
 */
data class Season (val overview: String, val episodes: ArrayList<Episode>,val seasonNumber: Int, val images: List<Artwork>,val PosterPath : String?, override var watched: Boolean) : Watched{
}