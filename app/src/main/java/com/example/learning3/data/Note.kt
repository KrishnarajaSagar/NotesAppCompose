package com.example.learning3.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val lastModified: Long,
    @ColumnInfo("isSelected", defaultValue = "0")
    var isSelected: Boolean = false,
    @ColumnInfo("isPinned", defaultValue = "0")
    var isPinned: Boolean = false
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf<String>(
            "$title$content",
            "$title $content",
            "${title.first()} ${content.first()}"
        )
        return matchingCombinations.any {
            it.contains(query, true)
        }
    }
}