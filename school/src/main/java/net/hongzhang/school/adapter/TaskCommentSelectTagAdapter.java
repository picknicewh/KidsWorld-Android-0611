package net.hongzhang.school.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hongzhang.baselibrary.util.G;
import net.hongzhang.school.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/4/19
 * 名称：标签列表
 * 版本说明：评论分数选中标签列表
 * 附加注释：
 * 主要接口：
 */
public class TaskCommentSelectTagAdapter extends RecyclerView.Adapter<TaskCommentSelectTagAdapter.ViewHolder> {
    private Activity context;
    private List<String> tagList;
    private int position;
    public TaskCommentSelectTagAdapter(Activity context, List<String> tagList, int position) {
        G.initDisplaySize(context);
        this.context = context;
        this.tagList = tagList;
        this.position = position;

        G.log("xxxxxxxxxxxxxxxxxxxx" + position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                context).inflate(R.layout.item_task_comment_select_tag, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    private List<TextView> textViews = new ArrayList<>();
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        textViews.add(holder.tv_tag);
        holder.tv_tag.setText(tagList.get(position));
    }

    public void setTagList(List<String> tagList, int position) {
        this.tagList.clear();
        this.tagList.addAll(tagList);
        if (position==this.position){
            notifyDataSetChanged();
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return tagList.size() > 6 ? 6 : tagList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_tag;

        public ViewHolder(View view) {
            super(view);
            tv_tag = (TextView) view.findViewById(R.id.tv_tag_child);
            int width = (G.size.W - G.dp2px(context, 120)) / 4;
            int height = G.dp2px(context, 25);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
           // lp.gravity = Gravity.CENTER;
            tv_tag.setLayoutParams(lp);
         //   tv_tag.setBackgroundResource(R.drawable.infor_et_bg);
          //  tv_tag.setTextColor(ContextCompat.getColor(context, R.color.minor_text));
            view.setTag(this);
        }
    }

}
