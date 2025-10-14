package com.investrove.di// di/AppModule.kt
import android.content.Context
import androidx.room.Room
import com.investrove.data.local.InvestmentItemDao
import com.investrove.data.local.InvestmentItemDatabase
import com.investrove.data.local.InvestmentStatsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): InvestmentItemDatabase {
        return Room.databaseBuilder(
            context,
            InvestmentItemDatabase::class.java,
            "collectible_db"
        ).build()
    }

    @Provides
    fun provideInvestmentItemDao(db: InvestmentItemDatabase): InvestmentItemDao = db.investmentItemDao()

    @Provides
    fun provideInvestmentStatsDao(db: InvestmentItemDatabase): InvestmentStatsDao = db.investmentStatsDao()
}