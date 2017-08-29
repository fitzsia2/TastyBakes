package com.afitzwa.andrew.tastybakes.network;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Fetches URL Data
 */

public class FetchUrlTask extends AsyncTask<String, Void, String> {
    private IFetchUrlTask mCaller;

    public FetchUrlTask(IFetchUrlTask cb) {
        mCaller = cb;
    }

    @Override
    protected String doInBackground(String... strings) {
        String url = strings[0];

        OkHttpClient client = new OkHttpClient();

        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        mCaller.handleFetchUrlResult(s);
    }
}
