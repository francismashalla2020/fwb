package com.thinkbold.fwb.filters;

import android.widget.Filter;

import com.thinkbold.fwb.adapters.PaymentHistoryAdapter;
import com.thinkbold.fwb.models.Get_payment_history;

import java.util.ArrayList;
import java.util.List;

public class PaymentHistoryFilter extends Filter {

    private final PaymentHistoryAdapter adapter;
    private final List<Get_payment_history> originalList;

    public PaymentHistoryFilter(PaymentHistoryAdapter adapter, List<Get_payment_history> originalList) {
        this.adapter = adapter;
        this.originalList = originalList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        if (constraint != null && constraint.length() > 0) {
            String query = constraint.toString().toUpperCase();
            List<Get_payment_history> filteredList = new ArrayList<>();

            for (Get_payment_history item : originalList) {
                if (
                        item.getLoan_type().toUpperCase().contains(query) ||
                                item.getPayment_date().toUpperCase().contains(query) ||
                                item.getAmount().toUpperCase().contains(query) ||
                                item.getDescription().toUpperCase().contains(query) ||
                                item.getStatus().toUpperCase().contains(query)
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
        adapter.getPaymentHistoryList().clear();
        adapter.getPaymentHistoryList().addAll((List<Get_payment_history>) results.values);
        adapter.notifyDataSetChanged();
    }
}
