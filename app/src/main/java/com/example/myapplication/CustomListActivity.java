package com.example.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{

    private static final String TAG = "CustomListActivity";
    private ListView mylist;
    Handler handler;
    ProgressBar progressBar;
    MyItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_custom_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                // 接收返回的数据项
                if(msg.what==3){
                    Log.i(TAG, "handleMessage: 获得网络数据");
                    ArrayList<RateItem> list2 =(ArrayList<RateItem>)msg.obj;
                    adapter = new MyItemAdapter(CustomListActivity.this, R.layout.list_item,list2);
                    mylist.setAdapter(adapter);
                    //隐藏进度条
                    progressBar.setVisibility(View.GONE);
                }

                super.handleMessage(msg);
            }
        };



        progressBar = findViewById(R.id.progressBar);

        mylist = findViewById(R.id.mylist2);

        mylist.setOnItemClickListener(this);
        mylist.setOnItemLongClickListener(this);


        //启动线程
        WebItemTask task = new WebItemTask();
        task.setHandler(handler);
        Thread t = new Thread(task);
        t.start();

        // new Thread(()->{
        //     Document doc = null;
        //     ArrayList<HashMap<String, String>> retlist = new ArrayList<HashMap<String, String>>();
        //     try {
        //         Thread.sleep(3000);
        //         doc = Jsoup.connect("https://www.boc.cn/sourcedb/whpj/").get();
        //
        //         Log.i(TAG, "run: title = " + doc.title());
        //
        //
        //
        //         Elements tables = doc.getElementsByTag("table");
        //         Element table = tables.get(1);
        //         Elements trs = table.getElementsByTag("tr");
        //         trs.remove(0);
        //         for(Element tr : trs){
        //             Elements tds = tr.children();
        //             Element td1 = tds.first();
        //             Element td2 = tds.get(5);
        //             String str1 = td1.text();
        //             String str2 = td2.text();
        //
        //             Log.i(TAG, "run: " + str1 + " ==> " + str2);
        //             HashMap<String, String> map = new HashMap<String,String>();
        //             map.put("ItemTitle",str1);
        //             map.put("ItemDetail",str2);
        //             retlist.add(map);
        //
        //         }
        //     } catch (IOException | InterruptedException e) {
        //         throw new RuntimeException(e);
        //     }
        //
        //     Message msg = handler.obtainMessage(3,retlist);
        //     handler.sendMessage(msg);
        //     Log.i(TAG, "onCreate: handler.sendMessage(msg)");
        // }).start();

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Log.i(TAG, "onItemClick: ");
        Object itemAtPosition = mylist.getItemAtPosition(position);
        RateItem item = (RateItem) itemAtPosition;
        String titleStr = item.getCname();
        String detailStr = String.valueOf(item.getCval());
        Log.i(TAG, "onItemClick: titleStr=" + titleStr);
        Log.i(TAG, "onItemClick: detailStr=" + detailStr);

        //删除数据项
        //adapter.remove(mylist.getItemAtPosition(position));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage("请确认是否删除当前数据")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "onClick: 对话框事件处理");
                        //删除数据项
                        adapter.remove(mylist.getItemAtPosition(position));
                    }
                }).setNegativeButton("否", null);
        builder.create().show();

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        Log.i(TAG, "onItemLongClick: ");

        return true;
    }
}