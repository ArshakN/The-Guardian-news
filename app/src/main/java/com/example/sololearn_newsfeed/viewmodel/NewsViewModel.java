package com.example.sololearn_newsfeed.viewmodel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.sololearn_newsfeed.network.News;
import com.example.sololearn_newsfeed.repository.NewsRepository;
import com.example.sololearn_newsfeed.persistence.entity.Result;

import java.util.List;

public class NewsViewModel extends ViewModel {


private MutableLiveData<News> mutableLiveData;
private NewsRepository newsRepository;

    public void init(){
        if (mutableLiveData != null){
            return;
        }
        newsRepository = NewsRepository.getInstance();


    }

    public LiveData<News> getAllNews(int page) {
        mutableLiveData = newsRepository.getNewsList(page);
        return mutableLiveData;
    }

    public LiveData<List<Result>> getSavedNews(){

        return newsRepository.getSavedNews();
    }

    public void saveNews(Result result){

        if (newsRepository==null){

            Log.i("LOG","NULL REPO");
        }
        else if (result==null){

            Log.i("LOG","NULL RESULT");
        }
            newsRepository.saveNews(result);



    }


}
