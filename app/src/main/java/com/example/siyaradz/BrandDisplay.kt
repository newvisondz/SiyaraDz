package com.example.siyaradz

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife

class BrandDisplay : AppCompatActivity() {
    @BindView(R.id.brand_recycler_view)
    private lateinit var  recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brand_display)
        ButterKnife.bind(this)

    }
}
