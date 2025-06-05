package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class CustomListActivity2 extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private static final String TAG = "CustomListActivity";
    private ListView mylist;
    private Handler handler;
    private ProgressBar progressBar;
    private MyAdapter adapter;
    private String logDate = "";
    private final String DATE_SP_KEY = "lastRateDateStr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list);

        SharedPreferences sp = getSharedPreferences("myrate", Context.MODE_PRIVATE);
        logDate = sp.getString(DATE_SP_KEY, "");
        Log.i("List", "lastRateDateStr=" + logDate);

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 3) {
                    Log.i(TAG, "handleMessage: 获得网络数据");
                    List<RateItem> list2 = (List<RateItem>) msg.obj;
                    adapter = new MyAdapter(CustomListActivity2.this, R.layout.list_item,list2);
                    mylist.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                }
            }
        };

        progressBar = findViewById(R.id.progressBar);
        mylist = findViewById(R.id.mylist2);
        mylist.setOnItemClickListener(this);
        mylist.setOnItemLongClickListener(this);

        new Thread(() -> {
            List<RateItem> retlist = new ArrayList<>();
            Message msg = handler.obtainMessage();
            String curDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
            Log.i("run", "curDateStr:" + curDateStr + " logDate:" + logDate);
            if (curDateStr.equals(logDate)) {
                Log.i("run", "日期相等，从数据库中获取数据");
                RateManager rateManager = new RateManager(CustomListActivity2.this);
                retlist = rateManager.listAll();
            } else {
                Log.i("run", "日期不等，从网络中获取在线数据");
                try {
                    List<RateItem> rateList = new ArrayList<>();
                    URL url = new URL("http://www.usd-cny.com/bankofchina.htm");
                    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                    InputStream in = httpConn.getInputStream();
                    String retStr = new Scanner(in, "GB2312").useDelimiter("\\A").next();
                    Document doc = Jsoup.parse(retStr);
                    Elements tables = doc.getElementsByTag("table");
                    Element retTable = tables.get(5);
                    Elements tds = retTable.getElementsByTag("td");
                    for (int i = 0; i < tds.size(); i += 8) {
                        Element td1 = tds.get(i);
                        Element td2 = tds.get(i + 5);
                        RateItem rateItem = new RateItem(td1.text(), td2.text());
                        rateList.add(rateItem);
                    }
                    RateManager rateManager = new RateManager(CustomListActivity2.this);
                    rateManager.deleteAll();
                    rateManager.addAll(rateList);

                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString(DATE_SP_KEY, curDateStr);
                    edit.apply();
                    retlist = rateList;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            msg.obj = retlist;
            msg.what = 3;
            handler.sendMessage(msg);
        }).start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RateItem item = (RateItem) parent.getItemAtPosition(position);
        new AlertDialog.Builder(this)
                .setTitle("汇率详情")
                .setMessage(item.toString())
                .setPositiveButton("确定", null)
                .show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return true;
    }
}