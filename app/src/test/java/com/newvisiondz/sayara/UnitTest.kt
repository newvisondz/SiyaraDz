package com.newvisiondz.sayara

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.newvisiondz.sayara.model.*
import com.newvisiondz.sayara.services.RetrofitClient
import com.newvisiondz.sayara.utils.listFormatter
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Call

class UnitTest {

    @Test
    fun formatModels() {

        val json =
            "{\"models\":[{\"id\":\"5cae7f470092b1001a797e86\",\"name\":\"model1\",\"colors\":[{\"id\":\"5cae7f470092b1001a797e8c\",\"name\":\"red\",\"value\":\"#124568\",\"createdAt\":\"2019-04-10T23:41:59.431Z\",\"updatedAt\":\"2019-04-10T23:41:59.431Z\"}],\"options\":[{\"name\":\"places\",\"values\":[{\"value\":\"6\",\"id\":\"5cae7f470092b1001a797e8b\"},{\"value\":\"4\",\"id\":\"5cae7f470092b1001a797e8a\"},{\"value\":\"8\",\"id\":\"5cae7f470092b1001a797e89\"}]},{\"name\":\"moteur\",\"values\":[{\"value\":\"diesel\",\"id\":\"5cae7f470092b1001a797e88\"},{\"value\":\"d\",\"id\":\"5cae7f470092b1001a797e87\"}]}]},{\"id\":\"5cae7fa40092b1001a797e8d\",\"name\":\"model246\",\"colors\":[{\"id\":\"5cae7fa40092b1001a797e93\",\"name\":\"red\",\"value\":\"#124568\",\"createdAt\":\"2019-04-10T23:43:32.923Z\",\"updatedAt\":\"2019-04-10T23:43:32.923Z\"}],\"options\":[{\"name\":\"places\",\"values\":[{\"value\":\"6\",\"id\":\"5cae7fa40092b1001a797e92\"},{\"value\":\"4\",\"id\":\"5cae7fa40092b1001a797e91\"},{\"value\":\"8\",\"id\":\"5cae7fa40092b1001a797e90\"}]},{\"name\":\"moteur\",\"values\":[{\"value\":\"diesel\",\"id\":\"5cae7fa40092b1001a797e8f\"},{\"value\":\"d\",\"id\":\"5cae7fa40092b1001a797e8e\"}]}]}],\"count\":4}"
        val listType = object : TypeToken<MutableList<Model>>() {}.type
        val model: MutableList<Model> = listFormatter(JsonParser().parse(json), listType, "models")
        println(model[1].colors[0].value)
        assertEquals(model[1].colors[0].value, "#124568")
    }

    @Test
    fun gettingImages() {
        val images =
            "{\"manufacturers\":[{\"id\":\"Toyota\",\"brand\":\"Toyota\",\"logo\":\"public/31af40e4-541b-42e6-8f86-b870bc99bd6f.jpg\",\"createdAt\":\"2019-04-10T23:33:55.188Z\",\"updatedAt\":\"2019-04-10T23:33:55.188Z\"},{\"id\":\"Mercedess1588\",\"brand\":\"Mercedess1588\",\"logo\":\"public/914b594d-2323-44a6-b1b4-b2c37507502f.jpg\",\"createdAt\":\"2019-04-10T22:40:04.748Z\",\"updatedAt\":\"2019-04-10T22:40:04.748Z\"},{\"id\":\"Mercedess158\",\"brand\":\"Mercedess158\",\"logo\":\"public/000d9e8f-3eb7-49fb-a079-645050cc9f2f.jpg\",\"createdAt\":\"2019-04-10T22:38:34.916Z\",\"updatedAt\":\"2019-04-10T22:38:34.916Z\"},{\"id\":\"Mercedess1\",\"brand\":\"Mercedess1\",\"logo\":\"/public/images/logo.png\",\"createdAt\":\"2019-04-10T22:33:36.740Z\",\"updatedAt\":\"2019-04-10T22:33:36.740Z\"},{\"id\":\"Toyota8887d\",\"brand\":\"Toyota8887d\",\"logo\":\"/public/images/logo.png\",\"createdAt\":\"2019-04-06T20:09:28.047Z\",\"updatedAt\":\"2019-04-10T23:43:33.007Z\"}],\"count\":5}"

        val listType = object : TypeToken<MutableList<Brand>>() {}.type
        val brands: MutableList<Brand> =
            listFormatter(JsonParser().parse(images), listType, "manufacturers")
        println(brands[0].logo)
        assertEquals("public/31af40e4-541b-42e6-8f86-b870bc99bd6f.jpg", brands[0].logo)
    }

    @Test
    fun testBidParsing() {
        val test =
            "[{\"id\":\"5d6044eb8ca816001717e2b3\",\"creator\":\"5d5fedff35dcee0017275e29\",\"usedCar\":{\"id\":\"5d5fc986b54a0d001773e38c\",\"title\":\"\\\"\\\"\",\"manufacturer\":\"\\\"Suzuku\\\"\",\"manufacturerId\":\"\\\"Suzuku\\\"\",\"model\":\"\\\"Suzuki ma\\\"\",\"modelId\":\"\\\"5d206ac16fffac001957a65c\\\"\",\"version\":\"\\\"S V1\\\"\",\"versionId\":\"\\\"5d206bf96fffac001957a664\\\"\",\"currentMiles\":456.7,\"registrationDate\":\"2019-07-19T00:00:00.000Z\",\"color\":\"\\\"ff0080ff\\\"\",\"images\":[\"/public/0624a6f05ce8d3411c79b5ed57d89d94\",\"/public/34fad615807f29d0f469c76126926727\"],\"minPrice\":45.9,\"owner\":\"5d5c14aa9ac0010017adaeaf\",\"createdAt\":\"2019-08-23T11:09:58.989Z\"},\"createdAt\":\"2019-08-23T19:56:27.658Z\",\"price\":213.87},{\"id\":\"5d6045758ca816001717e2b4\",\"creator\":\"5d5fedff35dcee0017275e29\",\"usedCar\":{\"id\":\"5d5fc986b54a0d001773e38c\",\"title\":\"\\\"\\\"\",\"manufacturer\":\"\\\"Suzuku\\\"\",\"manufacturerId\":\"\\\"Suzuku\\\"\",\"model\":\"\\\"Suzuki ma\\\"\",\"modelId\":\"\\\"5d206ac16fffac001957a65c\\\"\",\"version\":\"\\\"S V1\\\"\",\"versionId\":\"\\\"5d206bf96fffac001957a664\\\"\",\"currentMiles\":456.7,\"registrationDate\":\"2019-07-19T00:00:00.000Z\",\"color\":\"\\\"ff0080ff\\\"\",\"images\":[\"/public/0624a6f05ce8d3411c79b5ed57d89d94\",\"/public/34fad615807f29d0f469c76126926727\"],\"minPrice\":45.9,\"owner\":\"5d5c14aa9ac0010017adaeaf\",\"createdAt\":\"2019-08-23T11:09:58.989Z\"},\"createdAt\":\"2019-08-23T19:58:45.329Z\",\"price\":564.8},{\"id\":\"5d6045988ca816001717e2b5\",\"creator\":\"5d5fedff35dcee0017275e29\",\"usedCar\":{\"id\":\"5d5fc986b54a0d001773e38c\",\"title\":\"\\\"\\\"\",\"manufacturer\":\"\\\"Suzuku\\\"\",\"manufacturerId\":\"\\\"Suzuku\\\"\",\"model\":\"\\\"Suzuki ma\\\"\",\"modelId\":\"\\\"5d206ac16fffac001957a65c\\\"\",\"version\":\"\\\"S V1\\\"\",\"versionId\":\"\\\"5d206bf96fffac001957a664\\\"\",\"currentMiles\":456.7,\"registrationDate\":\"2019-07-19T00:00:00.000Z\",\"color\":\"\\\"ff0080ff\\\"\",\"images\":[\"/public/0624a6f05ce8d3411c79b5ed57d89d94\",\"/public/34fad615807f29d0f469c76126926727\"],\"minPrice\":45.9,\"owner\":\"5d5c14aa9ac0010017adaeaf\",\"createdAt\":\"2019-08-23T11:09:58.989Z\"},\"createdAt\":\"2019-08-23T19:59:20.951Z\",\"price\":6666.66}]"
        val listType = object : TypeToken<MutableList<UserBid>>() {}.type
        val res = Gson().fromJson<MutableList<UserBid>>(JsonParser().parse(test), listType)
    }



    //

}
