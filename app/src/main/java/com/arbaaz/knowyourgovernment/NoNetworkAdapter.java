package com.arbaaz.knowyourgovernment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Arbaaz on 16-04-2017.
 */

public class NoNetworkAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<Official> OfficialsList;
    private MainActivity mainAct;

    public NoNetworkAdapter(List<Official> officialsList, MainActivity mainAct) {
        OfficialsList = officialsList;
        this.mainAct = mainAct;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.d(TAG, "onCreateViewHolder: MAKING NEW");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.connectivity, parent, false);
        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Official official = OfficialsList.get(position);
        holder.ViewHolderTitle.setText("No Network Connection");
        holder.ViewHolderName.setText("Data cannot be accessed/loaded\n" +
                "without an internet connection.");
    }

    @Override
    public int getItemCount() {
        return OfficialsList.size();
    }
}
