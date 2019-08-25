package com.newvisiondz.sayara.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.newvisiondz.sayara.model.UsedCar
import com.newvisiondz.sayara.utils.MutableListConverters

@Dao
interface UsedCarDao {
    @Query("select * from usedCar")
    fun getAds(): LiveData<List<UsedCar>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg cars: UsedCar)

    @Query("DELETE FROM usedCar WHERE usedCar.id=:id;")
    fun deleteOne(id: String)
}

@Database(entities = [UsedCar::class], version = 1, exportSchema = false)
@TypeConverters(MutableListConverters::class)
abstract class UsedCarDatabase : RoomDatabase() {
    abstract val usedCarDao: UsedCarDao
}

private lateinit var INSTANCE: UsedCarDatabase

fun getDatabase(context: Context): UsedCarDatabase {

    synchronized(UsedCarDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE =
                Room.databaseBuilder(context.applicationContext, UsedCarDatabase::class.java, "sayaradz.db").build()
        }
    }

    return INSTANCE
}