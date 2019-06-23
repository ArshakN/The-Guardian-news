package com.example.sololearn_newsfeed.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.sololearn_newsfeed.network.ApiManager;
import com.example.sololearn_newsfeed.network.News;
import com.example.sololearn_newsfeed.network.TheGuardianService;
import com.example.sololearn_newsfeed.persistence.AppDatabase;
import com.example.sololearn_newsfeed.persistence.DBWrapper;
import com.example.sololearn_newsfeed.persistence.entity.Result;
import com.example.sololearn_newsfeed.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepository {


    private static NewsRepository newsRepository;
    AppDatabase database = DBWrapper.getDatabase();
    private TheGuardianService theGuardianService;


    public NewsRepository() {
        theGuardianService = ApiManager.getApiClient();
    }

    public static NewsRepository getInstance() {
        if (newsRepository == null) {
            newsRepository = new NewsRepository();
        }
        return newsRepository;
    }

    public MutableLiveData<News> getNewsList(int currentPage) {
        final MutableLiveData<News> newsData = new MutableLiveData<>();
        Call<News> call = theGuardianService.getNews(Utils.API_KEY, Utils.API_THUMBS, currentPage);
        call.enqueue(new Callback<News>() {

            @Override
            public void onResponse(Call<News> call, Response<News> response) {

                if (response.body() == null) {
                    Log.e("SSS", "NULL BODY");
                }
                if (!response.isSuccessful()) {
                    Log.e("SSS", "RESPONSE NOT S");
                }
                Log.e("SSS", "ON RESPONSE REPO");
                if (response.body() != null && response.isSuccessful()) {
                    newsData.setValue(response.body());
                    Log.e("SSS", "ON RESPONSE SUCCCESSFUL");
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.e("SSS", "ON Failure");
            }
        });
        return newsData;

    }


    public void saveNews(final Result result) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                database.newsDAO().insert(result);
                return null;
            }
        }.execute();
    }


    public LiveData<List<Result>> getSavedNews() {
        return database.newsDAO().getAll();
    }


}

