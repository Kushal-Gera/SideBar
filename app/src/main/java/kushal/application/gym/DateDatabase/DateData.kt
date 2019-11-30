package kushal.application.gym.DateDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "dates")
public class DateData {

    @PrimaryKey(autoGenerate = true)
    lateinit var ID: Integer

    lateinit var date: String

}