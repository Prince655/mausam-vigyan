package com.mausam.vigyan.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.mausam.vigyan.Helper.LocaleHelper
import com.mausam.vigyan.R
import com.mausam.vigyan.adapter.MainAdapter
import com.mausam.vigyan.adapter.MainAdapter.onSelectData
import com.mausam.vigyan.fragments.AboutDialogFragment
import com.mausam.vigyan.model.ModelMain
import com.mausam.vigyan.networking.BloggerApi
import kotlinx.android.synthetic.main.activity_blog.*
import org.json.JSONException
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class BlogActivity : AppCompatActivity(), onSelectData {

    var mainAdapter: MainAdapter? = null
    var mProgressBar: ProgressDialog? = null
    var modelMain: MutableList<ModelMain> = ArrayList()
    lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog)

        mProgressBar = ProgressDialog(this)
        mProgressBar!!.setTitle("Please Wait")
        mProgressBar!!.setCancelable(false)
        mProgressBar!!.setMessage("Loading Weather Reports...")

        IVideo.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.youtube.com/c/PLSNewsLab/")
            startActivity(intent)        }

        llPP.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("http://mfhapp.xyz/privacy/")
            startActivity(intent)
        }

        llDisclaimer.setOnClickListener {
            aboutDialog()
        }

        fabSource.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.youtube.com/c/PLSNewsLab/")
            startActivity(intent)
        }

        rvListArticles.setHasFixedSize(true)
        rvListArticles.setLayoutManager(LinearLayoutManager(this))

        //get data
        listArticle


        mAdView = findViewById(R.id.adView2)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

    }

    private fun aboutDialog() {
        AboutDialogFragment().show(supportFragmentManager, null)
    }

    fun updateView(lang: String?) {
        val context = LocaleHelper.setLocale(this, lang)
        val resources = context.resources
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"))
    }

    private val listArticle: Unit
        private get() {
            mProgressBar!!.show()
            AndroidNetworking.get(BloggerApi.ListPost)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject) {
                            try {
                                mProgressBar!!.dismiss()
                                val playerArray = response.getJSONArray("items")
                                for (i in 0 until playerArray.length()) {
                                    val jsonObject1 = playerArray.getJSONObject(i)
                                    val dataApi = ModelMain()

                                    dataApi.title = jsonObject1.getString("title")
                                    dataApi.content = jsonObject1.getString("content")
                                    dataApi.labels = jsonObject1.getString("labels")
                                    dataApi.url = jsonObject1.getString("url")

                                    val datePost = jsonObject1.getString("published")
                                    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                    val outputFormat = SimpleDateFormat("dd-MM-yyyy")
                                    val date = inputFormat.parse(datePost)
                                    val datePostConvert = outputFormat.format(date)
                                    dataApi.published = datePostConvert

                                    val jsonObject2 = jsonObject1.getJSONObject("author")
                                    val authorPost = jsonObject2.getString("displayName")
                                    dataApi.author = authorPost

                                    val jsonObject3 = jsonObject2.getJSONObject("image")
                                    val authorImage = jsonObject3.getString("url")
                                    dataApi.authorImage = Uri.parse("http:$authorImage").toString()
                                    modelMain.add(dataApi)
                                    showListArticle()
                                }
                            } catch (e: JSONException) {
                                e.printStackTrace()
                                Toast.makeText(this@BlogActivity,
                                        "Failed to display data!", Toast.LENGTH_SHORT).show()
                            } catch (e: ParseException) {
                                e.printStackTrace()
                                Toast.makeText(this@BlogActivity,
                                        "Failed to display data!", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onError(anError: ANError) {
                            mProgressBar!!.dismiss()
                            Toast.makeText(this@BlogActivity,
                                    "Failed to display data!", Toast.LENGTH_SHORT).show()
                        }
                    })
        }

    private fun showListArticle() {
        mainAdapter = MainAdapter(this@BlogActivity, modelMain, this)
        rvListArticles!!.adapter = mainAdapter
    }

    override fun onSelected(modelMain: ModelMain) {
        val intent = Intent(this@BlogActivity, DetailArtikelActivity::class.java)
        intent.putExtra("detailArtikel", modelMain)
        startActivity(intent)
    }
}