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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.thinkbold.fwb.FinancialExtraDetails;
import com.thinkbold.fwb.R;
import com.thinkbold.fwb.filters.FinancialFilter;
import com.thinkbold.fwb.models.Get_financials_details;

import java.util.ArrayList;
import java.util.List;

public class FinancialAdapter extends RecyclerView.Adapter<FinancialAdapter.ViewHolder> implements Filterable {

    private final Context context;
    private final List<Get_financials_details> originalList;
    private final List<Get_financials_details> financialList;
    private FinancialFilter filter;

    public FinancialAdapter(List<Get_financials_details> financialList, Context context) {
        this.context = context;
        this.financialList = financialList;
        this.originalList = new ArrayList<>(financialList); // for backup on filtering
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.financial_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Get_financials_details data = financialList.get(position);

        holder.memberName.setText(data.getMember_name());
        holder.shares.setText(data.getShares());
        holder.longTermLoan.setText(data.getLong_term_loan());
        holder.longTermStatus.setText(data.getLong_term_status());
        holder.emergencyLoan.setText(data.getEmergency_loan());
        holder.emergencyStatus.setText(data.getEmergency_status());

        holder.viewMore.setOnClickListener(v -> {
            Intent move = new Intent(context, FinancialExtraDetails.class);
            Bundle datas = new Bundle();

            Get_financials_details item = getFinancialList().get(position);

            datas.putString("id", item.getMember_id());
            datas.putString("memberName", item.getMember_name());
            datas.putString("shares", item.getShares());

            // Long-term loan
            datas.putString("long_term_loan", item.getLong_term_loan());
            datas.putString("long_term_paid", item.getLong_term_paid());
            datas.putString("long_term_balance", item.getLong_term_loan_balance());
            datas.putString("long_term_status", item.getLong_term_status());
            datas.putString("long_term_last_payment_date", item.getLong_term_last_payment_date());

            // Emergency loan
            datas.putString("emergency_loan", item.getEmergency_loan());
            datas.putString("emergency_paid", item.getEmergency_paid());
            datas.putString("emergency_balance", item.getEmergency_loan_balance());
            datas.putString("emergency_status", item.getEmergency_status());
            datas.putString("emergency_last_payment_date", item.getEmergency_last_payment_date());

            move.putExtras(datas);
            context.startActivity(move);
        });

        holder.container.setOnClickListener(v -> {
            Intent move = new Intent(context, FinancialExtraDetails.class);
            Bundle datas = new Bundle();

            Get_financials_details item = getFinancialList().get(position);

            datas.putString("id", item.getMember_id());
            datas.putString("memberName", item.getMember_name());
            datas.putString("shares", item.getShares());

            // Long-term loan
            datas.putString("long_term_loan", item.getLong_term_loan());
            datas.putString("long_term_paid", item.getLong_term_paid());
            datas.putString("long_term_balance", item.getLong_term_loan_balance());
            datas.putString("long_term_status", item.getLong_term_status());
            datas.putString("long_term_last_payment_date", item.getLong_term_last_payment_date());

            // Emergency loan
            datas.putString("emergency_loan", item.getEmergency_loan());
            datas.putString("emergency_paid", item.getEmergency_paid());
            datas.putString("emergency_balance", item.getEmergency_loan_balance());
            datas.putString("emergency_status", item.getEmergency_status());
            datas.putString("emergency_last_payment_date", item.getEmergency_last_payment_date());

            move.putExtras(datas);
            context.startActivity(move);
        });

    }

    @Override
    public int getItemCount() {
        return financialList.size();
    }

    public List<Get_financials_details> getFinancialList() {
        return financialList;
    }

    public List<Get_financials_details> getOriginalList() {
        return originalList;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new FinancialFilter(this, originalList);
        }
        return filter;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView container;
        TextView memberName, shares,
                longTermLoan, longTermStatus,
                emergencyLoan, emergencyStatus;
        ImageView viewMore;

        public ViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.c_view);
            memberName = itemView.findViewById(R.id.financial_member_name);
            shares = itemView.findViewById(R.id.financial_shares);
            longTermLoan = itemView.findViewById(R.id.long_term_loan);
            longTermStatus = itemView.findViewById(R.id.long_term_status);
            emergencyLoan = itemView.findViewById(R.id.emergency_loan);
            emergencyStatus = itemView.findViewById(R.id.emergency_status);
            viewMore = itemView.findViewById(R.id.btn_view_more);
        }
    }
}
