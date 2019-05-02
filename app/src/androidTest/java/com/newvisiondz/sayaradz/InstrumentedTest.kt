package com.newvisiondz.sayaradz

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.newvisiondz.sayaradz.services.RetrofitClient
import org.junit.Assert.assertEquals
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
                "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVjYjNjNzhmYjc5NTM5MDAxOWY4ZDIzYSIsInR5cGUiOiJBVVRPTU9CSUxJU1RFIiwiaWF0IjoxNTU1OTMyODYwLCJleHAiOjE1NjQ0ODY0NjB9.7RTlNp79COL70yd5VPJO3KTSvTngOX94Y-rrZe4EZWQ",
                "Mercedes","5cc330cc6544110019b924bc"
            )
        val test=call.execute().body()
        assertEquals(test!!.name,"model1")
    }
}
