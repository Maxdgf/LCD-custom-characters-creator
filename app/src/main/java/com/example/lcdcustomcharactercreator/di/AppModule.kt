package com.example.lcdcustomcharactercreator.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

import com.example.lcdcustomcharactercreator.databases.saved_patterns_database.PatternDao
import com.example.lcdcustomcharactercreator.databases.saved_patterns_database.PatternDatabase
import com.example.lcdcustomcharactercreator.databases.saved_patterns_database.repository.PatternRepository
import com.example.lcdcustomcharactercreator.databases.saved_patterns_database.repository.PatternRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providePatternsDatabase(@ApplicationContext context: Context): PatternDatabase =
        Room.databaseBuilder(
            context,
            PatternDatabase::class.java,
            "notes_database"
        ).fallbackToDestructiveMigration(false).build()

    @Provides
    fun provideNoteDao(patternDatabase: PatternDatabase): PatternDao = patternDatabase.getPatternDao()

    @Singleton
    @Provides
    fun provideNoteRepository(patternDao: PatternDao): PatternRepository = PatternRepositoryImpl(patternDao)
}