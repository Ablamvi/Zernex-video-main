package com.zernex.video.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "zernex_video_prefs")

class FavoritesRepository(private val context: Context) {

    private val favoriteIdsKey = stringSetPreferencesKey("favorite_video_ids")

    val favoriteIds: Flow<Set<Long>> = context.dataStore.data.map { prefs ->
        prefs[favoriteIdsKey]
            ?.mapNotNull { it.toLongOrNull() }
            ?.toSet()
            ?: emptySet()
    }

    suspend fun toggleFavorite(videoId: Long) {
        context.dataStore.edit { prefs ->
            val current = prefs[favoriteIdsKey]?.toMutableSet() ?: mutableSetOf()
            val idStr = videoId.toString()
            if (idStr in current) {
                current.remove(idStr)
            } else {
                current.add(idStr)
            }
            prefs[favoriteIdsKey] = current
        }
    }
}
