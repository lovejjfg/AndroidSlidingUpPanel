package com.sothree.slidinguppanel.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.demo.holder.FangShirHolder;
import com.sothree.slidinguppanel.demo.holder.MoreFangshiHolder;
import com.sothree.slidinguppanel.demo.holder.MoreHolder;
import com.sothree.slidinguppanel.demo.holder.OnShowMoreClickListener;
import com.sothree.slidinguppanel.demo.holder.TittleHolder;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity implements OnShowMoreClickListener {

//    private RecyclerView recyclerView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_second);
//        recyclerView = (RecyclerView) findViewById(R.id.list);
//    }
private RecyclerView recyclerView;
    private ArrayList<Item> items;
    private ArrayList<Item> moreItems = new ArrayList<>();


    boolean hasMore = false;
    private FangshiAdapter adapter;
    private boolean isShow = false;
    private ArrayList<Item> moreFangshi;
    private Item mCurrent;
    private SlidingUpPanelLayout mLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new FangshiAdapter();
        recyclerView.setAdapter(adapter);

        initData();

    }

    private void initData() {
        items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Item item = new Item(Type.TITTLE);
            items.add(item);
            item.tittle = "标题" + i;
            int j = (int) (Math.random() * 10);
            if (j > 2) {
                hasMore = true;
                moreFangshi = new ArrayList<>();
            }
            for (int k = 0; k <= j; k++) {
                if (k <= 2) {
                    Item normal = new Item(Type.FANGSHI);
                    normal.tittle = "房事" + k;
                    items.add(normal);
                } else {
                    Item i1 = new Item(Type.MORE_FANGSHI);
                    i1.tittle = "更多房事" + k;
                    moreFangshi.add(i1);
                }
            }
            if (hasMore) {
                Item more = new Item(Type.MORE);
                more.list = moreFangshi;
                more.tittle = "点我还有";
                items.add(more);
                moreItems.add(more);
                hasMore = false;
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.demo, menu);
        MenuItem item = menu.findItem(R.id.action_toggle);
        if (mLayout != null) {
            if (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN) {
                item.setTitle(R.string.action_show);
            } else {
                item.setTitle(R.string.action_hide);
            }
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_toggle: {
                if (mLayout != null) {
                    if (mLayout.getPanelState() != SlidingUpPanelLayout.PanelState.HIDDEN) {
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                        item.setTitle(R.string.action_show);
                    } else {
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        item.setTitle(R.string.action_hide);
                    }
                }
                return true;
            }
            case R.id.action_anchor: {
                if (mLayout != null) {
                    if (mLayout.getAnchorPoint() == 1.0f) {
                        mLayout.setAnchorPoint(0.7f);
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                        item.setTitle(R.string.action_anchor_disable);
                    } else {
                        mLayout.setAnchorPoint(1.0f);
                        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        item.setTitle(R.string.action_anchor_enable);
                    }
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Item item) {
        Log.e("点击--是否显示：：：：", item.isShow + "");
        Log.i("item:", item.toString());
        mCurrent = item;
        //// TODO: 2016-03-24 more的点击
        int position = items.indexOf(item);
        if (position <= 0 || position >= items.size()) {
            return;
        }
        //这个position就是more的类型
        if (item.type == Type.MORE && item.list.size() > 0) {
            for (Item item1 : item.list) {
                if (item.isShow) {
                    items.add(position, item1);
                    adapter.notifyItemInserted(position);
                } else {
                    int pos = items.indexOf(item1);
                    if (pos > 0 && pos < items.size()) {
                        items.remove(pos);
                        adapter.notifyItemRemoved(pos);
                    }
                }
            }
        }

    }

    class FangshiAdapter extends RecyclerView.Adapter  {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case Type.TITTLE:
                    TextView textView = new TextView(parent.getContext());
                    textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(50);
                    textView.setTextColor(getResources().getColor(R.color.lite_blue));

                    return new TittleHolder(textView);
                case Type.FANGSHI:
                    TextView textView1 = new TextView(parent.getContext());
                    textView1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    textView1.setGravity(Gravity.CENTER);
                    textView1.setTextSize(40);
                    textView1.setTextColor(getResources().getColor(R.color.default_green_color));
                    return new FangShirHolder(textView1);
                case Type.MORE_FANGSHI:
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_morefangshi, parent, false);
                    return new MoreFangshiHolder(view);
                case Type.MORE:
                    TextView textView3 = new TextView(parent.getContext());
                    textView3.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    textView3.setGravity(Gravity.CENTER);
                    textView3.setTextSize(80);
                    textView3.setTextColor(getResources().getColor(R.color.default_red_color));
                    return new MoreHolder(textView3, SecondActivity.this);
            }

            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            //倒着通知更改
            switch (getItemViewType(position)) {
                case Type.TITTLE:
                    ((TittleHolder) holder).bindTittleView(items.get(position).tittle);
                    break;
                case Type.FANGSHI:
                    ((FangShirHolder) holder).bindFangshi(items.get(position).tittle);
                    break;
                case Type.MORE_FANGSHI:
                    ((MoreFangshiHolder) holder).bindMoreFangshi(items.get(position).tittle, items.get(position).isShow);
                    break;
                case Type.MORE:
                    ((MoreHolder) holder).bindMore(items.get(position), position);
                    Log.i("更多的位置：", position + "");
                    Log.i("item:", items.get(position).toString());
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public int getItemViewType(int position) {
            return items.get(position).type;
        }


    }

    interface Type {
        int TITTLE = 0;
        int FANGSHI = 1;
        int MORE_FANGSHI = 2;
        int MORE = 3;
    }

    public static class Item {
        public Item(int type) {
            this.type = type;
        }


        public int type;
        public String tittle;
        public ArrayList<Item> list;
        public boolean isShow = false;
    }
}
