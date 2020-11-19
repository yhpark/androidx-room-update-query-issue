package com.example.roomupdateconverterissue

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DemoTest {
    private lateinit var db: TestDatabase
    private lateinit var dao: TestDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, TestDatabase::class.java).build()
        dao = db.dao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    /**
     * This works
     */
    @Test
    fun testInsert(): Unit = runBlocking {
        val id = 123L
        val data = Data(id, v = listOf("a", "b"), n = listOf(Nested("c"), Nested("d")))
        dao.insert(data)

        val inserted = dao.getById(id)
        assert(inserted == data)
    }

    /**
     * This also works
     */
    @Test
    fun testUpdateN(): Unit = runBlocking {
        val id = 123L
        dao.insert(Data(id, emptyList(), emptyList()))
        dao.updateN(id, listOf(Nested("a"), Nested("b")))

        val e = dao.getById(id)
        assert(e?.id == 123L)
        assert(e?.n == listOf(Nested("a"), Nested("b")))
    }

    /**
     * Broken!
     */
    @Test
    fun testUpdateV(): Unit = runBlocking {
        val id = 123L
        dao.insert(Data(id, emptyList(), emptyList()))
        dao.updateV(id, listOf("a", "b"))

        val e = dao.getById(id)
        assert(e?.id == 123L)
        assert(e?.v == listOf("a", "b"))
    }
}