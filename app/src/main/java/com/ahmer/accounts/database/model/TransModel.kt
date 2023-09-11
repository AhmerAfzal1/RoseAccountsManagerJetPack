package com.ahmer.accounts.database.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ahmer.accounts.utils.Constants
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = Constants.DATABASE_TRANSACTION_TABLE, foreignKeys = [ForeignKey(
        entity = UserModel::class,
        parentColumns = arrayOf("_id"),
        childColumns = arrayOf("UserID"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
    )], indices = [Index(value = ["_id"], unique = true), Index(value = ["UserID"])]
)
@Parcelize
data class TransModel(
    @ColumnInfo(name = "_id")
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "UserID")
    val userId: Int = 0,

    @ColumnInfo(name = "Date")
    val date: String = "",

    @ColumnInfo(name = "Type")
    val type: String = "",

    @ColumnInfo(name = "Description")
    val description: String = "",

    @ColumnInfo(name = "Amount")
    val amount: String = "",

    @ColumnInfo(name = "Created")
    val created: Long = System.currentTimeMillis(),
) : Parcelable
