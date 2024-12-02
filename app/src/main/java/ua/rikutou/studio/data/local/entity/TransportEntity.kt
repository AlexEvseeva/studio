package ua.rikutou.studio.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class TransportEntity(
    @PrimaryKey val transportId: Long,
    val type: String,
    val mark: String,
    val manufactureDate: Date,
    val seats: Int,
    val departmentId: Int,
    val color: String,
    val technicalState: String,
)