package net.hongzhang.discovery.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.discovery.R;
import net.hongzhang.discovery.modle.CommentInfoVo;

import java.util.List;

/**
 * ================================================
 * 作    者：wh
 * 时    间：2016/11/28
 * 描    述：
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class VideoCommentListAdapter extends RecyclerView.Adapter<VideoCommentListAdapter.ViewHolder> {
    private Activity context;
    private List<CommentInfoVo> commentInfoVos;
    private onItemClickListener itemClickListener = null;
    private int currentPosition = 0;


    public VideoCommentListAdapter(Activity context, List<CommentInfoVo> commentInfoVos) {
        this.context = context;
        this.commentInfoVos = commentInfoVos;

    }


    @Override
    public VideoCommentListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                context).inflate(R.layout.item_video_comment_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final VideoCommentListAdapter.ViewHolder holder, final int position) {
        CommentInfoVo commentInfoVo = commentInfoVos.get(position);
        ImageCache.imageLoader(commentInfoVo.getTsImgurl(), holder.iv_head_image);
        //显示的身份
        if (commentInfoVo.getTsType()==1){
            holder.tv_ts_type.setText("学");
            holder.tv_ts_type.setBackgroundResource(R.drawable.user_study_selecter);
        }else if (commentInfoVo.getTsType()==2||commentInfoVo.getTsType()==0){
            holder.tv_ts_type.setText("师");
            holder.tv_ts_type.setBackgroundResource(R.drawable.user_teach_selecter);
        }
        holder.tv_ts_name.setText(commentInfoVo.getTsName());
        holder.tv_comment_time.setText(commentInfoVo.getDate());
        holder.tv_content.setText(commentInfoVo.getContent());

        holder.rl_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.OnItemClick(view, position);
                }
            }
        });

    }
    public void setCurrentPosition(int position) {
        currentPosition = position;
        notifyDataSetChanged();
    }
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return commentInfoVos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl_content;
        private ImageView iv_head_image;
        private TextView tv_ts_type;
        private TextView tv_ts_name;
        private TextView tv_comment_time;
        private TextView tv_content;
        public ViewHolder(View view) {
            super(view);
            rl_content = (RelativeLayout)view.findViewById(R.id.rl_content) ;
            iv_head_image = (ImageView) view.findViewById(R.id.iv_head_image);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_ts_type = (TextView) view.findViewById(R.id.tv_ts_type);
            tv_ts_name = (TextView) view.findViewById(R.id.tv_ts_name);
            tv_comment_time = (TextView) view.findViewById(R.id.tv_comment_time);
            view.setTag(this);
        }
    }
    public void setOnItemClickListener(onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface onItemClickListener {
        void OnItemClick(View view, int position);
    }
}
