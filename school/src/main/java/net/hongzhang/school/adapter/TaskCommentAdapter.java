package net.hongzhang.school.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hongzhang.baselibrary.util.G;
import net.hongzhang.school.R;
import net.hongzhang.school.widget.AddTagDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： wanghua
 * 时间： 2017/4/19
 * 名称：标签列表
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class TaskCommentAdapter extends RecyclerView.Adapter<TaskCommentAdapter.ViewHolder> {
    private Activity context;
    private  List<String> tagList;
    private Map<Integer, Boolean> mapList;
    public TaskCommentAdapter(Activity context, List<String> tagList) {
        G.initDisplaySize(context);
        this.context = context;
        this.tagList = tagList;
        initData();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                context).inflate(R.layout.item_task_comment, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    private void initData() {
        mapList = new HashMap<>();
        for (int i = 0; i < tagList.size() + 1; i++) {
            if (i == 0) {
                mapList.put(i, true);
            } else {
                mapList.put(i, false);
            }
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (position < tagList.size()) {
            holder.tv_tag.setVisibility(View.VISIBLE);
            holder.tv_tag.setText(tagList.get(position));
        } else {
            holder.tv_tag.setText("+");
            holder.tv_tag.setVisibility(View.VISIBLE);
            if (position >= 8) {
                holder.tv_tag.setVisibility(View.GONE);
            }
        }
        if (mapList.get(position)) {
            holder.tv_tag.setBackgroundResource(R.drawable.info_red);
            holder.tv_tag.setTextColor(Color.RED);
        } else {
            holder.tv_tag.setBackgroundResource(R.drawable.infor_et_bg);
            holder.tv_tag.setTextColor(Color.parseColor("#494949"));
        }
        holder.tv_tag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position < tagList.size()) {
                        if (mapList.get(position)) {
                            holder.tv_tag.setBackgroundResource(R.drawable.infor_et_bg);
                            holder.tv_tag.setTextColor(Color.parseColor("#494949"));
                            mapList.put(position, false);
                        } else {
                            holder.tv_tag.setBackgroundResource(R.drawable.info_red);
                            holder.tv_tag.setTextColor(Color.RED);
                            mapList.put(position, true);
                        }
                        notifyDataSetChanged();
                    }else {
                        AddTagDialog dialog = new AddTagDialog(context,TaskCommentAdapter.this);
                        dialog.initView();
                    }
                }
            });

    }
    public  void addTag(String tag){
        tagList.add(tag);
        initData();
        notifyDataSetChanged();
    }
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        if (tagList.size() == 8) {
            return 8;
        }
        return tagList.size() + 1;
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_tag;
        public ViewHolder(View view) {
            super(view);
            tv_tag = (TextView) view.findViewById(R.id.tv_tag);
            int width = (G.size.W - G.dp2px(context, 90)) / 5;
            int height = G.dp2px(context, 35);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
            lp.setMargins(0, G.dp2px(context, 10), G.dp2px(context, 10), G.dp2px(context, 10));
            tv_tag.setLayoutParams(lp);
            view.setTag(this);
        }
    }
}
