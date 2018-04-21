package uk.ac.tees.p4061644.tvcheck_redo.utils

import android.content.Context
import android.util.Log
import com.omertron.themoviedbapi.model.tv.TVInfo
import com.omertron.themoviedbapi.model.tv.TVSeasonBasic
import uk.ac.tees.p4061644.tvcheck_redo.models.Episode
import uk.ac.tees.p4061644.tvcheck_redo.models.Season
import uk.ac.tees.p4061644.tvcheck_redo.models.Show

/**
 * Created by Craig on 01/02/2018.
 */
class Converter {

	/**
	 * Converts a TVInfo object into a Show object to be stored in database.
	 * @param [TVInfo] TVInfo object to be converted into a Show object
	 * @param [context] Activity context to allow access to string resources
	 * @return Show object that was converted from TVInfo object passed as parameter
	 */
	fun convert(TVInfo: TVInfo,context: Context): Show{
		val seasonList: ArrayList<Season> = ArrayList()
		if(TVInfo.seasons.size != 0){
			for (s in TVInfo.seasons){
				seasonList.add(convert(s!!,TVInfo.id,context))
				Log.d("SEASON ADDED","Season:" + s.seasonNumber.toString() + " Added")
			}
		}
		return Show(TVInfo.id,seasonList,false)
	}

	/**
	 * Retrieves TVSeasonInfo object using parameters and then converts TVSeasonInfo object into Season object to be stored in database
	 * @param [TVSeason] TVSeasonBasic object. Used to retrieve TVSeasonInfo objcet
	 * @param [TVid] Id of TV Show. Used to retrieve TVSeasonInfo object
	 * @param [context] Activity context to allow access to string resources
	 */
	fun convert(TVSeason: TVSeasonBasic,TVid: Int,context: Context): Season{
		val TVSeasonInfo = AsyncTasker(context).getSeasonAsync(TVSeason.seasonNumber,TVid)

		val episodeList: ArrayList<Episode> = ArrayList()
		if (TVSeasonInfo!!.episodes != null){
			if (TVSeasonInfo.episodes.size != 0){

				for (e in TVSeasonInfo.episodes){

					episodeList.add(Episode(false))
				}
			}
		}
		return Season(TVid,episodeList,TVSeasonInfo.seasonNumber,false)
	}

}