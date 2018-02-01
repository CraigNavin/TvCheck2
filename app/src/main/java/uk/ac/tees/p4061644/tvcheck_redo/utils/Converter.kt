package uk.ac.tees.p4061644.tvcheck_redo.utils

import com.omertron.themoviedbapi.model.tv.TVBasic
import com.omertron.themoviedbapi.model.tv.TVEpisodeBasic
import com.omertron.themoviedbapi.model.tv.TVInfo
import com.omertron.themoviedbapi.model.tv.TVSeasonBasic
import uk.ac.tees.p4061644.tvcheck_redo.models.Episode
import uk.ac.tees.p4061644.tvcheck_redo.models.Season
import uk.ac.tees.p4061644.tvcheck_redo.models.Show

/**
 * Created by Craig on 01/02/2018.
 */
class Converter {
	private var show: Show? = null
	private var seasonw: Season? = null
	private var episode: Episode? = null
	private val tasker: AsyncTasker = AsyncTasker()


	fun convert(TVBasic: TVBasic): Show{
		var TVInfo = tasker.getShow(TVBasic.id)
		show = Show(TVInfo.name,TVInfo.overview, null,
				TVInfo.images,false)
		return show!!
	}

	fun convert(TVSeason: TVSeasonBasic): Season{
		var TVSeasonInfo = tasker.getSeason(TVSeason.id)
		var episodeList: ArrayList<Episode> = ArrayList()
		for (e in TVSeasonInfo.episodes){
			episodeList.add(convert(e))
		}

		seasonw = Season(TVSeasonInfo.overview,episodeList,
				TVSeasonInfo.images,false)
		return seasonw!!

	}

	fun convert(TVEpisode: TVEpisodeBasic):Episode{
		var TVEpisodeInfo = tasker.getEpisode(TVEpisode.episodeNumber)
		episode = Episode(TVEpisodeInfo.name,TVEpisodeInfo.seasonNumber,TVEpisodeInfo.episodeNumber,
				TVEpisodeInfo.images,false)
		return episode!!

	}

	fun fullConvert(TVInfo: TVInfo): Show{
		var seasonList: ArrayList<Season> = ArrayList()

		for (s in TVInfo.seasons){
			seasonList.add(convert(s))
		}

		return Show(TVInfo.name,TVInfo.overview,seasonList,TVInfo.images,false)

	}

}