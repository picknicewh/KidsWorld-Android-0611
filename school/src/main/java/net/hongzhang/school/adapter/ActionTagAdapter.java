package net.hongzhang.school.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.hongzhang.school.R;

import java.util.List;

/**
 * 作者：Restring
 * 时间：2017/4/20
 * 描述：
 * 版本：
 */

public class ActionTagAdapter extends RecyclerView.Adapter<ActionTagAdapter.ViewHolder> {

    public List<String> itemList;

    public ActionTagAdapter(List<String> itemList) {
        this.itemList = itemList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.iitem_action_tag, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.b_tag.setText(itemList.get(position));
        if (itemList.get(position).equals("")) {
            holder.b_tag.setVisibility(View.INVISIBLE);
        } else {
            holder.b_tag.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Button b_tag;

        public ViewHolder(View itemView) {
            super(itemView);
            b_tag = (Button) itemView.findViewById(R.id.b_tag);
            itemView.setTag(this);
        }
    }
}
