package com.thinkbold.fwb.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thinkbold.fwb.R;
import com.thinkbold.fwb.filters.PaymentHistoryFilter;
import com.thinkbold.fwb.models.Get_payment_history;

import java.util.ArrayList;
import java.util.List;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.ViewHolder> implements Filterable {

    private final Context context;
    private final List<Get_payment_history> paymentHistoryList;
    private final List<Get_payment_history> originalList;
    private PaymentHistoryFilter filter;

    public PaymentHistoryAdapter(List<Get_payment_history> paymentHistoryList, Context context) {
        this.context = context;
        this.paymentHistoryList = paymentHistoryList;
        this.originalList = new ArrayList<>(paymentHistoryList); // backup list for filtering
    }

    @NonNull
    @Override
    public PaymentHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_history_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentHistoryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Get_payment_history item = paymentHistoryList.get(position);

        holder.loanType.setText(item.getLoan_type());
        holder.paymentDate.setText(item.getPayment_date());
        holder.amount.setText(item.getAmount());
        holder.loanAmount.setText(item.getLoan_amount());
        holder.balance.setText(item.getBalance());
        holder.status.setText(item.getStatus());
        holder.description.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return paymentHistoryList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new PaymentHistoryFilter(this, originalList);
        }
        return filter;
    }

    public List<Get_payment_history> getPaymentHistoryList() {
        return paymentHistoryList;
    }

    public List<Get_payment_history> getOriginalList() {
        return originalList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView loanType, paymentDate, amount, loanAmount, balance, status, description;

        public ViewHolder(View itemView) {
            super(itemView);
            loanType = itemView.findViewById(R.id.loan_type);
            paymentDate = itemView.findViewById(R.id.payment_date);
            amount = itemView.findViewById(R.id.amount);
            loanAmount = itemView.findViewById(R.id.loan_amount);
            balance = itemView.findViewById(R.id.balance);
            status = itemView.findViewById(R.id.status);
            description = itemView.findViewById(R.id.description);
        }
    }
}
