package com.example.sololearn_newsfeed;

import android.app.Application;

import com.evernote.android.job.JobManager;
import com.example.sololearn_newsfeed.job.NewsCheckJobCreator;
import com.example.sololearn_newsfeed.job.NewsSyncJob;
import com.example.sololearn_newsfeed.persistence.DBWrapper;
import com.example.sololearn_newsfeed.utils.NetworkCheck;

public class App extends Application {

    public static boolean isNetworkEnabled;

    @Override
    public void onCreate() {
        super.onCreate();
        DBWrapper.create(this);
        JobManager.create(this).addJobCreator(new NewsCheckJobCreator());
        NewsSyncJob.scheduleJob();
        isNetworkEnabled = NetworkCheck.isNetworkAvailable(this);

    }


}
