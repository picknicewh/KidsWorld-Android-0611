package net.hongzhang.school.adapter;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
public class TaskCommentTAdapter extends RecyclerView.Adapter<TaskCommentTAdapter.ViewHolder> {
    private Activity context;
    private List<DimensionalityTagVo> dimensionalityTagVoList;
    private String activityWorksId;
    private CommentTaskTPresenter presenter;
    private List<List<String>> selectList;
    private List<SelectTagVo> scrollList;
    private List<TaskCommentSelectTagAdapter> commentSelectTagAdapterList = new ArrayList<>();
    public TaskCommentTAdapter(Activity context, List<DimensionalityTagVo> dimensionalityTagVoList, List<List<String>> selectList, List<SelectTagVo> scrollList, String activityWorksId, CommentTaskTPresenter presenter) {
        G.initDisplaySize(context);
        this.context = context;
        this.dimensionalityTagVoList = dimensionalityTagVoList;
        this.activityWorksId = activityWorksId;
        this.presenter = presenter;
        this.selectList = selectList;
        this.scrollList = scrollList;
    }

    @Override
    public TaskCommentTAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_task_comment_t, parent, false));
    }

    @Override
    public void onBindViewHolder(TaskCommentTAdapter.ViewHolder viewHolder, int position) {
        DimensionalityTagVo tagVo = dimensionalityTagVoList.get(position);
        viewHolder.iv_tag_parent.setText(tagVo.getParentName());
       final TaskCommentChildTagAdapter adapter = new TaskCommentChildTagAdapter(context, dimensionalityTagVoList, position, activityWorksId, presenter);
        viewHolder.rv_child_tag.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        viewHolder.rv_child_tag.setAdapter(adapter);
        viewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                adapter.setEditFouceChange();//当编辑新建标签的时候，点击屏幕外的地方
                   // ，如果editText不为空，则添加新的标签，否则重置
                adapter.isChangeText = false;
                return false;
            }
        });
//        int scrollPosition = scrollList.get(position).getChildPosition();
      //  if (scrollPosition > 2) {
            //   viewHolder.rv_child_tag.scrollToPosition(scrollPosition);
      //  }
        TaskCommentSelectTagAdapter selectTagAdapter = new TaskCommentSelectTagAdapter(context, selectList.get(position), position);
        commentSelectTagAdapterList.add(selectTagAdapter);
        viewHolder.rv_select_tag.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        viewHolder.rv_select_tag.setAdapter(selectTagAdapter);

    }

    /**
     * 刷新界面
     * @param selectList    选中标签列表
     * @param groupPosition 当前选中便签的父级位置
     */
    public void setSelectList(List<List<String>> selectList, int groupPosition) {
        for (int i = 0; i < commentSelectTagAdapterList.size(); i++) {
            TaskCommentSelectTagAdapter adapter = commentSelectTagAdapterList.get(i);
            if (i == groupPosition) {
                adapter.setTagList(selectList.get(groupPosition), groupPosition);

            }
        }
    }
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return dimensionalityTagVoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView iv_tag_parent;
        private ImageView iv_tag_image;
        private RecyclerView rv_child_tag;
        private RecyclerView rv_select_tag;

        public ViewHolder(View view) {
            super(view);
            rv_child_tag = (RecyclerView) view.findViewById(R.id.rv_child_tag);
            rv_select_tag = (RecyclerView) view.findViewById(R.id.rv_select_tag);
            iv_tag_parent = (TextView) view.findViewById(R.id.tv_tag_parent);
            iv_tag_image = (ImageView) view.findViewById(R.id.iv_tag_image);
            view.setTag(this);
        }
    }
}
