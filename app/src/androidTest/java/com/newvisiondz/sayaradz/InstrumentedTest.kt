package com.newvisiondz.sayaradz

import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.newvisiondz.sayaradz.model.Version
import com.newvisiondz.sayaradz.services.RetrofitClient
import com.newvisiondz.sayaradz.utils.JsonFormatter
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class InstrumentedTest {
    private val appContext = InstrumentationRegistry.getTargetContext()

    @Test
    fun getModelDetail() {
        val call = RetrofitClient(appContext)
            .serverDataApi
            .getModelDetails(
                "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVjYjNjNzhmYjc5NTM5MDAxOWY4ZDIzYSIsInR5cGUiOiJBVVRPTU9CSUxJU1RFIiwiaWF0IjoxNTYwODA0MjY3LCJleHAiOjE1NjkzNTc4Njd9.kAD2_-3xg7hS84BI3J9J0W8uHV2UgDLKtS1abaKSdWg",
                "Toyota", "5d081201fce5dc0019de8748"
            )
        val test = call.execute().body()
        assertEquals(test?.name, "model1")
    }

    @Test
    fun getAllVersions() {
        val call = RetrofitClient(appContext).serverDataApi.getAllVersion(
            "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVjYjNjNzhmYjc5NTM5MDAxOWY4ZDIzYSIsInR5cGUiOiJBVVRPTU9CSUxJU1RFIiwiaWF0IjoxNTYwODA0MjY3LCJleHAiOjE1NjkzNTc4Njd9.kAD2_-3xg7hS84BI3J9J0W8uHV2UgDLKtS1abaKSdWg",
            "Toyota",
            "5d081201fce5dc0019de8748"
        )
        val modelRes = call.execute().body()
        val listType = object : TypeToken<MutableList<Version>>() {}.type
        val resultList = JsonFormatter.listFormatter<Version>(modelRes!!, listType)
        assertEquals("5d08145efce5dc0019de875b", resultList[0].id)
    }

    @Test
    fun getVersionDetails() {
        val call = RetrofitClient(appContext).serverDataApi.getVersionDetails(
            "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVjYjNjNzhmYjc5NTM5MDAxOWY4ZDIzYSIsInR5cGUiOiJBVVRPTU9CSUxJU1RFIiwiaWF0IjoxNTYwODA0MjY3LCJleHAiOjE1NjkzNTc4Njd9.kAD2_-3xg7hS84BI3J9J0W8uHV2UgDLKtS1abaKSdWg",
            "Toyota",
            "5d081201fce5dc0019de8748", "5d081506fce5dc0019de875c"
        )
        val version = call.execute().body()
        assertEquals(version?.id, "5d081506fce5dc0019de875c")
    }

    @Test
    fun updateUser() {
        val jsonObject= JsonObject()
        jsonObject.addProperty("firstName", "yacine")
        jsonObject.addProperty("lastName", "bka")
        jsonObject.addProperty("address", "medea")
        jsonObject.addProperty("phone", "023122")
        jsonObject.addProperty("birthDate", "2019-02-12")

        val call = RetrofitClient(appContext).serverDataApi.updateUser(
            "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVjYjNjNzhmYjc5NTM5MDAxOWY4ZDIzYSIsInR5cGUiOiJBVVRPTU9CSUxJU1RFIiwiaWF0IjoxNTYwODA0MjY3LCJleHAiOjE1NjkzNTc4Njd9.kAD2_-3xg7hS84BI3J9J0W8uHV2UgDLKtS1abaKSdWg",
            jsonObject
        )
        val nice=call.execute().body()?.get("ok")?.asString
        if (nice == "1") {
            assertTrue(true)
        } else if (nice == "0") {
            assertTrue(false)
        }
    }
}
