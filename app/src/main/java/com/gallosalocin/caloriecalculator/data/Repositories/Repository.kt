package com.gallosalocin.caloriecalculator.data.Repositories

import com.gallosalocin.caloriecalculator.data.LocalDataSource
import com.gallosalocin.caloriecalculator.data.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDataSource,
){
    val remote = remoteDataSource
    val local = localDataSource
}