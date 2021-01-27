package com.mausam.vigyan.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mausam.vigyan.model.ModelMain
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.mausam.vigyan.Helper.LocaleHelper
import com.mausam.vigyan.R
import kotlinx.android.synthetic.main.activity_detail_artikel.*
import org.jsoup.Jsoup

class DetailArtikelActivity : AppCompatActivity() {

    var modelMain: ModelMain? = null
    var strTitle: String? = null
    var strDate: String? = null
    var strKetDetail: String? = null
    var strImgDetail: String? = null
    var strUrl: String? = null
    lateinit var mAdView : AdView

    //WebView webView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_artikel)

        setSupportActionBar(toolbar_detail)
        assert(supportActionBar != null)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = null

        progressBar.max = 100
        modelMain = intent.getSerializableExtra("detailArtikel") as ModelMain
        progressBar.progress = 0

        if (modelMain != null) {
            strTitle = modelMain!!.title
            strDate = modelMain!!.published
            strKetDetail = modelMain!!.content
            strUrl = modelMain!!.url

            val document = Jsoup.parse(modelMain!!.content.also { strImgDetail = it })
            val element = document.select("img")
            Glide.with(this)
                    .load(element[0].attr("src"))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgDetail)

            progressBar.visibility = View.GONE

            tvTitle.text = strTitle
            tvTitle.isSelected = true
            tvDate.text = strDate

            //webView.loadData(strKetDetail, "text/html; charset=utf-8", "UTF-8");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                tvKetDetail.text = Html.fromHtml(strKetDetail,
                        Html.FROM_HTML_MODE_LEGACY) else {
                tvKetDetail.text = Html.fromHtml(strKetDetail)
            }

            fabSource.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://www.youtube.com/c/PLSNewsLab/")
                startActivity(intent)
            }

            mAdView = findViewById(R.id.adView3)
            val adRequest = AdRequest.Builder().build()
            mAdView.loadAd(adRequest)


        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun updateView(lang: String?) {
        val context = LocaleHelper.setLocale(this, lang)
        val resources = context.resources
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"))
    }
}