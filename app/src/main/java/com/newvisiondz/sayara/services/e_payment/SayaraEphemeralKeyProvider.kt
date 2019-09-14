package com.newvisiondz.sayara.services.e_payment

import androidx.annotation.Size
import com.stripe.android.EphemeralKeyProvider
import com.stripe.android.EphemeralKeyUpdateListener

class SayaraEphemeralKeyProvider  : EphemeralKeyProvider {

//    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
//    private val backendApi: BackendApi =
//        RetrofitFactory.instance.create(BackendApi::class.java)

    override fun createEphemeralKey(
        @Size(min = 4) apiVersion: String,
        keyUpdateListener: EphemeralKeyUpdateListener
    ) {
//        compositeDisposable.add(
//            backendApi.createEphemeralKey(hashMapOf("api_version" to apiVersion))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe { responseBody ->
//                    try {
//                        val ephemeralKeyJson = responseBody.string()
//                        keyUpdateListener.onKeyUpdate(ephemeralKeyJson)
//                    } catch (e: IOException) {
//                        keyUpdateListener
//                            .onKeyUpdateFailure(0, e.message ?: "")
//                    }
//                })
    }
}