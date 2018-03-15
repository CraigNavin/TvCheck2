package uk.ac.tees.p4061644.tvcheck_redo.utils

import android.content.Context
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

	fun convert(TVSeason: TVSeasonBasic,TVid: Int): Season{


		var TVSeasonInfo = tasker.getSeasonAsync(TVSeason.seasonNumber,TVid)

		var episodeList: ArrayList<Episode> = ArrayList()
		if (TVSeasonInfo.episodes != null){
			if (TVSeasonInfo.episodes.size != 0){

				for (e in TVSeasonInfo.episodes){

					episodeList.add(convert())
				}
			}
		}


		return Season(TVid,episodeList,TVSeasonInfo.seasonNumber,false)

	}

	fun convert():Episode{

		return Episode(false)
	}

/*	fun fullConvert(TVInfo: TVInfo): Show{
		var seasonList: ArrayList<Season> = ArrayList()

		for (s in TVInfo.seasons){
			seasonList.add(convert(s))
		}

		return Show(TVInfo.name,TVInfo.overview,seasonList,TVInfo.images,false)

	}*/

}