package net.hunme.school.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.hunme.school.R;
import net.hunme.school.bean.LeaveVo;

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
public class LeaveListAdapter extends BaseAdapter {
    private Context context;
    private List<LeaveVo> leaveVoList;

    public LeaveListAdapter(Context context, List<LeaveVo> publishList) {
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHold viewHold;
        if(null==view){
            view= LayoutInflater.from(context).inflate(R.layout.item_leave_list,null);
            new ViewHold(view);
        }
        LeaveVo vo=leaveVoList.get(i);
        viewHold= (ViewHold) view.getTag();
        viewHold.tv_date.setText(vo.getCreationTime().substring(0,10));
        viewHold.tv_name.setText(vo.getTsName());
        viewHold.tv_timeStart.setText(vo.getStartDate().substring(0,16)+"至");
        viewHold.tv_timeEnd.setText(vo.getEndDate().substring(0,16));
        viewHold.tv_reason.setText(vo.getCause());
        return view;
    }

     class ViewHold{
         TextView tv_date;
         TextView tv_name;
         TextView tv_timeStart;
         TextView tv_timeEnd;
         TextView tv_reason;

        public ViewHold(View view) {
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_timeStart= (TextView) view.findViewById(R.id.tv_timeStart);
            tv_date= (TextView) view.findViewById(R.id.tv_date);
            tv_timeEnd= (TextView) view.findViewById(R.id.tv_timeEnd);
            tv_reason= (TextView) view.findViewById(R.id.tv_reason);
            view.setTag(this);
        }

     }
}
