package com.example.data.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "translation_cache",
    indices = [Index(value = ["sourceText", "targetLang"], unique = true)]
)
data class TranslationCacheEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sourceText: String,
    val translatedText: String,
    val engine: String,
    val targetLang: String,
    val timestamp: Long = System.currentTimeMillis()
)
