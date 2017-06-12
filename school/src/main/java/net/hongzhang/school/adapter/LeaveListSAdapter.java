package net.hongzhang.school.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.hongzhang.school.R;
import net.hongzhang.school.bean.VacationVo;

import java.util.List;


/**
 * 作者：Restring
 * 时间：2017/4/20
 * 描述：
 * 版本：
 */

public class LeaveListSAdapter extends RecyclerView.Adapter<LeaveListSAdapter.ViewHolder> {

    private List<VacationVo> vactionVoList;
    public LeaveListSAdapter(List<VacationVo> vactionVoList) {
        this.vactionVoList = vactionVoList;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expend_leave_list_c, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        VacationVo vo = vactionVoList.get(position);
        holder.tv_days.setText(vo.getDay_number() + "天");
        holder.tv_back_date.setText(vo.getBack_school_date().substring(0, 11));
        holder.tv_course.setText(vo.getVa_content());
        holder.tv_name.setText(vo.getTs_name());

    }

    @Override
    public int getItemCount() {
        return vactionVoList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_back_date;
        TextView tv_name;
        TextView tv_days;
        TextView tv_course;

        public ViewHolder(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_back_date = (TextView) view.findViewById(R.id.tv_back_date);
            tv_days = (TextView) view.findViewById(R.id.tv_days);
            tv_course = (TextView) view.findViewById(R.id.tv_course);
            view.setTag(this);
        }
    }
}
