package com.team.moblocation.core.di

import com.team.moblocation.core.data.repo.IUserRepo
import com.team.moblocation.core.data.repo.UserRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
	@Provides
	@Singleton
	fun provideUserRepo(): IUserRepo {
		return UserRepo()
	}
}