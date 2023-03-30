package com.earl.treesnavigation.data.localDataSource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.earl.treesnavigation.data.localDataSource.enteties.NodeDb

@Database(
    entities = [
        NodeDb::class
    ],
    exportSchema = false,
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun nodesDao() : NodesDao

}

fun createDatabase(application: Context) =
    Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "db"
    )
        .fallbackToDestructiveMigration()
        .build()