package com.jamessaboia.netflixremake.util;

import android.os.AsyncTask;

import com.jamessaboia.netflixremake.model.Category;

import java.util.List;

public class JsonDownloadTask extends AsyncTask<String, Void, List<Category>> {

    // main thread
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    // background thread
    @Override
    protected List<Category> doInBackground(String... params) {
        return null;
    }

    // main thread
    @Override
    protected void onPostExecute(List<Category> categories) {
        super.onPostExecute(categories);
    }
}
