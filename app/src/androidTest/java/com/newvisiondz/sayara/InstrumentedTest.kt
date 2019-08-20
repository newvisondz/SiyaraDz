@file:Suppress("DEPRECATION")

package com.newvisiondz.sayara

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.facebook.internal.Mutable
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.newvisiondz.sayara.model.CarInfo
import com.newvisiondz.sayara.model.Model
import com.newvisiondz.sayara.model.Version
import com.newvisiondz.sayara.services.RetrofitClient
import com.newvisiondz.sayara.utils.listFormatter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import androidx.test.orchestrator.junit.BundleJUnitUtils.getResult
import org.junit.experimental.results.ResultMatchers.isSuccessful
import com.google.firebase.auth.GetTokenResult
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.newvisiondz.sayara.model.UsedCar


@RunWith(AndroidJUnit4::class)
class InstrumentedTest {
    private val appContext = InstrumentationRegistry.getTargetContext()

    @Test
    fun getHashKey() {
        val info: PackageInfo
        try {
            info = appContext.packageManager.getPackageInfo(appContext.packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val something = String(Base64.encode(md.digest(), 0))
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hashkey", something)
            }
        } catch (e1: PackageManager.NameNotFoundException) {
            Log.e("name not found", e1.toString())
        } catch (e: NoSuchAlgorithmException) {
            Log.e("no such an algorithm", e.toString())
        } catch (e: Exception) {
            Log.e("exception", e.toString())
        }

    }


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
        val resultList = listFormatter<Version>(modelRes!!, listType)
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
        val jsonObject = JsonObject()
        jsonObject.addProperty("firstName", "yacine")
        jsonObject.addProperty("lastName", "bka")
        jsonObject.addProperty("address", "medea")
        jsonObject.addProperty("phone", "023122")
        jsonObject.addProperty("birthDate", "2019-02-12")

        val call = RetrofitClient(appContext).serverDataApi.updateUser(
            "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVjYjNjNzhmYjc5NTM5MDAxOWY4ZDIzYSIsInR5cGUiOiJBVVRPTU9CSUxJU1RFIiwiaWF0IjoxNTYwODA0MjY3LCJleHAiOjE1NjkzNTc4Njd9.kAD2_-3xg7hS84BI3J9J0W8uHV2UgDLKtS1abaKSdWg",
            jsonObject
        )
        val nice = call.execute().body()?.get("ok")?.asString
        if (nice == "1") {
            assertTrue(true)
        } else if (nice == "0") {
            assertTrue(false)
        }
    }

    @Test
    fun sendCommand() {
        val queries = mutableListOf("5d090633fce5dc0019de8786", "5d08145efce5dc0019de875b")
        val call = RetrofitClient(appContext).serverDataApi.sendUserCommand(
            "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVjYjNjNzhmYjc5NTM5MDAxOWY4ZDIzYSIsInR5cGUiOiJBVVRPTU9CSUxJU1RFIiwiaWF0IjoxNTYwODA0MjY3LCJleHAiOjE1NjkzNTc4Njd9.kAD2_-3xg7hS84BI3J9J0W8uHV2UgDLKtS1abaKSdWg",
            "Toyota",
            "5d081201fce5dc0019de8748", "5d08145efce5dc0019de875b", queries
        )
        val version = call.execute().body()
        assertEquals(version?.price, 11754)
    }

    @Test
    fun confirmUserCommand() {
        val jsonObject = JsonObject()
        jsonObject.addProperty("vehicule", "5d0d088cbef59411e4db98fe")
        val call = RetrofitClient(appContext).serverDataApi.confirmUserCommande(
            "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVjYjNjNzhmYjc5NTM5MDAxOWY4ZDIzYSIsInR5cGUiOiJBVVRPTU9CSUxJU1RFIiwiaWF0IjoxNTYwODA0MjY3LCJleHAiOjE1NjkzNTc4Njd9.kAD2_-3xg7hS84BI3J9J0W8uHV2UgDLKtS1abaKSdWg",
            jsonObject
        )
        call.execute()
    }

    @Test
    fun testUsedCarGet() {
        var call: Call<List<UsedCar>> =
            RetrofitClient(appContext).serverDataApi.getAllBids("bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVkNWE4Nzg0ZmI2NmRlNDAyYmM5N2IzMSIsInR5cGUiOiJBVVRPTU9CSUxJU1RFIiwiaWF0IjoxNTY2MjQxMjI1LCJleHAiOjE1NzQ3OTQ4MjV9.WhK-rbHO55GaTBcMO1853DIbAthY-4Rt_-uztNMLqsY")
        val mUser = FirebaseAuth.getInstance().currentUser
        mUser!!.getIdToken(true).addOnCompleteListener {
            //            it.result.token
        }

        val body = call.execute().body()
        //bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVkNWE4Nzg0ZmI2NmRlNDAyYmM5N2IzMSIsInR5cGUiOiJBVVRPTU9CSUxJU1RFIiwiaWF0IjoxNTY2MjQxMjI1LCJleHAiOjE1NzQ3OTQ4MjV9.WhK-rbHO55GaTBcMO1853DIbAthY-4Rt_-uztNMLqsY
//        assertEquals("5d585583c4e2ff46b880eff2", body!![0].id)
    }

    @Test
    fun getBrandsList() {
        val call = RetrofitClient(appContext).serverDataApi.getAllVersion(
            "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVjYjNjNzhmYjc5NTM5MDAxOWY4ZDIzYSIsInR5cGUiOiJBVVRPTU9CSUxJU1RFIiwiaWF0IjoxNTYwODA0MjY3LCJleHAiOjE1NjkzNTc4Njd9.kAD2_-3xg7hS84BI3J9J0W8uHV2UgDLKtS1abaKSdWg",
            "Suzuku", "5d206ac16fffac001957a65c"
        )
        val listType = object : TypeToken<MutableList<CarInfo>>() {}.type
        val body: MutableList<CarInfo> = listFormatter(call.execute().body()!!, listType)
        print(body[0].name)
    }

    @Test
    fun getModels() {
        val call = RetrofitClient(appContext).serverDataApi.getAllModels(
            "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVjYjNjNzhmYjc5NTM5MDAxOWY4ZDIzYSIsInR5cGUiOiJBVVRPTU9CSUxJU1RFIiwiaWF0IjoxNTYwODA0MjY3LCJleHAiOjE1NjkzNTc4Njd9.kAD2_-3xg7hS84BI3J9J0W8uHV2UgDLKtS1abaKSdWg",
            "Suzuku", ""
        )
        val listType = object : TypeToken<MutableList<CarInfo>>() {}.type
        val body: MutableList<CarInfo> = listFormatter(call.execute().body()!!, listType, "models")
        print(body[0].name)
    }
}
