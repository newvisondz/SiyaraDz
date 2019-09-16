package com.newvisiondz.sayara.screens.myorders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import cn.pedant.SweetAlert.SweetAlertDialog
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.databinding.FragmentMyOrdersBinding
import com.newvisiondz.sayara.model.CommandConfirmed
import com.stripe.android.ApiResultCallback
import com.stripe.android.Stripe
import com.stripe.android.model.Card
import com.stripe.android.model.Token
import kotlinx.android.synthetic.main.e_payment_layout.view.*
import java.lang.Exception

class MyOrders : Fragment() {
    private lateinit var stripe: Stripe
    lateinit var viewModel: MyOrdersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        stripe = Stripe(context!!, "pk_test_ZCo9GAsquoGzEJmQhXef01dq00nx22J82z")

        val binding: FragmentMyOrdersBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_orders, container, false)
        val application = requireNotNull(this.activity).application
        viewModel = ViewModelProviders.of(this, MyOrdersViewModelFactory(application))
            .get(MyOrdersViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        viewModel.paymentAccepted.observe(this, Observer {
            Toast.makeText(context, "Payed With Success", Toast.LENGTH_LONG).show()
        })
        binding.commandeList.adapter = MyOrdersAdapter(MyOrdersAdapter.Listener {
            paymentDialog(it)
        })

        return binding.root
    }

    private fun paymentDialog(command: CommandConfirmed) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(context!!)
        val mView = layoutInflater.inflate(R.layout.e_payment_layout, null)
        builder.setView(mView).setCancelable(true)
        builder.create().setCanceledOnTouchOutside(true)
        val dialog = builder.show()
        mView.payment_info_submit.setOnClickListener {
            payWithCreditCard(mView, command)
            dialog.dismiss()
        }
        mView.payment_info_cancel.setOnClickListener {
            dialog.cancel()
        }
    }


    private fun payWithCreditCard(
        dialog: View,
        command: CommandConfirmed
    ) {
        val card: Card = Card.create("4242424242424242", 12, 2020, "123")
        tokenizeCard(card, command)
//        val cardToSave: Card? = dialog.card_multiline_widget.card
//        if (cardToSave == null) {
//            SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
//                .setTitleText("Invalid Card Data!")
//                .show()
//            return
//        } else {
//
////            if (cardToSave.validateCVC()
////                && cardToSave.validateCard()
////                && cardToSave.validateExpiryDate()
////                && cardToSave.validateNumber()
////            ) {
////
////            }
//        }
    }

    private fun tokenizeCard(card: Card, command: CommandConfirmed) {
        stripe.createToken(
            card,
            object : ApiResultCallback<Token> {
                override fun onError(e: Exception) {
                    SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("A problem occured!")
                        .show()
                }

                override fun onSuccess(token: Token) {
                    viewModel.sendPayementTokentoBackend(command.id, token.id)
                }
            }
        )
    }
}
