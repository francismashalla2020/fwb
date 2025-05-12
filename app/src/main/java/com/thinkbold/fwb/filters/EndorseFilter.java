package com.thinkbold.fwb.filters;

import android.widget.Filter;

import com.thinkbold.fwb.adapters.EndorseAdapter;
import com.thinkbold.fwb.models.Get_Endorse;

import java.util.ArrayList;
import java.util.List;

public class EndorseFilter extends Filter {
    private final EndorseAdapter adapter;
    private final List<Get_Endorse> originalList;

    public EndorseFilter(EndorseAdapter adapter, List<Get_Endorse> originalList) {
        this.adapter = adapter;
        this.originalList = originalList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint == null || constraint.length() == 0) {
            results.count = originalList.size();
            results.values = originalList;
        } else {
            String filterPattern = constraint.toString().toLowerCase().trim();
            List<Get_Endorse> filteredList = new ArrayList<>();
            for (Get_Endorse item : originalList) {
                if (item.getMember_name().toLowerCase().contains(filterPattern)) {
                    filteredList.add(item);
                }
            }
            results.count = filteredList.size();
            results.values = filteredList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.endorseList.clear();
        adapter.endorseList.addAll((List<Get_Endorse>) results.values);
        adapter.notifyDataSetChanged();
    }
}
