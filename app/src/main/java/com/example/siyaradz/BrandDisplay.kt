package com.example.siyaradz

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife

class BrandDisplay : AppCompatActivity() {

    @BindView(R.id.brand_recycler_view)
    @Nullable
    lateinit var  recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brand_display)
        ButterKnife.bind(this)
        //recyclerView=findViewById(R.id.brand_recycler_view)
        initRecycerView()
    }

    private fun initRecycerView() {
        val layoutManger =LinearLayoutManager(this)
        recyclerView.layoutManager=(layoutManger)
        recyclerView.setHasFixedSize(true)
    }
}
