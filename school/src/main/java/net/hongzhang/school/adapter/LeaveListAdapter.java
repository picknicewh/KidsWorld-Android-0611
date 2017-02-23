package net.hongzhang.school.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.hongzhang.baselibrary.util.UserMessage;
import net.hongzhang.school.R;
import net.hongzhang.school.activity.LeaveListActivity;
import net.hongzhang.school.bean.LeaveVo;
import net.hongzhang.school.widget.DeleteDialog;

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
    private LeaveListActivity context;
    private List<LeaveVo> leaveVoList;

    public LeaveListAdapter(LeaveListActivity context, List<LeaveVo> publishList) {
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
            view= LayoutInflater.from(context).inflate(R.layout.item_leave_list,null);
            new ViewHold(view);
        }
        final LeaveVo vo=leaveVoList.get(i);
        viewHold= (ViewHold) view.getTag();
        viewHold.tv_date.setText(vo.getCreationTime().substring(0,10));

        viewHold.tv_timeStart.setText(vo.getStartDate().substring(0,16));
        viewHold.tv_timeEnd.setText(vo.getEndDate().substring(0,16));
        viewHold.tv_reason.setText(vo.getCause());

        if (UserMessage.getInstance(context).getType().equals("1")){
            viewHold.tv_read.setVisibility(View.VISIBLE);
            if (vo.getStatus()==2){
                viewHold.tv_read.setBackgroundColor(context.getResources().getColor(R.color.noread_grey));
                viewHold.tv_read.setText("已读");
            }else if (vo.getStatus()==1){
                viewHold.tv_read.setBackgroundColor(context.getResources().getColor(R.color.main_green));
                viewHold.tv_read.setText("未读");
            }
            viewHold.ll_name.setVisibility(View.GONE);
        }else {
            viewHold.tv_read.setVisibility(View.GONE);
            viewHold.tv_name.setText(vo.getTsName());

        }
        viewHold.ll_leave.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DeleteDialog deleteDialog = new DeleteDialog(context,vo.getVacation_id(),0,i);
                deleteDialog.initView();
                return true;
            }
        });

        return view;
    }
     class ViewHold{
         LinearLayout ll_leave;
         LinearLayout ll_name;
         TextView tv_date;
         TextView tv_name;
         TextView tv_timeStart;
         TextView tv_timeEnd;
         TextView tv_reason;
         TextView tv_read;
        public ViewHold(View view) {
            ll_name = (LinearLayout)view.findViewById(R.id.ll_name);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_timeStart= (TextView) view.findViewById(R.id.tv_timeStart);
            tv_date= (TextView) view.findViewById(R.id.tv_date);
            tv_timeEnd= (TextView) view.findViewById(R.id.tv_timeEnd);
            tv_reason= (TextView) view.findViewById(R.id.tv_reason);
            tv_read = (TextView) view.findViewById(R.id.tv_read);
            ll_leave = (LinearLayout) view.findViewById(R.id.ll_leave);
            view.setTag(this);
        }
     }
}
