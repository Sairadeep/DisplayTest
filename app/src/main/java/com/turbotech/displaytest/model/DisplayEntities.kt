package com.turbotech.displaytest.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "health_report_tbl")
data class DisplayEntities(

    @PrimaryKey
    val id: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "display_test_type_name")
    val testName: String,

    @ColumnInfo(name = "test_started")
    val isTestStarted: Boolean,

    @ColumnInfo(name = "test_result")
    val testResult: Boolean,

)
