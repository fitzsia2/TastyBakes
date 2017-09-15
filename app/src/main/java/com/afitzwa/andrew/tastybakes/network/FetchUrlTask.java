package com.afitzwa.andrew.tastybakes.network;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Fetches URL Data
 */

public class FetchUrlTask extends AsyncTask<String, Void, String> {
    private final IFetchUrlTask mCaller;
    private final Context mContext;

    public FetchUrlTask(@NonNull IFetchUrlTask cb, @NonNull Context context) {
        mCaller = cb;
        mContext = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        if (NetworkUtil.isConnected(mContext)) {

            String url = strings[0];

            OkHttpClient client = new OkHttpClient();

        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        mCaller.handleFetchUrlResult(s);
    }
}
