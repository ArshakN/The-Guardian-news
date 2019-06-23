package com.example.sololearn_newsfeed.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sololearn_newsfeed.R;
import com.example.sololearn_newsfeed.network.Thumbnails;
import com.example.sololearn_newsfeed.persistence.entity.Result;
import com.example.sololearn_newsfeed.utils.Utils;
import com.example.sololearn_newsfeed.viewmodel.NewsViewModel;

import static com.example.sololearn_newsfeed.utils.Utils.pinResult;


public class Detailed_Activity extends AppCompatActivity {
    private TextView title, description;
    private ImageView newsImage;
    private NewsViewModel newsViewModel;
    private Bundle bundle;
    private Result currentNews;
    private boolean isPined = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_);
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        newsViewModel.init();
        initViews();
        bundle = getIntent().getBundleExtra("A");
        currentNews = bundle.getParcelable(Utils.BUNDLE_KEY);
        title.setText(currentNews.getSectionName());
        description.setText(currentNews.getWebTitle());
        String url = getIntent().getStringExtra("B");
        currentNews.setThumbnail(new Thumbnails(url));

        try {
            Glide.with(this).load(url).into(newsImage);
        } catch (Exception e) {
            Log.e("SSS", e.getMessage());
        }

    }

    private void initViews() {
        title = findViewById(R.id.detailed_title);
        description = findViewById(R.id.detailed_newsText);
        newsImage = findViewById(R.id.detailed_image);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_pin) {
            if (!isPined) {
                Log.e("PIN", String.valueOf(pinResult.size()));
                pinResult.add(currentNews);
                isPined=true;
            }
        }
        if (id == R.id.action_save) {
            newsViewModel.saveNews(currentNews);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem greed = menu.findItem(R.id.action_change_layout);
        greed.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }
}





