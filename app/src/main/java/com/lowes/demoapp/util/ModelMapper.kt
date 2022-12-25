package com.lowes.demoapp.util

interface ModelMapper <T, DomainModel>{

    fun mapToDomainModel(model: T): DomainModel
    fun mapFromDomainModel(domainModel: DomainModel): T
}