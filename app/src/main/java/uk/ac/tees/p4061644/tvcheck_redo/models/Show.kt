package uk.ac.tees.p4061644.tvcheck_redo.models

import android.util.Log
import com.omertron.themoviedbapi.model.artwork.Artwork

/**
 * Created by Craig on 01/02/2018.
 */
data class Show (val name: String, val overview: String, val seasons: ArrayList<Season>?, val images: List<Artwork>?, override var watched: Boolean): Watched{
}