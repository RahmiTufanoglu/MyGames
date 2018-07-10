package com.rahmitufanoglu.mygames

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.widget.toast
import com.rahmitufanoglu.mygames.util.NetworkUtil
import kotlinx.android.synthetic.main.activity_web.*
import kotlinx.android.synthetic.main.app_bar_default.*
import kotlinx.android.synthetic.main.progress_bar_default.*

class ShopActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var gameShop: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        gameShop = intent.getStringExtra(STRING_EXTRA_SHOP)

        swipe_refresh_layout_web.run {
            setOnRefreshListener(this@ShopActivity)
            setColorSchemeResources(R.color.colorAccent)
        }

        setActionBar()
        setWebView()
    }

    private fun setActionBar() {
        setSupportActionBar(toolbar_default)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.shop)
        }
    }

    private fun setWebView() {
        web_view.run {
            settings.javaScriptEnabled
            settings.javaScriptCanOpenWindowsAutomatically
            settings.setSupportMultipleWindows(true)
            webViewClient = CustomWebViewClient()
            loadUrl(gameShop)
        }

        swipe_refresh_layout_web.isRefreshing = false
        progress_bar_default.visibility = View.GONE
    }

    private inner class CustomWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            view.loadUrl(request.url.toString())
            return true
        }
    }

    override fun onRefresh() {
        if (NetworkUtil.isNetworkConnected(this)) {
            applicationContext.toast(getString(R.string.refreshing))
            setWebView()
        } else {
            NetworkUtil.isNetworkConnected(this)
            applicationContext.toast(getString(R.string.device_not_connected))
            swipe_refresh_layout_web.isRefreshing = false
            progress_bar_default.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
    }

    companion object {
        private const val STRING_EXTRA_SHOP = "shop"
    }
}
