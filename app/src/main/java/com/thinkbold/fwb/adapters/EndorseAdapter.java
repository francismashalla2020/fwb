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

import com.thinkbold.fwb.EndorseDetails;
import com.thinkbold.fwb.R;
import com.thinkbold.fwb.filters.EndorseFilter;
import com.thinkbold.fwb.models.Get_Endorse;

import java.util.List;

public class EndorseAdapter extends RecyclerView.Adapter<EndorseAdapter.ViewHolder> implements Filterable {

    public final List<Get_Endorse> endorseList;
    private final List<Get_Endorse> endorseListFiltered;
    private final Context context;
    private EndorseFilter filter;


    public EndorseAdapter(List<Get_Endorse> endorseList, List<Get_Endorse> endorseListFiltered, Context context) {
        this.endorseList = endorseList;
        this.endorseListFiltered = endorseListFiltered;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.endorse_design, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.memberName.setText(endorseList.get(position).getMember_name());
        holder.amount.setText(endorseList.get(position).getAmount());

        String approvalStatus = endorseList.get(position).getApproval_status();
        if (approvalStatus.equalsIgnoreCase("1")) {
            holder.approvalStatus.setText(R.string.approved);
        } else if (approvalStatus.equalsIgnoreCase("0")) {
            holder.approvalStatus.setText(R.string.pending);
        } else if (approvalStatus.equalsIgnoreCase("2")) {
            holder.approvalStatus.setText(R.string.rejected);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move = new Intent(context, EndorseDetails.class);
                Bundle data = new Bundle();
                data.putString("referee_id", endorseList.get(position).getReferee_id());
                data.putString("loan_id", endorseList.get(position).getLoan_id());
                data.putString("member_id", endorseList.get(position).getMember_id());
                data.putString("member_name", endorseList.get(position).getMember_name());
                data.putString("amount", endorseList.get(position).getAmount());
                data.putString("approval_status", endorseList.get(position).getApproval_status());
                move.putExtras(data);
                context.startActivity(move);
            }
        });
    }

    @Override
    public int getItemCount() {
        return endorseList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new EndorseFilter(this, endorseListFiltered);
        }
        return filter;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView memberName, amount, approvalStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            memberName = itemView.findViewById(R.id.member_name);
            amount = itemView.findViewById(R.id.amount);
            approvalStatus = itemView.findViewById(R.id.approval_status);
        }
    }
}

