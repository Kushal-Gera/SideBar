package kushal.application.gym.DateDatabase

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [DateData::class], version = 1)
abstract class DateDatabase : RoomDatabase() {

    abstract val myDAO : MyDAO

}