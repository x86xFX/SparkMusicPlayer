package me.theek.spark.core.data.repository

import kotlinx.coroutines.flow.Flow
import me.theek.spark.core.datastore.UserPreferencesDatasource
import me.theek.spark.core.model.data.UserData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepository @Inject constructor(private val userPreferencesDatasource: UserPreferencesDatasource) : UserDataRepository {

    override val userData: Flow<UserData> = userPreferencesDatasource.userData

    override suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        userPreferencesDatasource.setShouldHideOnboarding(shouldHideOnboarding)
    }
}