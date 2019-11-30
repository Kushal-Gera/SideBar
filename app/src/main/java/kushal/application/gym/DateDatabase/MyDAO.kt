package kushal.application.gym.DateDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MyDAO {

    @Insert
    fun insertDate(date: DateData)


    @Query("select * from dates")//dates here is the table name from dateData class
    fun readDates() : List<DateData>


    @Update
    fun updateDate(data: DateData)

}