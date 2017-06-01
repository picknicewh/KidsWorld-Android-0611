package net.hongzhang.school.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hongzhang.baselibrary.util.G;
import net.hongzhang.school.R;
import net.hongzhang.school.bean.DimensionalityTagVo;
import net.hongzhang.school.bean.SelectTagVo;
import net.hongzhang.school.presenter.CommentTaskTPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： wanghua
 * 时间： 2017/4/19
 * 名称：标签列表
 * 版本说明：
 * 附加注释：
 * 主要接口：TaskCommentSelectTagAdapter
 */
public class TaskCommentChildTagAdapter extends RecyclerView.Adapter<TaskCommentChildTagAdapter.ViewHolder> {
    private Activity context;
    private List<DimensionalityTagVo.ChildTagVo> tagList;
    private CommentTaskTPresenter presenter;
    private String activityWorksId;
    private String parentId;
    private int groupPosition;
    private List<DimensionalityTagVo> dimensionalityTagVoList;

    public TaskCommentChildTagAdapter(Activity context, List<DimensionalityTagVo> dimensionalityTagVoList, int groupPosition, String activityWorksId, CommentTaskTPresenter presenter) {
        G.initDisplaySize(context);
        this.context = context;
        this.dimensionalityTagVoList = dimensionalityTagVoList;
        this.tagList = dimensionalityTagVoList.get(groupPosition).getTags();
        this.parentId = dimensionalityTagVoList.get(groupPosition).getParentId();
        this.activityWorksId = activityWorksId;
        this.presenter = presenter;
        this.groupPosition = groupPosition;
        editTextList = new ArrayList<>();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                context).inflate(R.layout.item_task_comment_child, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    private List<EditText> editTextList;
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        editTextList.add(position,holder.tv_tag);
        holder.tv_tag.setFocusable(false);
        holder.tv_tag.setFocusableInTouchMode(false);
        if (position > 0) {
            holder.tv_tag.setVisibility(View.VISIBLE);
            holder.tv_tag.setText(tagList.get(position - 1).getTagName());

        } else {
            holder.tv_tag.setGravity(Gravity.CENTER);
            holder.tv_tag.setText("+");
            holder.tv_tag.setVisibility(View.VISIBLE);
            if (position >= 8) {
                holder.tv_tag.setVisibility(View.GONE);
            }
        }
        if (CommentTaskTPresenter.mapList.get(groupPosition).get(position)) {
            holder.tv_tag.setBackgroundResource(R.drawable.frame_green);
            holder.tv_tag.setTextColor(Color.WHITE);

        } else {
            holder.tv_tag.setBackgroundResource(R.drawable.frame_white);
            holder.tv_tag.setTextColor(ContextCompat.getColor(context, R.color.theme_text));
        }
        holder.tv_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position > 0) {
                    holder.tv_tag.setFocusable(false);
                    if (CommentTaskTPresenter.mapList.get(groupPosition).get(position)) {
                        holder.tv_tag.setBackgroundResource(R.drawable.frame_white);
                        holder.tv_tag.setTextColor(ContextCompat.getColor(context, R.color.theme_text));
                        CommentTaskTPresenter.mapList.get(groupPosition).put(position, false);
                    } else {
                        holder.tv_tag.setBackgroundResource(R.drawable.frame_green);
                        holder.tv_tag.setTextColor(Color.WHITE);
                        CommentTaskTPresenter.mapList.get(groupPosition).put(position, true);
                    }
                    selectTagTrans(holder.tv_tag);
                    addSelectTagVo(groupPosition, position);
                    presenter.setTagMapList(CommentTaskTPresenter.mapList, groupPosition);
                    notifyDataSetChanged();
                } else {
                    holder.tv_tag.setText("");
                    holder.tv_tag.setFocusableInTouchMode(true);
                    holder.tv_tag.setFocusable(true);
                    holder.tv_tag.requestFocus();
                    holder.tv_tag.setGravity(Gravity.LEFT |Gravity.CENTER_VERTICAL);
                    holder.tv_tag.setPadding(5,0,0,0);
                    isChangeText = true;
                }
            }
        });

    }
    public boolean isChangeText=false;
    public void setEditFouceChange(){
        EditText editText = editTextList.get(0);
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        if (isChangeText){
            String text = editText.getText().toString();
            addTag(text,editText);
            isChangeText  =false;
        }
    }
    private SelectTagVo getSelectTagVo(int groupPosition, int position) {
        SelectTagVo selectTagVo = new SelectTagVo();
        selectTagVo.setChildPosition(position);
        selectTagVo.setGroupPosition(groupPosition);
        return selectTagVo;
    }
    private void addSelectTagVo(int groupPosition, int position) {
        SelectTagVo selectTagVo = getSelectTagVo(groupPosition, position);
        for (int i = 0; i < CommentTaskTPresenter.selectTagVoList.size(); i++) {
            SelectTagVo tagVo = CommentTaskTPresenter.selectTagVoList.get(i);
            if (tagVo.getGroupPosition() == groupPosition) {
                CommentTaskTPresenter.selectTagVoList.set(i, selectTagVo);
            }
        }
    }
    private void selectTagTrans(TextView textView){
        int toY = G.dp2px(context,100);//垂直位移
        TranslateAnimation animation = new TranslateAnimation(0,toY,0,toY);
        animation.setDuration(5000);
        textView.startAnimation(animation);
    }
    /**
     * 添加标签
     *
     * @param tag 标签名称
     */
    public void addTag(String tag,EditText editText) {
        if (!G.isEmteny(tag)){
            DimensionalityTagVo.ChildTagVo childTagVo = new DimensionalityTagVo.ChildTagVo();
            childTagVo.setTagName(tag);
            tagList.add(childTagVo);
            presenter.addActivityTagName(tag, parentId, activityWorksId);
            presenter.initMapList(dimensionalityTagVoList);
            notifyDataSetChanged();
        }else {
            editText.setText("+");
            editText.setGravity(Gravity.CENTER);
        }
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
        private EditText tv_tag;

        public ViewHolder(View view) {
            super(view);
            tv_tag = (EditText) view.findViewById(R.id.tv_tag_child);
            int width = (G.size.W - G.dp2px(context, 60)) / 4;
            int height = G.dp2px(context, 40);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
            lp.setMargins(0, G.dp2px(context, 10), G.dp2px(context, 10), G.dp2px(context, 10));
            tv_tag.setLayoutParams(lp);
            view.setTag(this);
        }
    }

}
