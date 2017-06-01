package net.hongzhang.school.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.baselibrary.widget.CircleImageView;
import net.hongzhang.school.R;
import net.hongzhang.school.bean.RankingInfoVo;
import net.hongzhang.school.widget.StarProgressBar;

import java.util.List;

/**
 * 作者：Restring
 * 时间：2017/4/20
 * 描述：
 * 版本：
 */

public class MyRankListContentAdapter extends RecyclerView.Adapter<MyRankListContentAdapter.ViewHolder> {
     private List<RankingInfoVo> rankingInfoVoList;
    private Context context;
    public MyRankListContentAdapter(Context context, List<RankingInfoVo> rankingInfoVoList){
        this.rankingInfoVoList = rankingInfoVoList;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rank_list_content, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RankingInfoVo  rankingInfoVo = rankingInfoVoList.get(position);
        holder.ratingBar.setProgress(rankingInfoVo.getScore());
        ImageCache.imageLoader(rankingInfoVo.getImgUrl(),  holder.civImage);
        holder.tvName.setText(rankingInfoVo.getTsName());
        if (UserMessage.getInstance(context).getType().equals("1")){
            //家长版
            holder.ivSee.setVisibility(rankingInfoVo.getPublicity()==1?View.VISIBLE:View.GONE);
        }else {
            //教师版
            holder.ivSee.setVisibility(View.GONE);
            holder.tv_score.setVisibility(View.VISIBLE);
            holder.tv_score.setText(rankingInfoVo.getScore()+"分");
        }
    }

    @Override
    public int getItemCount() {
        return rankingInfoVoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView civImage;
        TextView tvName;
        StarProgressBar ratingBar;
        ImageView ivSee;
        TextView tv_score;
        public ViewHolder(View itemView) {
            super(itemView);
            civImage = (CircleImageView) itemView.findViewById(R.id.civ_image);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            ratingBar = (StarProgressBar) itemView.findViewById(R.id.ratingBar);
            ivSee = (ImageView) itemView.findViewById(R.id.iv_see);
            tv_score = (TextView) itemView.findViewById(R.id.tv_score);
            itemView.setTag(this);
        }
    }
}
