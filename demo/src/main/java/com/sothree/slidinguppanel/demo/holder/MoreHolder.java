package com.sothree.slidinguppanel.demo.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sothree.slidinguppanel.demo.SecondActivity;

/**
 * Created by Joe on 2016-03-24
 * Email: zhangjun166@pingan.com.cn
 */
public class MoreHolder extends RecyclerView.ViewHolder {
    private TextView mFooterView;
    OnShowMoreClickListener listener;
    SecondActivity.Item mItem;

    public MoreHolder(View itemView, OnShowMoreClickListener listener) {
        super(itemView);
        mFooterView = (TextView) itemView;
        this.listener = listener;
    }

    public void bindMore( SecondActivity.Item item,  int position) {
        this.mItem = item;
        mFooterView.setText(mItem.tittle);
        mFooterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItem.isShow = !mItem.isShow;
                listener.onClick(mItem);
            }
        });

    }
}
