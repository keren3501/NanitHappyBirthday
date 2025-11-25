package com.example.nanithappybirthday.DI

import android.content.Context
import com.example.nanithappybirthday.model.NanitRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideNanitRepository(
        @ApplicationContext context: Context
    ): NanitRepository {
        return NanitRepository(context)
    }

}