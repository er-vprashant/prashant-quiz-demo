package com.prashant.marrowquiz.di

import com.prashant.marrowquiz.data.api.QuizApiService
import com.prashant.marrowquiz.data.api.QuizApiServiceImpl
import com.prashant.marrowquiz.data.repository.QuizRepositoryImpl
import com.prashant.marrowquiz.domain.repository.QuizRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindQuizRepository(
        quizRepositoryImpl: QuizRepositoryImpl
    ): QuizRepository
    
    @Binds
    @Singleton
    abstract fun bindQuizApiService(
        quizApiServiceImpl: QuizApiServiceImpl
    ): QuizApiService
}
