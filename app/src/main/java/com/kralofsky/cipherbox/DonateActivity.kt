package com.kralofsky.cipherbox

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*


object DonateEntry : MainMenuEntry {
    override val activity = DonateActivity::class
    override val name ="Buy me a Coffe"
    override val description = "If you like this app, please consider donating"
    override val imageId = R.drawable.coffee
}

class DonateActivity : AppCompatActivity(), PurchasesUpdatedListener {
    private lateinit var billingClient: BillingClient

    private val textbox = lazy { this.findViewById<TextView>(R.id.donatetext) }
    private val donationbtnlist = lazy {this.findViewById<LinearLayout>(R.id.donationbuttons) }

    private val skuList = listOf(
        "com.kralofsky.cipherbox.donation.05",
        "com.kralofsky.cipherbox.donation.1",
        "com.kralofsky.cipherbox.donation.5",
        "com.kralofsky.cipherbox.donation.10"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.donatelayout)

        billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                    loadAllSKUs()
                    consumeAllPurchases()
                }
            }
            override fun onBillingServiceDisconnected() {
                textbox.value.text = "ERROR: onBillingServiceDisconnected"
            }
        })
    }

    private fun loadAllSKUs() = if (billingClient.isReady) {
        val params = SkuDetailsParams
            .newBuilder()
            .setSkusList(skuList)
            .setType(BillingClient.SkuType.INAPP)
            .build()
        billingClient.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
            // Process the result.
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList.isNotEmpty()) {
                skuDetailsList.sortBy { it.priceAmountMicros }
                for (skuDetails in skuDetailsList) {
                    val button = Button(this)
                    donationbtnlist.value.addView(button)
                    button.text = skuDetails.price
                    button.setOnClickListener {
                        val billingFlowParams = BillingFlowParams
                            .newBuilder()
                            .setSkuDetails(skuDetails)
                            .build()
                        billingClient.launchBillingFlow(this, billingFlowParams)
                    }
                }
            }
        }

    } else {
        println("Billing Client not ready")
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult?,
        purchases: MutableList<Purchase>?
    ) {
        if (billingResult?.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                acknowledgePurchase(purchase.purchaseToken)
                consumeAllPurchases()
            }
        } else if (billingResult?.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.

        } else {
            // Handle any other error codes.
        }
    }

    private fun acknowledgePurchase(purchaseToken: String) {
        val params = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchaseToken)
            .build()
        billingClient.acknowledgePurchase(params) { billingResult ->
            val responseCode = billingResult.responseCode
            val debugMessage = billingResult.debugMessage
        }
    }

    private fun consumeAllPurchases() {
        val pr = billingClient.queryPurchases(BillingClient.SkuType.INAPP)
        val pList = pr.purchasesList
        for (iitem in pList) {
            val consumeParams = ConsumeParams.newBuilder()
                .setPurchaseToken(iitem.purchaseToken)
                .build()
            billingClient.consumeAsync(consumeParams) { billingResult: BillingResult, s: String -> }
        }
    }
}