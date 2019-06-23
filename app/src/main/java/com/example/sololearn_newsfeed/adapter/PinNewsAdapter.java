package com.example.sololearn_newsfeed.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.sololearn_newsfeed.R;
import com.example.sololearn_newsfeed.persistence.entity.Result;

import java.util.ArrayList;
import java.util.List;

public class PinNewsAdapter extends RecyclerView.Adapter<PinNewsAdapter.PinNewsViewHolder> {
    private List<Result> newsList = new ArrayList<>();
    private Context context;
    private OnPinNewsClickListener onPinNewsClickListener;


    public PinNewsAdapter(Context context) {
        this.context = context;
    }

    public void setOnPinNewsClickListener(OnPinNewsClickListener onPinNewsClickListener) {
        this.onPinNewsClickListener = onPinNewsClickListener;
    }

    public void setNewsList(ArrayList<Result> newsList) {
        this.newsList = newsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PinNewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pin_news_model, viewGroup, false);
        return new PinNewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PinNewsViewHolder newsViewHolder, int i) {
        Result result = newsList.get(i);

        ViewCompat.setTransitionName(newsViewHolder.thumbnail, String.valueOf(newsList.get(i)));

        try {
            Glide.with(context).load(newsList.get(i).getThumbnail().getThumbnail()).transform(new CircleCrop()).into(newsViewHolder.thumbnail);
            Log.e("PIN", "GLIDE  " + newsList.get(i).getThumbnail().getThumbnail());
        } catch (Exception e) {
        }


    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    void addAll(List<Result> list) {

        newsList.addAll(list);
        notifyDataSetChanged();


    }

    void add(Result result) {
        newsList.add(result);
        notifyItemChanged(newsList.size() - 1);
    }

    public interface OnPinNewsClickListener {

        void onPinNewsClick(Result currentNews, View viewroot);

    }

    public class PinNewsViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;


        public PinNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.pin_image_id);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Result news = newsList.get(getLayoutPosition());
                    onPinNewsClickListener.onPinNewsClick(news, itemView);
                }
            });
        }
    }
}
