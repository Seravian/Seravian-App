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
            childColumns = ["chat_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["chat_id"])]
)
data class DiagnosisEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long = -1,
    @ColumnInfo(name = "chat_id")
    val chatId: String = "",
    @ColumnInfo(name = "requested_at_utc")
    val requestedAtUtc: String = "",
    @ColumnInfo(name = "completed_at_utc")
    val completedAtUtc: String? = null,
    @ColumnInfo(name = "diagnosed_problem")
    val diagnosedProblem: String? = null,
    @ColumnInfo(name = "reasoning")
    val reasoning: String? = null,
    @ColumnInfo(name = "prescriptions")
    val prescriptions: List<String>? = null,
    @ColumnInfo(name = "failure_reason")
    val failureReason: String? = null
) {
    fun extractDiagnosis() = Diagnosis(
        id = id,
        requestedAtUtc = requestedAtUtc,
        completedAtUtc = completedAtUtc,
        diagnosedProblem = diagnosedProblem,
        reasoning = reasoning,
        prescriptions = prescriptions,
        failureReason = failureReason
    )
}
