package com.newvisiondz.sayara

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.newvisiondz.sayara.database.UsedCarDao
import com.newvisiondz.sayara.database.UsedCarDatabase
import com.newvisiondz.sayara.database.getDatabase
import com.newvisiondz.sayara.model.Brand
import com.newvisiondz.sayara.model.Model
import com.newvisiondz.sayara.model.UsedCar
import com.newvisiondz.sayara.model.Version
import com.newvisiondz.sayara.services.RetrofitClient
import com.newvisiondz.sayara.utils.listFormatter
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class NetworkTesting {
    private val appContext = InstrumentationRegistry.getTargetContext()
    private val call = RetrofitClient(appContext).serverDataApi
    private lateinit var dataBase: UsedCarDatabase
    private lateinit var usedCarDao: UsedCarDao


    @Before
    fun initDb() {
        dataBase = Room.inMemoryDatabaseBuilder(appContext, UsedCarDatabase::class.java).build()
        usedCarDao = dataBase.usedCarDao
    }

    @After
    fun closeDb() {
        dataBase.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertBufferoosSavesData() {
        val usedCarDao = dataBase.usedCarDao
        val usedCar = UsedCar("1", "Toyota")
        usedCarDao.insertAll(usedCar)
        val count = usedCarDao.getCount()
        Assert.assertEquals(1, count)
    }


    @Test
    fun getAllUsedCar() {
        val res = call.getAllUsedCars(
            "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVkNWZlZGZmMzVkY2VlMDAxNzI3NWUyOSIsInR5cGUiOiJBVVRPTU9CSUxJU1RFIiwiaWF0IjoxNTY2NTY3OTM1LCJleHAiOjE1NzUxMjE1MzV9.qr9s-WV6nEt23lWDSBFhGRmKKV1Oe1McN37yeIsF_bA",
            1,
            10
        )
        val body = res.execute().body()
        Assert.assertEquals("5d78303c7291f50017be6f93", body!![0].id)
    }

    @Test
    fun getAllBrands() {
        val res = call.getAllBrands(
            "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVkNWZlZGZmMzVkY2VlMDAxNzI3NWUyOSIsInR5cGUiOiJBVVRPTU9CSUxJU1RFIiwiaWF0IjoxNTY2NTY3OTM1LCJleHAiOjE1NzUxMjE1MzV9.qr9s-WV6nEt23lWDSBFhGRmKKV1Oe1McN37yeIsF_bA",
            1, 22, ""
        )
        val body = res.execute().body()
        val listType = object : TypeToken<MutableList<Brand>>() {}.type
        val brands: MutableList<Brand> = listFormatter(body!!, listType, "manufacturers")
        Assert.assertEquals("TToyota2fdasd", brands[0].id)
    }

    @Test
    fun getAllModelOfBrand() {
        val res = call.getAllModels(
            "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVkNWZlZGZmMzVkY2VlMDAxNzI3NWUyOSIsInR5cGUiOiJBVVRPTU9CSUxJU1RFIiwiaWF0IjoxNTY2NTY3OTM1LCJleHAiOjE1NzUxMjE1MzV9.qr9s-WV6nEt23lWDSBFhGRmKKV1Oe1McN37yeIsF_bA",
            "Suzuku", 1, 22
        )
        val body = res.execute().body()
        val listType = object : TypeToken<MutableList<Model>>() {}.type
        val model: MutableList<Model> = listFormatter(body!!, listType, "models")
        Assert.assertEquals("5d206ac16fffac001957a65c", model[0].id)

    }

    @Test
    fun getAllVersionsOfModel() {
        val res = call.getAllVersion(
            "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVkNWZlZGZmMzVkY2VlMDAxNzI3NWUyOSIsInR5cGUiOiJBVVRPTU9CSUxJU1RFIiwiaWF0IjoxNTY2NTY3OTM1LCJleHAiOjE1NzUxMjE1MzV9.qr9s-WV6nEt23lWDSBFhGRmKKV1Oe1McN37yeIsF_bA",
            "Suzuku", "5d206ac16fffac001957a65c"
        )
        val body = res.execute().body()
        val listType = object : TypeToken<MutableList<Version>>() {}.type
        val versions: MutableList<Version> = listFormatter(body!!, listType)
        Assert.assertEquals("5d206bf96fffac001957a664", versions[0].id)
    }

}
