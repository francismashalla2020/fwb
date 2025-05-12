package com.thinkbold.fwb.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.thinkbold.fwb.NewsDetails;
import com.thinkbold.fwb.R;
import com.thinkbold.fwb.filters.NewsFilter;
import com.thinkbold.fwb.models.Get_news;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> implements Filterable {
    public List<Get_news> allnews;
    public List<Get_news> filternews;
    private final Context context;
    NewsFilter filter;
    public NewsAdapter(List<Get_news> allnews, List<Get_news> filternews, Context context) {
        this.allnews = allnews;
        this.filternews = filternews;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup paramViewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.newsdesign, paramViewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder paramViewHolder, @SuppressLint("RecyclerView") int position) {
        paramViewHolder.textViewTitle.setText(allnews.get(position).getHead());
        paramViewHolder.textViewTdesc.setText(allnews.get(position).getDesci());
        paramViewHolder.textViewpostdDate.setText(allnews.get(position).getDate());
        paramViewHolder.textViewCategory.setText(allnews.get(position).getCategory());

        paramViewHolder.c_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move = new Intent(context, NewsDetails.class);
                Bundle data = new Bundle();
                data.putString("id", allnews.get(position).getId());
                data.putString("title", allnews.get(position).getHead());
                data.putString("desci", allnews.get(position).getDesci());
                data.putString("stori", allnews.get(position).getStory());
                data.putString("cats", allnews.get(position).getCategory());
                data.putString("date", allnews.get(position).getDate());
                move.putExtras(data);
                context.startActivity(move);
            }
        });
    }
    @Override
    public int getItemCount() {
        return allnews.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter = new NewsFilter(filternews, this);
        }
        return filter;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView c_view;
        TextView textViewTitle, textViewTdesc, textViewpostdDate, textViewCategory;
        ViewHolder(View paramView) {
            super(paramView);
            c_view = paramView.findViewById(R.id.c_view);
            textViewTitle = paramView.findViewById(R.id.textViewTitle);
            textViewTdesc = paramView.findViewById(R.id.textViewTdesc);
            textViewpostdDate = paramView.findViewById(R.id.textViewpostdDate);
            textViewCategory = paramView.findViewById(R.id.textViewCategory);
        }
    }
    public void onAttachedToRecyclerView(RecyclerView paramRecyclerView) {
        super.onAttachedToRecyclerView(paramRecyclerView);
    }
}
