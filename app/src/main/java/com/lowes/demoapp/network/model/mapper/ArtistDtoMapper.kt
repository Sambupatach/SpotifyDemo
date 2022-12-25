package com.lowes.demoapp.network.model.mapper

import com.lowes.demoapp.domain.model.Artist
import com.lowes.demoapp.network.model.ArtistDto
import com.lowes.demoapp.util.ModelMapper

class ArtistDtoMapper: ModelMapper<ArtistDto, Artist> {
    override fun mapToDomainModel(model: ArtistDto): Artist {
        return Artist(
            name = model!!.name
        )
    }

    override fun mapFromDomainModel(domainModel: Artist): ArtistDto {
        TODO("Not yet implemented")
    }

    fun toDomainList(initial: List<ArtistDto>): List<Artist>{
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<Artist>): List<ArtistDto>{
        return initial.map { mapFromDomainModel(it) }
    }
}