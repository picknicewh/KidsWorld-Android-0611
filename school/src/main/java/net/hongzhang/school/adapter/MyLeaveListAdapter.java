package net.hongzhang.school.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.hongzhang.school.R;
import net.hongzhang.school.activity.LeaveListActivity;
import net.hongzhang.school.bean.VacationVo;

import java.util.List;

/**
 * ================================================
 * 作    者：wh
 * 时    间：2016/9/19
 * 描    述：请假列表--适配器
 * 版    本：
 * 修订历史：
 * 主要接口：
 * ================================================
 */
public class MyLeaveListAdapter extends BaseAdapter {
    private LeaveListActivity context;
    private List<VacationVo> leaveVoList;

    public MyLeaveListAdapter(LeaveListActivity context, List<VacationVo> publishList) {
        this.context = context;
        this.leaveVoList = publishList;
    }

    @Override
    public int getCount() {
        return leaveVoList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHold viewHold;
        if(null==view){
            view= LayoutInflater.from(context).inflate(R.layout.item_expend_leave_list_c,null);
            new ViewHold(view);
        }
        VacationVo vo = leaveVoList.get(i);
        viewHold= (ViewHold) view.getTag();

        viewHold.tv_days.setText("请假"+vo.getDay_number()+"天");
        viewHold.tv_back_date.setText("预计"+vo.getBack_school_date().substring(0,12)+"返校");
        viewHold.tv_course.setText(vo.getVa_content());
        viewHold.tv_name.setText(vo.getTs_name());

        return view;
    }
     class ViewHold{
         TextView tv_back_date;
         TextView tv_name;
         TextView tv_days;
         TextView tv_course;
        public ViewHold(View view) {
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_back_date= (TextView) view.findViewById(R.id.tv_back_date);
            tv_days= (TextView) view.findViewById(R.id.tv_days);
            tv_course= (TextView) view.findViewById(R.id.tv_course);
            view.setTag(this);
        }
     }
}
