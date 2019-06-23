package com.example.sololearn_newsfeed.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sololearn_newsfeed.R;
import com.example.sololearn_newsfeed.persistence.entity.Result;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    public OnNewsClickListener onNewsClickListener;
    private List<Result> newsList = new ArrayList<>();
    private Context context;


    public NewsAdapter(Context context) {

        this.context = context;
    }

    public void setOnNewsClickListener(OnNewsClickListener onNewsClickListener) {
        this.onNewsClickListener = onNewsClickListener;

    }

    public void setNewsList(ArrayList<Result> newsList) {
        this.newsList = newsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_model, viewGroup, false);
        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, int i) {
        Result result = newsList.get(i);

        ViewCompat.setTransitionName(newsViewHolder.thumbnail, String.valueOf(newsList.get(i)));

        newsViewHolder.title.setText(result.getWebTitle());
        newsViewHolder.category.setText(result.getSectionName());
        try {
            Glide.with(context).load(newsList.get(i).getThumbnail().getThumbnail()).into(newsViewHolder.thumbnail);

        } catch (Exception e) {
        }


    }


    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void addAll(List<Result> list) {

        newsList.addAll(list);
        notifyDataSetChanged();


    }

    void add(Result result) {
        newsList.add(result);
        notifyItemChanged(newsList.size() - 1);
    }

    public interface OnNewsClickListener {

        void onNewsClick(Result currentNews, View viewroot);

    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView title;
        TextView category;


        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.image_id);
            category = itemView.findViewById(R.id.category_id);
            title = itemView.findViewById(R.id.news_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Result news = newsList.get(getLayoutPosition());
                    onNewsClickListener.onNewsClick(news, itemView);
                }
            });
        }
    }
}
