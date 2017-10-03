package fr.intech.s5.tp.ws_rest_android.service;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import fr.intech.s5.tp.ws_rest_android.model.DataItem;
import fr.intech.s5.tp.ws_rest_android.utils.HttpHelper;
import fr.intech.s5.tp.ws_rest_android.utils.RequestPackage;

/**
 * Created by Vu Tuan Trung on 9/25/2017.
 */

public class MyIntentService extends IntentService {
    public static final String TAG = "MyIntentService";

    public static final String MY_INTENT_SERVICE_ID = "myIntentServiceID";
    public static final String MY_INTENT_SERVICE_MESSAGE = "myIntentServiceMessage";
    public static final String REQUEST_PACKAGE = "requestPackage";
    public MyIntentService(){
        super("MyIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Dans le onCreate");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent){
        RequestPackage requestPackage = (RequestPackage)intent.getParcelableExtra(REQUEST_PACKAGE);

        String response;
        try{
            response = HttpHelper.downloadFromFeed(requestPackage);
        } catch(IOException e){
            e.printStackTrace();
            return;
        }

        Gson gson = new Gson();
        DataItem[] dataItems = gson.fromJson(response, DataItem[].class);

        Intent messageIntent = new Intent(MY_INTENT_SERVICE_ID);
        messageIntent.putExtra(MY_INTENT_SERVICE_MESSAGE, dataItems);

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(messageIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Dans le onDestroy");
    }
}
