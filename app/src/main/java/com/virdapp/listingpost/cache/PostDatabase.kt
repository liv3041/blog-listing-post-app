package com.virdapp.listingpost.cache

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [UrlCache::class], version = 1)
abstract class PostDatabase : RoomDatabase() {
    abstract fun urlCacheDao(): UrlCacheDao

    companion object {
        @Volatile private var INSTANCE: PostDatabase? = null



        fun getDatabase(context: Context): PostDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PostDatabase::class.java,
                    "cache_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }


}
