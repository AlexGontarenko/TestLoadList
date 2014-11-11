package com.example.alexg.avitotest.asyncloader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.example.alexg.avitotest.api.API;

/**
 * Created by AlexG on 11.11.2014.
 */
public class DataLoader<ResultResponce> extends AsyncTaskLoader<ResultResponce> {

    public static final int ID = 123;

    private ResultResponce result;
    private API api;

    public DataLoader(Context context) {
        super(context);
        api = new API(context);
    }

    protected ResultResponce doRequest() {
        return (ResultResponce) api.getUsers();
    }

   @Override
    protected void onStartLoading() {
        super.onStartLoading();

        if (result == null || takeContentChanged())
            forceLoad();
        else
            deliverResult(result);
    }

    @Override
    public ResultResponce loadInBackground() {
            return doRequest();
    }

    @Override
    public void deliverResult(ResultResponce data) {
        if (isStarted()) {
            super.deliverResult(data);
            this.result = data;
        }
    }
}
