package com.sothree.slidinguppanel.demo.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


/**
 * Created by Joe on 2016-03-24
 * Email: zhangjun166@pingan.com.cn
 */
public class TittleHolder extends RecyclerView.ViewHolder {
    private TextView tvTittle;

    public TittleHolder(View itemView) {
        super(itemView);
        tvTittle = (TextView) itemView;
    }

    public void bindTittleView(String tittle) {
        tvTittle.setText(tittle);
    }
}
