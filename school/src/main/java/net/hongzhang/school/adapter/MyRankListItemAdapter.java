package net.hongzhang.school.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.hongzhang.school.R;

/**
 * 作者：Restring
 * 时间：2017/4/20
 * 描述：
 * 版本：
 */

public class MyRankListItemAdapter extends RecyclerView.Adapter<MyRankListItemAdapter.ViewHolder> {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rank_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.vLine.setVisibility(position == getItemCount() - 1 ? View.GONE : View.VISIBLE);
        holder.ivTrophy.setVisibility(position == getItemCount() - 1 ? View.INVISIBLE : View.VISIBLE);
        holder.rvRankList.setAdapter(new MyRankListContentAdapter());
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rvRankList;
        View vLine;
        ImageView ivTrophy;

        public ViewHolder(View itemView) {
            super(itemView);
            rvRankList = (RecyclerView) itemView.findViewById(R.id.rv_rank_list);
            vLine = itemView.findViewById(R.id.v_line);
            ivTrophy = (ImageView) itemView.findViewById(R.id.iv_trophy);
            rvRankList.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            rvRankList.setNestedScrollingEnabled(false);
        }
    }
}
