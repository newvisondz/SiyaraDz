package com.example.siyaradz.views

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.siyaradz.R
import com.example.siyaradz.adapters.MarquesAdapter
import com.example.siyaradz.model.Marque
import kotlinx.android.synthetic.main.activity_brand_display.*

class BrandDisplay : AppCompatActivity() {
    private lateinit var brands:MutableList<Marque>
    private var adapter:MarquesAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brand_display)

        getContect()
        initRecycerView()
    }

    private fun getContect() {
        this.brands = mutableListOf(Marque(1,"audi"),Marque(2,"Renault"),Marque(3,"Graves"))
    }

    private fun initRecycerView() {
        val layoutManger =LinearLayoutManager(this)
        brand_recycler_view.layoutManager=(layoutManger)
        brand_recycler_view.setHasFixedSize(true)
        this.adapter= MarquesAdapter(this.brands,this)
        brand_recycler_view.adapter=this.adapter
    }
}
