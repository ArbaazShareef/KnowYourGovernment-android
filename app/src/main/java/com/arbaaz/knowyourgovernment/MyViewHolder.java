package com.arbaaz.knowyourgovernment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Arbaaz on 08-04-2017.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView ViewHolderTitle,ViewHolderName;
    public MyViewHolder(View itemView) {
        super(itemView);
        ViewHolderTitle = (TextView)itemView.findViewById(R.id.ViewHolderTitle);
        ViewHolderName = (TextView)itemView.findViewById(R.id.ViewHolderName);
        }
}
