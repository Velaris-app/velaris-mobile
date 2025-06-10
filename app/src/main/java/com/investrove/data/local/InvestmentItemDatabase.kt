package com.investrove.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.investrove.data.model.InvestmentItem

@Database(entities = [InvestmentItem::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class InvestmentItemDatabase : RoomDatabase() {
    abstract fun collectibleDao(): InvestmentItemDao
}