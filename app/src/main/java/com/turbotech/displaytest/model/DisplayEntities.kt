package com.turbotech.displaytest.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "display_entities_tbl")
data class DisplayEntities(

    @PrimaryKey
    val id: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "swipe_screen_test_result")
    val swipeScreenTestResult: Boolean,

    @ColumnInfo(name = "single_touch_test_result")
    val singleTouchTestResult: Boolean,

    @ColumnInfo(name = "result_time")
    val resultTime: Long = System.currentTimeMillis()


)
