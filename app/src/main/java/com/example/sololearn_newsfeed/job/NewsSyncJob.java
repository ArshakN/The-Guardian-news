package com.example.sololearn_newsfeed.job;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.example.sololearn_newsfeed.R;
import com.example.sololearn_newsfeed.network.ApiManager;
import com.example.sololearn_newsfeed.network.News;
import com.example.sololearn_newsfeed.view.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.sololearn_newsfeed.utils.Utils.API_KEY;
import static com.example.sololearn_newsfeed.utils.Utils.API_THUMBS;
import static com.example.sololearn_newsfeed.utils.Utils.SAVED_ID;


public class NewsSyncJob extends Job {

    public static final String TAG = "new_news_check";
    public static final String CHANNEL_ID = "notification";
    static News newsData;
    SharedPreferences sharedPreferences;


    @SuppressLint("WrongThread")
    @Override
    @NonNull
    protected Result onRunJob(Params params) {
        PendingIntent pi = PendingIntent.getActivity(getContext(), 0,
                new Intent(getContext(), MainActivity.class), 0);
        Log.e("Job", "Job_Run");
        final NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My channel",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("My channel description");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(false);
            notificationManager.createNotificationChannel(channel);
        }
        Intent notificationIntent = new Intent(getContext(), MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0, notificationIntent, 0);

        final NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                        .setAutoCancel(true)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("New News")
                        .setContentText("We have new news")
                        .setContentIntent(contentIntent);

        Call<News> call = ApiManager.getApiClient().getNews(API_KEY, API_THUMBS, 1);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.body() != null && response.isSuccessful()) {
                    newsData = response.body();
                    Log.e("ARSH", "Successs: " + newsData.toString());
                    sharedPreferences = getContext().getSharedPreferences("Main", MODE_PRIVATE);
                    if (!newsData.getResponse().getResults().get(0).getId().equals(sharedPreferences.getString(SAVED_ID, ""))) {
                        Log.e("ARSH", "Successs: " + newsData.getResponse().getResults().get(0).getId() + "\n" + sharedPreferences.getString(SAVED_ID, ""));
                        notificationManager.notify(1, builder.build());
                    }
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.e("ARSH", "OnFailure", t);
            }
        });

        scheduleJob();
        return Result.SUCCESS;
    }

    public static void scheduleJob() {
        new JobRequest.Builder(TAG)
                .startNow()
                .setExact(30000)
                .build()
                .scheduleAsync();
    }
}
