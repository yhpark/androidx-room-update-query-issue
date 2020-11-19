package com.example.roomupdateconverterissue

import androidx.room.*


data class Nested(val str: String)

@Entity(tableName = "Data")
data class Data(
    @PrimaryKey
    val id: Long,
    val v: List<String>,
    val n: List<Nested>
)

class Converters {

    @TypeConverter
    fun listStrToString(value: List<String>): String = value.joinToString(",")

    @TypeConverter
    fun stringTolistStr(str: String): List<String> = str.split(",")

    @TypeConverter
    fun nestedToString(value: List<Nested>): String = value.joinToString(",") { it.str }

    @TypeConverter
    fun stringToNested(str: String): List<Nested> = str.split(",").map { Nested(it) }

}

@Dao
interface TestDao {
    @Insert
    suspend fun insert(e: Data)

    @Query("UPDATE Data SET v = :v WHERE id = :id")
    suspend fun updateV(id: Long, v: List<String>) // incorrect!

    @Query("UPDATE Data SET n = :n WHERE id = :id")
    suspend fun updateN(id: Long, n: List<Nested>) // correct

    @Query("SELECT * FROM Data WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): Data?
}

@Database(
    entities = [Data::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class TestDatabase : RoomDatabase() {
    abstract fun dao(): TestDao
}
