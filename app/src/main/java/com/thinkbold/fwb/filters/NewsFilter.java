package com.thinkbold.fwb.filters;

import android.widget.Filter;

import com.thinkbold.fwb.adapters.NewsAdapter;
import com.thinkbold.fwb.models.Get_news;

import java.util.ArrayList;
import java.util.List;

public class NewsFilter extends Filter {

    NewsAdapter adapter;
    List<Get_news> allnews;

    public NewsFilter(List<Get_news> allnews, NewsAdapter adapter) {
        this.adapter = adapter;
        this.allnews = allnews;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null && constraint.length() > 0){
            constraint = constraint.toString().toUpperCase();
            List<Get_news> filteredPlayers = new ArrayList<>();
            for (int i=0; i < allnews.size(); i++){
                if (allnews.get(i).getHead().toUpperCase().contains(constraint) || allnews.get(i).getCategory().toUpperCase().contains(constraint) || allnews.get(i).getDesci().toUpperCase().contains(constraint) || allnews.get(i).getStory().toUpperCase().contains(constraint) || allnews.get(i).getDate().toUpperCase().contains(constraint)){
                    filteredPlayers.add(allnews.get(i));
                }
            }
            results.count = filteredPlayers.size();
            results.values = filteredPlayers;
        }else {
            results.count = allnews.size();
            results.values = allnews;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.allnews = (List<Get_news>) results.values;
        adapter.notifyDataSetChanged();
    }
}
