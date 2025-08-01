package com.salsa.clone.salsaclone.di

import com.salsa.clone.salsaclone.data.api.CreatorApi
import com.salsa.clone.salsaclone.data.repository.CreatorRepository
import com.salsa.clone.salsaclone.data.repository.FakeSearchRepository
import com.salsa.clone.salsaclone.data.repository.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideCreatorApi(): CreatorApi = CreatorApi.create()

    @Provides
    fun provideCreatorRepo(api: CreatorApi) = CreatorRepository(api)

    @Provides
    @Singleton
    fun provideSearchRepository(): SearchRepository = FakeSearchRepository()

}