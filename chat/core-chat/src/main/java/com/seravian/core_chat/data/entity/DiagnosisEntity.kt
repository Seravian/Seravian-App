package com.seravian.core_chat.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.seravian.core_chat.domain.models.Diagnosis

@Entity(
    tableName = "diagnoses",
    foreignKeys = [
        ForeignKey(
            entity = ChatEntity::class,
            parentColumns = ["id"],
            childColumns = ["chatId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["chatId"])]
)
data class DiagnosisEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long = -1,
    @ColumnInfo(name = "chatId")
    val chatId: String = "",
    @ColumnInfo(name = "description")
    val description: String? = null,
    @ColumnInfo(name = "requestedAtUtc")
    val requestedAtUtc: String = "",
    @ColumnInfo(name = "completedAtUtc")
    val completedAtUtc: String? = null
) {
    fun extractDiagnosis() = Diagnosis(
        id = id,
        description = description,
        requestedAtUtc = requestedAtUtc,
        completedAtUtc = completedAtUtc
    )
}
