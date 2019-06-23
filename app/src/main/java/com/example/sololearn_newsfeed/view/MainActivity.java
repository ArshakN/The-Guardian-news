package com.example.sololearn_newsfeed.view;

import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.sololearn_newsfeed.App;
import com.example.sololearn_newsfeed.R;
import com.example.sololearn_newsfeed.adapter.NewsAdapter;
import com.example.sololearn_newsfeed.adapter.PinNewsAdapter;
import com.example.sololearn_newsfeed.network.ApiManager;
import com.example.sololearn_newsfeed.network.News;
import com.example.sololearn_newsfeed.persistence.entity.Result;
import com.example.sololearn_newsfeed.utils.Utils;
import com.example.sololearn_newsfeed.viewmodel.NewsViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sololearn_newsfeed.utils.Utils.API_KEY;
import static com.example.sololearn_newsfeed.utils.Utils.API_THUMBS;
import static com.example.sololearn_newsfeed.utils.Utils.BUNDLE_KEY;
import static com.example.sololearn_newsfeed.utils.Utils.SAVED_ID;
import static com.example.sololearn_newsfeed.utils.Utils.pinResult;

public class MainActivity extends AppCompatActivity implements NewsAdapter.OnNewsClickListener, PinNewsAdapter.OnPinNewsClickListener {

    private static News newsData;
    private RecyclerView verticalRecV;
    private RecyclerView horizonalRecV;
    private NewsViewModel newsViewModel;
    private PinNewsAdapter pinNewsAdapter;
    private NewsAdapter newsAdapter;
    private SharedPreferences sPref;
    private ImageView imageView;
    private SwipeRefreshLayout swipeRefresh;
    private Boolean isScrolling = false;
    private int visible_items, total_items, scroll_out_items;
    private ProgressBar progressBar;
    private GridLayoutManager gridLayoutManager;
    private int pages = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewInit();
        setUpHorizontalRV();
        Log.e("SSS", "ON CREATE");
        gridLayoutManager = new GridLayoutManager(MainActivity.this,1);
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        newsViewModel.init();
        if (App.isNetworkEnabled == true) {
            swipeRefresh.setOnRefreshListener((() -> getNewNews()));
            newsViewModel.getAllNews(Utils.newsPage).observe(this, new Observer<News>() {
                @Override
                public void onChanged(@Nullable News news) {
                    Log.e("SSS", "ON CHANGED");
                    setupRecyclerView();
                    newsAdapter.setNewsList((ArrayList<Result>) news.getResponse().getResults());
                    savelastid(news.getResponse().getResults().get(0).getId());
                }
            });
        } else {
            setupRecyclerView();
            newsViewModel.getSavedNews().observe(this, new Observer<List<Result>>() {
                @Override
                public void onChanged(@Nullable List<Result> results) {
                    newsAdapter.setNewsList((ArrayList<Result>) results);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("PIN", "On Resume " + String.valueOf(pinResult.size()));
        Log.e("SSSS", "On Resume");
        if (pinResult.size() > 0) {
            pinNewsAdapter.setNewsList(pinResult);
        }

    }


    private void viewInit() {
        verticalRecV = findViewById(R.id.vert_recycler_view);
        horizonalRecV = findViewById(R.id.hr_recycler_view);
        imageView = findViewById(R.id.image_id);
        swipeRefresh = findViewById(R.id.swiperefresh);
        progressBar = findViewById(R.id.progressBar_id);

    }


    private void setupRecyclerView() {
        if (newsAdapter == null) {
            newsAdapter = new NewsAdapter(MainActivity.this);
            verticalRecV.setLayoutManager(gridLayoutManager);
            verticalRecV.setAdapter(newsAdapter);
            verticalRecV.setItemAnimator(new DefaultItemAnimator());
            verticalRecV.setNestedScrollingEnabled(true);
            verticalRecV.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        isScrolling = true;
                    }
                }
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    visible_items = gridLayoutManager.getChildCount();
                    total_items = gridLayoutManager.getItemCount();
                    scroll_out_items = gridLayoutManager.findFirstVisibleItemPosition();
                    if (isScrolling && visible_items + scroll_out_items == total_items) {
                        isScrolling = false;
                        progressBar.setVisibility(View.VISIBLE);
                        pages++;
                        fillNews();
                    }
                }
            });
            newsAdapter.setOnNewsClickListener(this);
        } else {
            newsAdapter.notifyDataSetChanged();
        }
    }

    private void setUpHorizontalRV() {
        pinNewsAdapter = new PinNewsAdapter(MainActivity.this);
        horizonalRecV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        horizonalRecV.setAdapter(pinNewsAdapter);
        horizonalRecV.setItemAnimator(new DefaultItemAnimator());
        horizonalRecV.setNestedScrollingEnabled(true);
        pinNewsAdapter.setNewsList(pinResult);
        pinNewsAdapter.setOnPinNewsClickListener(this);
    }
    @Override
    public void onNewsClick(Result currentNews, View viewRoot) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_KEY, currentNews);
        Intent intent = new Intent(this, Detailed_Activity.class);
        intent.putExtra("A", bundle);
        intent.putExtra("B", currentNews.getThumbnail().getThumbnail());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Fade(Fade.IN));
            getWindow().setEnterTransition(new Fade(Fade.OUT));
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(
                            this,
                            new Pair<View, String>(viewRoot.findViewById(R.id.image_id), "image_trans"),
                            new Pair<View, String>(viewRoot.findViewById(R.id.category_id), "title_trans"),
                            new Pair<View, String>(viewRoot.findViewById(R.id.news_title), "text_trans"));
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void onPinNewsClick(Result currentNews, View viewRoot) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_KEY, currentNews);
        Intent intent = new Intent(this, Detailed_Activity.class);
        intent.putExtra("A", bundle);
        intent.putExtra("B", currentNews.getThumbnail().getThumbnail());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Fade(Fade.IN));
            getWindow().setEnterTransition(new Fade(Fade.OUT));
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(
                            this, viewRoot.findViewById(R.id.pin_image_id), "image_trans");
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    private void savelastid(String id) {
        sPref = getSharedPreferences("Main", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_ID, id);
        ed.commit();
    }

    public void getNewNews() {
        Call<News> call = ApiManager.getApiClient().getNews(API_KEY, API_THUMBS, Utils.newsPage);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.body() != null && response.isSuccessful()) {

                    swipeRefresh.setRefreshing(false);
                    newsData = response.body();
                    newsAdapter.setNewsList((ArrayList<Result>) newsData.getResponse().getResults());
                    savelastid(newsData.getResponse().getResults().get(0).getId());
                }
            }
            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.e("ARSH", "OnFailure", t);
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void fillNews() {
        Call<News> call = ApiManager.getApiClient().getNews(API_KEY, API_THUMBS, pages);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.body() != null && response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    newsData = response.body();
                    newsAdapter.addAll(newsData.getResponse().getResults());
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.e("ARSH", "OnFailure", t);
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
       MenuItem pin=menu.findItem(R.id.action_pin);
       MenuItem save=menu.findItem(R.id.action_save);
       MenuItem grid=menu.findItem(R.id.action_change_layout);

       pin.setVisible(false);
       save.setVisible(false);

       return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_layout:
                if (gridLayoutManager.getSpanCount()==1) {
                    gridLayoutManager.setSpanCount(2);
                    item.setIcon(R.drawable.listmenu_con);
                }
                else {
                    gridLayoutManager.setSpanCount(1);
                    item.setIcon(R.drawable.gridmenu_icon);
                }
                return true;
        }
        return true;
    }



}
