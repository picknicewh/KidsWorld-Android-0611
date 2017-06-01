package net.hongzhang.school.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import net.hongzhang.baselibrary.util.G;
import net.hongzhang.school.R;

import java.util.List;
import java.util.Map;

/**
 * 作者：Restring
 * 时间：2017/4/20
 * 描述：
 * 版本：
 */

public class ActionParentTagAdapter extends RecyclerView.Adapter<ActionParentTagAdapter.ViewHolder> {

    public List<Map<String, Object>> itemList;
    private Context context;
    private int images[] = new int[]{R.mipmap.ic_blue_gradient,R.mipmap.ic_green_gradient,
            R.mipmap.ic_violet_gradient,R.mipmap.ic_yellow_gradient,R.mipmap.ic_dark_green_gradient};
    public ActionParentTagAdapter(Context context, List<Map<String, Object>> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.iitem_action_tag_parent, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String, Object> map = itemList.get(position);
        String tag = (String) map.get("tag");
        int size = (int) map.get("size");
        holder.b_tag.setText(tag);
        holder.b_tag.setBackgroundResource(images[position%5]);
        LinearLayout.LayoutParams lp;
        int height = G.dp2px(context, 40);
        int padding = G.dp2px(context, 5);
        //如果子标签的大于3个，父标签占两个
        if (size > 3) {
            lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (height + padding) * 2);
            lp.setMargins(padding, padding, padding, padding);
            holder.b_tag.setLayoutParams(lp);
        }
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
