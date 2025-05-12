package com.thinkbold.fwb.filters;

import android.widget.Filter;

import com.thinkbold.fwb.adapters.MemberAdapter;
import com.thinkbold.fwb.models.Get_members;

import java.util.ArrayList;
import java.util.List;

public class MemberFilter extends Filter {
    MemberAdapter adapter;
    List<Get_members> getMembers;

    public MemberFilter(MemberAdapter adapter, List<Get_members> getMembers) {
        this.adapter = adapter;
        this.getMembers = getMembers;
    }

    @Override
    protected Filter.FilterResults performFiltering(CharSequence constraint) {
        Filter.FilterResults results = new Filter.FilterResults();
        if (constraint != null && constraint.length() > 0){
            constraint = constraint.toString().toUpperCase();
            List<Get_members> filteredPlayers = new ArrayList<>();
            for (int i=0; i < getMembers.size(); i++){
                if (getMembers.get(i).getFirst_name().toUpperCase().contains(constraint) || getMembers.get(i).getSurname().toUpperCase().contains(constraint) || getMembers.get(i).getPhone_no().toUpperCase().contains(constraint) || getMembers.get(i).getMiddle_name().toUpperCase().contains(constraint) || getMembers.get(i).getEmail().toUpperCase().contains(constraint)){
                    filteredPlayers.add(getMembers.get(i));
                }
            }
            results.count = filteredPlayers.size();
            results.values = filteredPlayers;
        }else {
            results.count = getMembers.size();
            results.values = getMembers;
        }
        return results;
    }
    @Override
    protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
        adapter.getMembers = (List<Get_members>) results.values;
        adapter.notifyDataSetChanged();
    }

}
