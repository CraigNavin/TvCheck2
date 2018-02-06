package uk.ac.tees.p4061644.tvcheck_redo.utils

import com.omertron.themoviedbapi.model.tv.*
import uk.ac.tees.p4061644.tvcheck_redo.models.Episode
import uk.ac.tees.p4061644.tvcheck_redo.models.Season
import uk.ac.tees.p4061644.tvcheck_redo.models.Show

/**
 * Created by Craig on 01/02/2018.
 */
class Converter {
	private val tasker: AsyncTasker = AsyncTasker()


	fun convert(TVInfo: TVInfo): Show{
		var seasonList: ArrayList<Season> = ArrayList()

		for (s in TVInfo.seasons){
			seasonList.add(convert(s!!))
		}

		return Show(TVInfo.name,TVInfo.overview, null,
				TVInfo.images,TVInfo.posterPath,false)
	}

	fun convert(TVSeason: TVSeasonBasic): Season{
		var TVSeasonInfo = tasker.getSeason(TVSeason.seasonNumber)
		var episodeList: ArrayList<Episode> = ArrayList()
		for (e in TVSeasonInfo.episodes){
			episodeList.add(convert(e))
		}

		return Season(TVSeasonInfo.overview,episodeList,
				TVSeasonInfo.images,TVSeasonInfo.posterPath,false)

	}

	fun convert(TVEpisodeInfo: TVEpisodeInfo):Episode{

		return Episode(TVEpisodeInfo.name,TVEpisodeInfo.seasonNumber,TVEpisodeInfo.episodeNumber,
				TVEpisodeInfo.images,TVEpisodeInfo.posterPath,false)

	}

/*	fun fullConvert(TVInfo: TVInfo): Show{
		var seasonList: ArrayList<Season> = ArrayList()

		for (s in TVInfo.seasons){
			seasonList.add(convert(s))
		}

		return Show(TVInfo.name,TVInfo.overview,seasonList,TVInfo.images,false)

	}*/

}