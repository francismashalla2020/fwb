package com.thinkbold.fwb.filters;

import android.widget.Filter;

import com.thinkbold.fwb.adapters.FinancialAdapter;
import com.thinkbold.fwb.models.Get_financials_details;

import java.util.ArrayList;
import java.util.List;

public class FinancialFilter extends Filter {

    private final FinancialAdapter adapter;
    private final List<Get_financials_details> originalList;

    public FinancialFilter(FinancialAdapter adapter, List<Get_financials_details> originalList) {
        this.adapter = adapter;
        this.originalList = originalList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        if (constraint != null && constraint.length() > 0) {
            String query = constraint.toString().toUpperCase();
            List<Get_financials_details> filteredList = new ArrayList<>();

            for (Get_financials_details item : originalList) {
                if (
                        item.getMember_name().toUpperCase().contains(query) ||
                                item.getShares().toUpperCase().contains(query) ||
                                item.getLong_term_loan().toUpperCase().contains(query) ||
                                item.getEmergency_loan().toUpperCase().contains(query) ||
                                item.getLong_term_status().toUpperCase().contains(query) ||
                                item.getEmergency_status().toUpperCase().contains(query)
                ) {
                    filteredList.add(item);
                }
            }

            results.count = filteredList.size();
            results.values = filteredList;

        } else {
            results.count = originalList.size();
            results.values = originalList;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.getFinancialList().clear();
        adapter.getFinancialList().addAll((List<Get_financials_details>) results.values);
        adapter.notifyDataSetChanged();
    }
}
