package com.lowes.demoapp.network.model.mapper

import com.lowes.demoapp.domain.model.Album
import com.lowes.demoapp.network.model.AlbumDto
import com.lowes.demoapp.util.ModelMapper

class AlbumDtoMapper : ModelMapper<AlbumDto, Album> {
    var artistMapper = ArtistDtoMapper()
    var imageMapper = ImageDtoMapper()
    override fun mapToDomainModel(model: AlbumDto): Album {
        return Album(
            artists = artistMapper.toDomainList(model!!.artistDtos!!),
            images = imageMapper.toDomainList(model!!.imageDtos!!),
            name = model!!.name
        )
    }

    override fun mapFromDomainModel(domainModel: Album): AlbumDto {
        TODO("Not yet implemented")
    }

    fun toDomainList(initial: List<AlbumDto>): List<Album>{
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<Album>): List<AlbumDto>{
        return initial.map { mapFromDomainModel(it) }
    }
}