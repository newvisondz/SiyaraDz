package com.newvisiondz.sayara

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.newvisiondz.sayara.model.CarInfo
import com.newvisiondz.sayara.model.UsedCar
import com.newvisiondz.sayara.model.Version
import com.newvisiondz.sayara.services.RetrofitClient
import com.newvisiondz.sayara.utils.listFormatter
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Call
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


@RunWith(AndroidJUnit4::class)
class NetworkTesting {
    private val appContext = InstrumentationRegistry.getTargetContext()


    @Test
    fun getUsedCar() {
        val call: Call<List<UsedCar>> =
            RetrofitClient(appContext).serverDataApi.getAllUsedCars("bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVkNWZlZGZmMzVkY2VlMDAxNzI3NWUyOSIsInR5cGUiOiJBVVRPTU9CSUxJU1RFIiwiaWF0IjoxNTY2NTY3OTM1LCJleHAiOjE1NzUxMjE1MzV9.qr9s-WV6nEt23lWDSBFhGRmKKV1Oe1McN37yeIsF_bA",1,10)
        val body = call.execute().body()
        Assert.assertEquals("5d6456f6733e59001709a79d", body!![0].id)
    }

}
