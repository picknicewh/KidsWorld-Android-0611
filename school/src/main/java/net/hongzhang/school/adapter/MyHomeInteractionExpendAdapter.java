package net.hongzhang.school.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.util.DateUtil;
import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.school.R;
import net.hongzhang.school.activity.RankingListActivity;
import net.hongzhang.school.bean.ActivityInfoVo;
import net.hongzhang.school.bean.ActivityInfoVos;
import net.hongzhang.school.widget.RoundImageView;

import java.util.Date;
import java.util.List;


/**
 * 作者： wanghua
 * 时间： 2017/5/3
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class MyHomeInteractionExpendAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<ActivityInfoVos> activityVosList;
    private int[] images  = new int[]{R.mipmap.ic_education,R.mipmap.ic_child};
    public MyHomeInteractionExpendAdapter(Context context, List<ActivityInfoVos> activityVosList) {
        this.context = context;
        this.activityVosList = activityVosList;
    }

    @Override
    public int getGroupCount() {
        return activityVosList.size();
    }

    @Override
    public int getChildrenCount(int childPosition) {
//        if (childPosition == 0) {
//            return 1;
//        }
        return activityVosList.get(childPosition).getActivityJsonList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return activityVosList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return activityVosList.get(groupPosition).getActivityJsonList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {
        GroupViewHolder groupViewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_home_interaction_t, null);
            new GroupViewHolder(view);
        }
        groupViewHolder = (GroupViewHolder) view.getTag();
        ActivityInfoVos activityVos = activityVosList.get(groupPosition);
        groupViewHolder.tvEductionTitle.setText(activityVos.getTypeName());
        Drawable drawableLeft = ContextCompat.getDrawable(context,images[groupPosition]);
        drawableLeft.setBounds(0,0,drawableLeft.getIntrinsicWidth(),drawableLeft.getIntrinsicHeight());
        Drawable drawableRight = ContextCompat.getDrawable(context,R.mipmap.ic_group_indicator);
        drawableRight.setBounds(0,0,drawableRight.getIntrinsicWidth(),drawableRight.getIntrinsicHeight());
         groupViewHolder.tvEductionTitle.setCompoundDrawables(drawableLeft,null,drawableRight,null);

        return view;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder childViewHoler;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_homeinteraciton_content, null);
            new ChildViewHolder(view);
        }
        childViewHoler = (ChildViewHolder) view.getTag();
        ActivityInfoVo activityVo = activityVosList.get(groupPosition).getActivityJsonList().get(childPosition);
        childViewHoler.tv_active_name.setText(activityVo.getTitle());
        //由于ChildViewHolder 复用，而Glide是异步加载的，所以当改变其中的一个内容时，则会同时改变同一位置上的内容
        if (activityVo.getImgUrls() != null && activityVo.getImgUrls().size() > 0) {
            ImageCache.imageLoader(activityVo.getImgUrls().get(0), childViewHoler.iv_bg);
        } else {
            Glide.with(childViewHoler.iv_bg.getContext())
                    .load(R.mipmap.ic_home_image_bg)
                    .into(childViewHoler.iv_bg);
        }
        if (activityVo.getAppraise() == 1) {
            if (activityVo.getAppraiseId() != null) {
                childViewHoler.iv_prise.setVisibility(View.VISIBLE);
            } else {
                childViewHoler.iv_prise.setVisibility(View.GONE);
            }
        } else {
            childViewHoler.iv_prise.setVisibility(View.GONE);
        }
        childViewHoler.iv_prise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RankingListActivity.class);
                context.startActivity(intent);
            }
        });
        childViewHoler.tv_active_check.setText(getChecks(activityVo.getDimensionalityName()));
        if (UserMessage.getInstance(context).getType().equals("1")) {
            childViewHoler.tv_end_time.setText(DateUtil.format_chinese.format(new Date(activityVo.getDeadline())));
            childViewHoler.tv_active_content.setText(activityVo.getContent());
            childViewHoler.ll_active_progress.setVisibility(View.GONE);
        } else {
            childViewHoler.tv_active_content.setText(DateUtil.format_chinese.format(new Date(activityVo.getDeadline())));
            childViewHoler.ll_active_progress.setVisibility(View.VISIBLE);
            childViewHoler.progressBar.setMax(activityVo.getStudentNumber());
            childViewHoler.progressBar.setProgress(activityVo.getNumber());
            childViewHoler.tv_end_time.setBackgroundResource(R.drawable.bg_task_progress);
            childViewHoler.tv_progress_percent.setText(activityVo.getNumber() + "/" +activityVo.getStudentNumber());
            childViewHoler.iv_status.setVisibility(View.VISIBLE);
            childViewHoler.tv_end_time.setVisibility(View.GONE);
            long currentTime = System.currentTimeMillis();
            if (currentTime > activityVo.getDeadline()) {
                childViewHoler.iv_status.setImageResource(R.mipmap.ic_task_finish);
               // childViewHoler.tv_end_time.setText("活动已截止");
            } else {
                if (currentTime < activityVo.getPostTime()) {
                    childViewHoler.iv_status.setImageResource(R.mipmap.ic_task_unpublish);
                   // childViewHoler.tv_end_time.setText("活动未发布");
                } else {
                    childViewHoler.iv_status.setImageResource(R.mipmap.ic_task_doing);
               //     childViewHoler.tv_end_time.setText("活动进行中");
                }
            }
        }

        return view;
    }

    private class GroupViewHolder {
        TextView tvEductionTitle;

        public GroupViewHolder(View view) {
            tvEductionTitle = (TextView) view.findViewById(R.id.tv_eduction_title);
            view.setTag(this);
        }
    }

    private class ChildViewHolder {
        RoundImageView iv_bg;
        TextView tv_active_name;
        TextView tv_end_time;
        TextView tv_active_check;
        TextView tv_active_content;
        ImageView iv_prise;
        LinearLayout ll_active_progress;
        ProgressBar progressBar;
        TextView tv_progress_percent;
        ImageView iv_status;
        public ChildViewHolder(View itemView) {
            tv_active_name = (TextView) itemView.findViewById(R.id.tv_active_name);
            iv_bg = (RoundImageView) itemView.findViewById(R.id.iv_bg);
            tv_end_time = (TextView) itemView.findViewById(R.id.tv_end_time);
            tv_active_check = (TextView) itemView.findViewById(R.id.tv_active_check);
            tv_active_content = (TextView) itemView.findViewById(R.id.tv_active_content);
            iv_prise = (ImageView) itemView.findViewById(R.id.iv_prise);
            progressBar = (ProgressBar) itemView.findViewById(R.id.pb_active_progress);
            tv_progress_percent = (TextView) itemView.findViewById(R.id.tv_progress_percent);
            ll_active_progress = (LinearLayout) itemView.findViewById(R.id.ll_active_progress);
            iv_status  = (ImageView) itemView.findViewById(R.id.iv_status);
            itemView.setTag(this);
        }
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    /**
     * 获取选择的维度
     */
    public String getChecks(List<String> values) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < values.size(); i++) {
            if (i == values.size() - 1) {
                buffer.append(values.get(i));
            } else {
                buffer.append(values.get(i)).append(" | ");
            }
        }
        return buffer.toString();
    }
}


