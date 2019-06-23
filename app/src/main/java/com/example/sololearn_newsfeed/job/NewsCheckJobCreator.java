package com.example.sololearn_newsfeed.job;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

public class NewsCheckJobCreator implements JobCreator {

    @Override
    @Nullable
    public Job create(@NonNull String tag) {
        switch (tag) {
            case NewsSyncJob.TAG:
                return new NewsSyncJob();
            default:
                return null;
        }
    }
}
