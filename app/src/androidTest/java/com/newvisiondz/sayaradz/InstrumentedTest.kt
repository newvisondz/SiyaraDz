package com.newvisiondz.sayaradz

import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
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
                "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVjYzMyZmI2NjU0NDExMDAxOWI5MjRiOSIsInR5cGUiOiJGQUJSSUNBTlQiLCJpYXQiOjE1NjA3NjQyNzAsImV4cCI6MTU2OTMxNzg3MH0.wWFygEwABVWVoWbkvIlLtmpU1-pKw5LrYpu-3Uksr1k",
                "Mercedes","5cc330cc6544110019b924bc"
            )
        val test=call.execute().body()
        assertEquals(test?.name,"model1")
    }
}
