package uk.ac.tees.p4061644.tvcheck_redo.utils

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.omertron.themoviedbapi.model.tv.*
import uk.ac.tees.p4061644.tvcheck_redo.models.Episode
import uk.ac.tees.p4061644.tvcheck_redo.models.Season
import uk.ac.tees.p4061644.tvcheck_redo.models.Show

/**
 * Created by Craig on 01/02/2018.
 */
class Converter(context: Context) {
	private val tasker = AsyncTasker(context)


	/**
	 * Converts a TVInfo object into a Show object to be stored in database.
	 * @param [TVInfo] TVInfo object to be converted into a Show object
	 * @return Show object that was converted from TVInfo object passed as parameter
	 */
	fun convert(TVInfo: TVInfo): Show{
		var seasonList: ArrayList<Season> = ArrayList()
		if(TVInfo.seasons.size != 0){
			for (s in TVInfo.seasons){
				seasonList.add(convert(s!!,TVInfo.id))
				Log.d("SEASON ADDED","Season:" + s.seasonNumber.toString() + " Added")
			}
		}
		return Show(TVInfo.id,seasonList,false)
	}

	/**
	 * Retrieves TVSeasonInfo object using parameters and then converts TVSeasonInfo object into Season object to be stored in database
	 * @param [TVSeason] TVSeasonBasic object. Used to retrieve TVSeasonInfo objcet
	 * @param [TVid] Id of TV Show. Used to retrieve TVSeasonInfo object
	 */
	fun convert(TVSeason: TVSeasonBasic,TVid: Int): Season{
		var TVSeasonInfo = tasker.getSeasonAsync(TVSeason.seasonNumber,TVid)

		var episodeList: ArrayList<Episode> = ArrayList()
		if (TVSeasonInfo.episodes != null){
			if (TVSeasonInfo.episodes.size != 0){

				for (e in TVSeasonInfo.episodes){

					episodeList.add(Episode(false))
				}
			}
		}
		return Season(TVid,episodeList,TVSeasonInfo.seasonNumber,false)
	}

}