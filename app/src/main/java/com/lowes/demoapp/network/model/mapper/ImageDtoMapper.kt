package com.lowes.demoapp.network.model.mapper

import com.lowes.demoapp.domain.model.Image
import com.lowes.demoapp.network.model.ImageDto
import com.lowes.demoapp.util.ModelMapper

class ImageDtoMapper: ModelMapper<ImageDto, Image> {
    override fun mapToDomainModel(model: ImageDto): Image {
        return Image(
            height = model!!.height,
            url = model!!.url,
            width = model!!.width
        )
    }

    override fun mapFromDomainModel(domainModel: Image): ImageDto {
        TODO("Not yet implemented")
    }

    fun toDomainList(initial: List<ImageDto>): List<Image>{
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<Image>): List<ImageDto>{
        return initial.map { mapFromDomainModel(it) }
    }
}