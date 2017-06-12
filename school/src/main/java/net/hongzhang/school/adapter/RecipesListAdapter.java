package net.hongzhang.school.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.hongzhang.school.R;
import net.hongzhang.school.bean.RecipesVo;
import net.hongzhang.status.util.PictrueUtils;

import java.util.List;

/**
 * 作者：Restring
 * 时间：2017/4/20
 * 描述：
 * 版本：
 */

public class RecipesListAdapter extends RecyclerView.Adapter<RecipesListAdapter.ViewHolder> {
    private Context context;
    private List<RecipesVo.CBInfo> cbInfoList;
    public RecipesListAdapter(Context context, List<RecipesVo.CBInfo> cbInfoList) {
        this.context = context;
        this.cbInfoList = cbInfoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipes_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecipesVo.CBInfo cbInfo = cbInfoList.get(position);
        holder.pictrueUtils.setPictrueLoad(context, cbInfo.getImgUrl(), holder.rl_photo);
        holder.tv_food_name.setText(cbInfo.getTitle());
        holder.tv_food_time.setText(cbInfo.getType_name());
    }

    @Override
    public int getItemCount() {
        return cbInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_food_time;
        TextView tv_food_name;
        RelativeLayout rl_photo;
        PictrueUtils pictrueUtils;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_food_time = (TextView) itemView.findViewById(R.id.tv_food_time);
            tv_food_name = (TextView) itemView.findViewById(R.id.tv_food_name);
            rl_photo = (RelativeLayout) itemView.findViewById(R.id.rl_photo);
            pictrueUtils = new PictrueUtils();
            itemView.setTag(this);
        }
    }
}
