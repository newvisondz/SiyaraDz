package com.example.siyaradz.services

import android.util.Log
import com.example.siyaradz.model.Marque
import retrofit2.Call
import retrofit2.Response

class NetworkManager {
    companion object {
        fun getAllBrands(sort: String, select: String, page: String): List<Marque> {
            var brands: List<Marque>? =null

            val call = RetrofitClient()
                .serverDataApi
                .GetAllBrands(sort, select, page)

            call.enqueue(object : retrofit2.Callback<List<Marque>> {
                override fun onResponse(call: Call<List<Marque>>, response: Response<List<Marque>>) {
                    if (response.isSuccessful) {
                        brands = response.body()!!
                        Log.i("success", brands!!.size.toString())

                    } else {
                        Log.i("fail on response", "the format is wrong")
                    }

                }

                override fun onFailure(call: Call<List<Marque>>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        return brands!!
        }
    }


}