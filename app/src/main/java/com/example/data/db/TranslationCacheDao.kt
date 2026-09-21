package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslationCacheDao {
    @Query("SELECT * FROM translation_cache WHERE sourceText = :sourceText AND targetLang = :targetLang LIMIT 1")
    suspend fun getCached(sourceText: String, targetLang: String): TranslationCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(entry: TranslationCacheEntity)

    @Query("SELECT COUNT(*) FROM translation_cache")
    fun getCacheCount(): Flow<Int>

    @Query("SELECT * FROM translation_cache ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentCache(limit: Int = 100): Flow<List<TranslationCacheEntity>>

    @Query("DELETE FROM translation_cache")
    suspend fun clearAll()

    @Query("DELETE FROM translation_cache WHERE id = :id")
    suspend fun deleteById(id: Long)
}
