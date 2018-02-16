package com.example.dhruvshah.newburghmap.services;

/**
 * Created by Dhruv Shah on 2/9/2018.
 */

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.dhruvshah.newburghmap.model.DataItem;
import com.example.dhruvshah.newburghmap.utils.HttpHelper;
import com.google.gson.Gson;

import java.io.IOException;

public class MyService extends IntentService {

    public static final String TAG = "MyService";
    public static final String MY_SERVICE_MESSAGE = "myServiceMessage";
    public static final String MY_SERVICE_PAYLOAD = "myServicePayload";

     public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Uri uri = intent.getData();
        Log.i(TAG, "onHandleIntent: " + uri.toString());

        String response;
         try {
             response =  HttpHelper.downloadUrl(uri.toString()).replaceAll("\n", "").replaceAll(" ","");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

//        int firstIndex = response.indexOf('(');
//        int lastIndex = response.lastIndexOf(')');
//        String c = response.substring(firstIndex+1, lastIndex);
//        System.out.println(c);


        Gson gson = new Gson();
        DataItem[] dataItems = gson.fromJson(response,DataItem[].class);



        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
        messageIntent.putExtra(MY_SERVICE_PAYLOAD, response);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(messageIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

}



