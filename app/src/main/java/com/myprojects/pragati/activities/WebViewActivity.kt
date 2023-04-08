package com.myprojects.pragati.activities

import android.os.Bundle
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.myprojects.pragati.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding
    private lateinit var progressBar: ProgressBar
    private var isToolbarVisible = true
    private var previousScrollY = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE

        setSupportActionBar(binding.webViewToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        /// Remove the app name from the Toolbar
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        // Set the toolbar navigation icon to use the default color
        binding.webViewToolbar.navigationIcon?.setTintList(null)

        // Enable options menu for the activity
        invalidateOptionsMenu()


//        val webView = findViewById<WebView>(R.id.webView)
//        webView.settings.javaScriptEnabled = true
//        webView.loadUrl("https://www.w3schools.com/")


        val url = intent.getStringExtra("url")
        binding.webView.loadUrl(url.toString())
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webViewClient = WebViewClient()

        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE // hide progress bar when page is finished loading
//                binding.schoolToolbarText.text = view?.title // set the title of the Toolbar to the title of the webpage
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                progressBar.visibility = View.GONE // hide progress bar when an error occurs
            }
        }

        // Add the following code to hide the Toolbar when the user scrolls
//        val appBarLayout = findViewById<AppBarLayout>(R.id.appBar)
//
//        binding.webView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
//            if (scrollY > previousScrollY && isToolbarVisible) {
//                appBarLayout.animate().translationY(-appBarLayout.height.toFloat())
//                    .setDuration(200).start()
//                isToolbarVisible = false
//            } else if (scrollY < previousScrollY && !isToolbarVisible) {
//                appBarLayout.animate().translationY(0f).setDuration(200).start()
//                isToolbarVisible = true
//            }
//            previousScrollY = scrollY
//        }
    }



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        // Handle the back button press event here
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}