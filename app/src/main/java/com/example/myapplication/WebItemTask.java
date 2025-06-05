package com.example.myapplication;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class WebItemTask implements Runnable{
    private static final String TAG = "CustomListActivity";

    private Handler handler;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        Document doc = null;
        ArrayList<RateItem> retlist = new ArrayList<RateItem>();
        try {
            Thread.sleep(3000);
            doc = Jsoup.connect("https://www.boc.cn/sourcedb/whpj/").get();

            Log.i(TAG, "run: title = " + doc.title());

            Elements tables = doc.getElementsByTag("table");
            Element table = tables.get(1);
            Elements trs = table.getElementsByTag("tr");
            trs.remove(0);
            for(Element tr : trs){
                Elements tds = tr.children();
                Element td1 = tds.first();
                Element td2 = tds.get(5);
                String str1 = td1.text();
                String str2 = td2.text();

                Log.i(TAG, "run: " + str1 + " ==> " + str2);
                // HashMap<String, String> map = new HashMap<String,String>();
                // map.put("ItemTitle",str1);
                // map.put("ItemDetail",str2);
                retlist.add(new RateItem(str1,String.valueOf(str2)));

            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        Message msg = handler.obtainMessage(3,retlist);
        handler.sendMessage(msg);
        Log.i(TAG, "onCreate: handler.sendMessage(msg)");
    }
}