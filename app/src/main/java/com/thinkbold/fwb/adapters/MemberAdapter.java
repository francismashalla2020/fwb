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

import com.thinkbold.fwb.ExMemberDetails;
import com.thinkbold.fwb.R;
import com.thinkbold.fwb.filters.MemberFilter;
import com.thinkbold.fwb.models.Get_members;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> implements Filterable {

    public List<Get_members> getMembers;
    public List<Get_members> getMembersf;
    private final Context context;
    MemberFilter filter;

    public MemberAdapter(List<Get_members> getMembers, List<Get_members> getMembersf, Context context) {
        this.getMembers = getMembers;
        this.getMembersf = getMembersf;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup paramViewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.memberdesign, paramViewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder paramViewHolder, @SuppressLint("RecyclerView") int position) {
        paramViewHolder.fname.setText(getMembers.get(position).getFirst_name());
        paramViewHolder.surname.setText(getMembers.get(position).getSurname());
        //paramViewHolder.status.setText(getMembers.get(position).getMember_status());
        String mstatus;
        mstatus = getMembers.get(position).getMember_status();

        if (mstatus.equalsIgnoreCase("1")){
            paramViewHolder.status.setText(R.string.a88);
        }else if (mstatus.equalsIgnoreCase("0")){
            paramViewHolder.status.setText(R.string.a89);
        } else if (mstatus.equalsIgnoreCase("2")) {
            paramViewHolder.status.setText(R.string.a90);
        }

        paramViewHolder.c_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move = new Intent(context, ExMemberDetails.class);
                Bundle data = new Bundle();
                data.putString("id", getMembers.get(position).getId());
                data.putString("fname", getMembers.get(position).getFirst_name());
                data.putString("mname", getMembers.get(position).getMiddle_name());
                data.putString("surname", getMembers.get(position).getSurname());
                data.putString("phone", getMembers.get(position).getPhone_no());
                data.putString("email", getMembers.get(position).getEmail());
                data.putString("address", getMembers.get(position).getAddress());
                data.putString("password", getMembers.get(position).getPassword());
                data.putString("role", getMembers.get(position).getRole());
                data.putString("status", getMembers.get(position).getMember_status());
                data.putString("date1", getMembers.get(position).getDate_created());
                data.putString("date2", getMembers.get(position).getDate_updated());
                move.putExtras(data);
                context.startActivity(move);
            }
        });
    }
    @Override
    public int getItemCount() {
        return getMembers.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null){
            filter = new MemberFilter(this, getMembersf);
        }
        return filter;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView c_view;
        TextView fname,surname, status;
        ViewHolder(View paramView) {
            super(paramView);
            c_view = paramView.findViewById(R.id.c_view);
            fname = paramView.findViewById(R.id.fname);
            status = paramView.findViewById(R.id.status);
            surname = paramView.findViewById(R.id.surname);
        }
    }
}
