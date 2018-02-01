package uk.ac.tees.p4061644.tvcheck_redo.models

import android.util.Log
import com.omertron.themoviedbapi.model.artwork.Artwork

/**
 * Created by Craig on 01/02/2018.
 */
data class Season (val overview: String, val episodes: ArrayList<Episode>, val images: List<Artwork>, override var watched: Boolean) : Watched{
	override fun switchWatched() {
		super.switchWatched()
	}

	override fun toString(): String {
		return Log.d("SEASON","Overview: $overview Episodes:${episodes.count()} Images: ${images.count()}  Watched $watched").toString()
	}
}