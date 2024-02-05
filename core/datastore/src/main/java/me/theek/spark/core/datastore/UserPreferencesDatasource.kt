package me.theek.spark.core.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.theek.spark.core.model.data.UserData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesDatasource @Inject constructor(private val userPreferences: DataStore<UserPreferences>) {

    val userData: Flow<UserData> = userPreferences.data.map {
        UserData(shouldHideOnboarding = it.shouldHideOnboarding)
    }

    suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        userPreferences.updateData {
            it.copy { this.shouldHideOnboarding = shouldHideOnboarding }
        }
    }
}