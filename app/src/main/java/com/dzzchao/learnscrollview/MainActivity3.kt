package com.dzzchao.learnscrollview

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.dzzchao.learnscrollview.customview.HorizontalScrollViewEx
import com.dzzchao.learnscrollview.utils.MyUtils


/**
 * 外部拦截法
 */
class MainActivity3 : AppCompatActivity() {

    private val TAG = "MainActivity3"

    private lateinit var mListContainer: HorizontalScrollViewEx


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)


        Log.d(TAG, "onCreate")
        initView()
    }

    private fun initView() {
        val inflater = layoutInflater
        mListContainer = findViewById(R.id.container)
        val screenWidth = MyUtils.getScreenMetrics(this).widthPixels
        val screenHeight = MyUtils.getScreenMetrics(this).heightPixels
        for (i in 0..2) {
            val layout = inflater.inflate(
                    R.layout.content_layout, mListContainer, false) as ViewGroup
            layout.layoutParams.width = screenWidth
            val textView = layout.findViewById<View>(R.id.title) as TextView
            textView.text = "page " + (i + 1)
            layout.setBackgroundColor(Color.rgb(255 / (i + 1), 255 / (i + 1), 0))
            createList(layout)
            mListContainer.addView(layout)
        }
    }


    private fun createList(layout: ViewGroup) {
        val listView = layout.findViewById<View>(R.id.list) as ListView
        val datas = ArrayList<String>()
        for (i in 0..49) {
            datas.add("name $i")
        }
        val adapter = ArrayAdapter(this, R.layout.content_list_item, R.id.name, datas)
        listView.adapter = adapter
        listView.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, "click item", Toast.LENGTH_SHORT).show()
        }
    }
}
