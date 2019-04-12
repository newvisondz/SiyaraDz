package com.newvisiondz.sayaradz

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.newvisiondz.sayaradz.Utils.JsonFormatter
import com.newvisiondz.sayaradz.model.Model
import org.junit.Test

import org.junit.Assert.*
import com.google.gson.JsonParser
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.newvisiondz.sayaradz.model.Brand


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTest {
    @Test
    fun formatModels() {
        var json ="{\"models\":[{\"id\":\"5cae7f470092b1001a797e86\",\"name\":\"model1\",\"colors\":[{\"id\":\"5cae7f470092b1001a797e8c\",\"name\":\"red\",\"value\":\"#124568\",\"createdAt\":\"2019-04-10T23:41:59.431Z\",\"updatedAt\":\"2019-04-10T23:41:59.431Z\"}],\"options\":[{\"name\":\"places\",\"values\":[{\"value\":\"6\",\"id\":\"5cae7f470092b1001a797e8b\"},{\"value\":\"4\",\"id\":\"5cae7f470092b1001a797e8a\"},{\"value\":\"8\",\"id\":\"5cae7f470092b1001a797e89\"}]},{\"name\":\"moteur\",\"values\":[{\"value\":\"diesel\",\"id\":\"5cae7f470092b1001a797e88\"},{\"value\":\"d\",\"id\":\"5cae7f470092b1001a797e87\"}]}]},{\"id\":\"5cae7fa40092b1001a797e8d\",\"name\":\"model246\",\"colors\":[{\"id\":\"5cae7fa40092b1001a797e93\",\"name\":\"red\",\"value\":\"#124568\",\"createdAt\":\"2019-04-10T23:43:32.923Z\",\"updatedAt\":\"2019-04-10T23:43:32.923Z\"}],\"options\":[{\"name\":\"places\",\"values\":[{\"value\":\"6\",\"id\":\"5cae7fa40092b1001a797e92\"},{\"value\":\"4\",\"id\":\"5cae7fa40092b1001a797e91\"},{\"value\":\"8\",\"id\":\"5cae7fa40092b1001a797e90\"}]},{\"name\":\"moteur\",\"values\":[{\"value\":\"diesel\",\"id\":\"5cae7fa40092b1001a797e8f\"},{\"value\":\"d\",\"id\":\"5cae7fa40092b1001a797e8e\"}]}]}],\"count\":4}"
        var jsonFormatError=JsonFormatter()
        val listType = object : TypeToken<MutableList<Model>>() {}.type
        var model:MutableList<Model> =jsonFormatError.listFormatter(JsonParser().parse(json),listType,"models")
        println(model[1].colors[0].value)
        assertEquals(model[1].colors[0].value,"#124568")
    }
}
