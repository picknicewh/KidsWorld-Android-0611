package net.hongzhang.school.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.hongzhang.baselibrary.image.ImageCache;
import net.hongzhang.baselibrary.util.G;
import net.hongzhang.baselibrary.widget.CircleImageView;
import net.hongzhang.school.R;
import net.hongzhang.school.bean.TaskInfoVo;

import java.util.List;

/**
 * ================================================
 * 作    者：wh
 * 时    间：2016/9/19
 * 描    述：学生上传活动列表--适配器
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class SubmitTaskListAdapter extends BaseAdapter {
    private Context context;
    private List<TaskInfoVo> taskListInfoVoList;

    public SubmitTaskListAdapter(Context context, List<TaskInfoVo> taskListInfoVoList) {
        this.context = context;
        this.taskListInfoVoList = taskListInfoVoList;
        G.log("------------------------"+taskListInfoVoList.size());
    }

    @Override
    public int getCount() {
        return taskListInfoVoList.size();
    }

    @Override
    public Object getItem(int i) {
        return taskListInfoVoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHold viewHold;
        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.item_publish_list, null);
            new ViewHold(view);
        }
        viewHold = (ViewHold) view.getTag();
        final TaskInfoVo taskListInfoVo = taskListInfoVoList.get(i);
        if (taskListInfoVo!=null){
            String tsName = taskListInfoVo.getTsName();
            viewHold.tv_publish_name.setText(getName(tsName));
            viewHold.tv_publish_time.setText(taskListInfoVo.getCreateTime());
            viewHold.tv_relation.setText(getParentRelation(tsName));
            if (taskListInfoVo.getAppraiseId() == null){
                viewHold.tv_apprise.setText("您未评价");
                viewHold.tv_apprise.setTextColor(ContextCompat.getColor(context,R.color.minor_text));
            }else {
                viewHold.tv_apprise.setText("您已评价");
                viewHold.tv_apprise.setTextColor(ContextCompat.getColor(context,R.color.home_blue));
            }
            ImageCache.imageLoader(taskListInfoVo.getImgUrl(), viewHold.civ_image);
        }


        return view;
    }
    class ViewHold {
        CircleImageView civ_image;
        TextView tv_publish_name;
        TextView tv_publish_time;
        TextView tv_relation;
        TextView tv_apprise;

        public ViewHold(View view) {
            tv_publish_name = (TextView) view.findViewById(R.id.tv_publish_name);
            tv_publish_time = (TextView) view.findViewById(R.id.tv_publish_time);
            tv_relation = (TextView) view.findViewById(R.id.tv_relation);
            tv_apprise = (TextView) view.findViewById(R.id.tv_apprise);
            civ_image = (CircleImageView) view.findViewById(R.id.civ_image);
            view.setTag(this);
        }
    }

    /**
     * 获取小孩的姓名
     *
     * @param tsName
     */
    private String getName(String tsName) {
        int index = tsName.indexOf("(");
        if (index != -1) {
            String name = tsName.substring(0, index);
            return name;
        }
        return null;
    }

    /**
     * 获取小孩的关系
     *
     * @param tsName
     */
    private String getParentRelation(String tsName) {
        int index = tsName.indexOf("(");
        int lastIndex = tsName.indexOf(")");
        if (index != -1 && lastIndex != -1) {
            String name = tsName.substring(index + 1, lastIndex);
            return name;
        }
        return null;
    }
}
