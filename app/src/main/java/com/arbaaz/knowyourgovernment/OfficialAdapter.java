package com.arbaaz.knowyourgovernment;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Arbaaz on 08-04-2017.
 */

public class OfficialAdapter extends RecyclerView.Adapter<MyViewHolder>{
    private static final String TAG = "StockAdapter";
    private List<Official> OfficialsList;
    private MainActivity mainAct;

    public OfficialAdapter(List<Official> officialsList, MainActivity mainAct) {
        OfficialsList = officialsList;
        this.mainAct = mainAct;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.officail_list_row, parent, false);
        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Official official = OfficialsList.get(position);
        holder.ViewHolderTitle.setText(official.getTitle());
        holder.ViewHolderName.setText(official.getName()+" ("+official.getParty()+")");
    }

    @Override
    public int getItemCount() {
        return OfficialsList.size();
    }
}
