package net.hongzhang.school.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import net.hongzhang.school.R;
import net.hongzhang.school.bean.VacationVo;

import java.util.List;


/**
 * 作者： wanghua
 * 时间： 2017/5/3
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class LeaveListExpendAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<List<VacationVo>> vactionVoList;
    private List<String> titles;

    public LeaveListExpendAdapter(Context context, List<String> titles, List<List<VacationVo>> vactionVoList) {
        this.context = context;
        this.vactionVoList = vactionVoList;
        this.titles = titles;
    }


    @Override
    public int getGroupCount() {
        return titles.size();
    }

    @Override
    public int getChildrenCount(int childPosition) {
        return vactionVoList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return titles.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childPosition;
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
        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.item_expend_leave_list, null);
            new GroupViewHolder(view);
        }
        groupViewHolder = (GroupViewHolder) view.getTag();
        groupViewHolder.textView.setText(titles.get(groupPosition));
        return view;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder childViewHolder;
        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.item_expend_leave_list_c, null);
            new ChildViewHolder(view);
        }
        childViewHolder = (ChildViewHolder) view.getTag();
        VacationVo vo = vactionVoList.get(groupPosition).get(childPosition);
        childViewHolder.tv_days.setText(vo.getDay_number() + "天");
        childViewHolder.tv_back_date.setText(vo.getBack_school_date().substring(0, 11));
        childViewHolder.tv_course.setText(vo.getVa_content());
        childViewHolder.tv_name.setText(vo.getTs_name());
        return view;
    }

    private class GroupViewHolder {
        TextView textView;

        public GroupViewHolder(View view) {
            textView = (TextView) view.findViewById(R.id.tv_title);
            view.setTag(this);
        }
    }

    private class ChildViewHolder {
        TextView tv_back_date;
        TextView tv_name;
        TextView tv_days;
        TextView tv_course;

        public ChildViewHolder(View view) {
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_back_date = (TextView) view.findViewById(R.id.tv_back_date);
            tv_days = (TextView) view.findViewById(R.id.tv_days);
            tv_course = (TextView) view.findViewById(R.id.tv_course);
            view.setTag(this);
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

}


