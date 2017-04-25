package net.hongzhang.school.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.hongzhang.school.R;

/**
 * 作者：Restring
 * 时间：2017/4/20
 * 描述：
 * 版本：
 */

public class MyHomeInteractionAdapter extends RecyclerView.Adapter {
    private final int TITLE = 0;
    private final int CONTENT = 1;
    private boolean isLoadTitle = false; //是否加载标题

    public MyHomeInteractionAdapter() {
        this.isLoadTitle = false; //默认不加载标题
    }

    public MyHomeInteractionAdapter(boolean isLoadTitle) {
        this.isLoadTitle = isLoadTitle;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TITLE) {
            return new TitleViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_homeinteraction_title, parent, false));
        } else if (viewType == CONTENT) {
            return new ContentViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_homeinteraciton_content, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isLoadTitle) {
            if (position == 0) {
                ((TitleViewHolder) holder).tvTitle.setText("已截止");
            } else if (position == 4) {
                ((TitleViewHolder) holder).tvTitle.setText("进行中");
            } else if (position == 7) {
                ((TitleViewHolder) holder).tvTitle.setText("未发布");
            }
        }
    }

    @Override
    public int getItemCount() {
        return 10 + 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoadTitle && (position == 0 || position == 4 || position == 7)) {
            return TITLE;
        } else {
            return CONTENT;
        }
//        return super.getItemViewType(position);
    }

    private class TitleViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;

        TitleViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }

    private class ContentViewHolder extends RecyclerView.ViewHolder {

        ContentViewHolder(View itemView) {
            super(itemView);
        }
    }
}
