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

    @Test
    fun testUpdateV(): Unit = runBlocking {
        val id = 123L
        dao.insert(Data(id, emptyList(), emptyList()))
        dao.updateV(id, listOf("a", "b"))

        val e = dao.getById(id)
        assert(e?.id == 123L)
        assert(e?.v == listOf("a", "b"))
    }

    @Test
    fun testUpdateN(): Unit = runBlocking {
        val id = 123L
        dao.insert(Data(id, emptyList(), emptyList()))
        dao.updateN(id, listOf(Nested("a"), Nested("b")))

        val e = dao.getById(id)
        assert(e?.id == 123L)
        println(e?.n)
        assert(e?.n == listOf(Nested("a"), Nested("b")))
    }
}