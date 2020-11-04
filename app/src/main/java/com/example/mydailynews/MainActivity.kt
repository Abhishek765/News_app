package com.example.mydailynews

import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.BuildConfig
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.news_item.*

class MainActivity : AppCompatActivity(), NewsItemClicked {
    private lateinit var mNewsAdapter: MyNewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    // TODO: 05-11-2020  Try to implement different categories of news in different fragments
        rv_news_list.layoutManager = LinearLayoutManager(this)

        fetchData()
        mNewsAdapter = MyNewsAdapter(this)
        rv_news_list.adapter = mNewsAdapter
    }

    private fun fetchData() {
        val newsApi = BuildConfig.NewsApi
        val url =
            "https://newsapi.org/v2/top-headlines?country=in&apiKey=$newsApi"

        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null,
            {
                val newsJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                Log.e("TAG", "Length:  ${newsJsonArray.length()}")


                for (i in 0 until newsJsonArray.length()) {
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage")
                    )
                    newsArray.add(news)
                }
//                Putting the newsArray into adapter
                mNewsAdapter.upDateNews(newsArray)
            },
//            handling error
            {
//                Toast.makeText(this, it.message ,  Toast.LENGTH_SHORT).show()
                Log.e("TAG", "Error message:  ${it.message}")
            }
        )

        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }
        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(items: News) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(items.url))
    }

}