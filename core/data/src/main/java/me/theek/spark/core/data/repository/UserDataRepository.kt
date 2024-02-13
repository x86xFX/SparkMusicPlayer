package me.theek.spark.core.data.repository

import kotlinx.coroutines.flow.Flow
import me.theek.spark.core.model.data.UserData

interface UserDataRepository {
    val userData: Flow<UserData>

    suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean)
}