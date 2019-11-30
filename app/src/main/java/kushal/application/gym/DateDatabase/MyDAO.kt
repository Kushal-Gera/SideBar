package kushal.application.gym.DateDatabase

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface MyDAO {

    @Insert(onConflict = REPLACE)
    fun insertDate(date: DateData)


    @Query("select * from dates")//dates here is the table name from dateData class
    fun readDates() : List<DateData>


    @Query("delete from dates")
    fun deleteDate()

}