package net.hongzhang.school.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
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

public class MulLeaveListAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> titleList;
    private List<Integer> resIdList;
    private List<VacationVo> vacationVoList;
    private int beforeSize;
    private int afterSize;
    private Context context;
    public MulLeaveListAdapter2(Context context, List<VacationVo> vacationVoList, int beforeSize, int afterSize, List<String> titleList, List<Integer> resIdList) {
        this.vacationVoList = vacationVoList;
        this.titleList = titleList;
        this.resIdList = resIdList;
        this.context = context;
        this.beforeSize = beforeSize;
        this.afterSize = afterSize;
    }
    public void setSize(int beforeSize, int afterSize){
        this.beforeSize = beforeSize;
        this.afterSize =afterSize;
    }
    public enum ViewType {
        PARENT,
        CHILD
    }
    @Override
    public int getItemViewType(int position) {
        if (beforeSize > 0 && afterSize > 0) {
            if (position == 0 || position == beforeSize + 1) {
                return ViewType.PARENT.ordinal();
            } else {
                return ViewType.CHILD.ordinal();
            }
        } else {
            if (position == 0) {
                return ViewType.PARENT.ordinal();
            } else {
                return ViewType.CHILD.ordinal();
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ViewType.CHILD.ordinal()) {
            return new ChildViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_expend_leave_list_c, parent, false));
        } else {
            return new GroupViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_expend_leave_list, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ChildViewHolder) {
            ChildViewHolder childViewHolder = (ChildViewHolder) holder;
            if (vacationVoList.size() > 0) {//列表总量大于零，分三种情况，before>0 && after>0 ||  before<0 && after>0  || before>0 && after<0
                //列表总量大于零，分三种情况，before>0 && after>0 ||  before<0 && after>0  || before>0 && after<0
                VacationVo vo = null;
                if (beforeSize > 0 && afterSize > 0) {
                    if (position > 0 && position < beforeSize + 1) {
                        vo = vacationVoList.get(position - 1);
                    } else if (position > beforeSize + 1) {
                        vo = vacationVoList.get(position - beforeSize - 2);
                    }
                } else {
                    vo = vacationVoList.get(position-1);
                }
                childViewHolder.tv_days.setText(vo.getDay_number() + "天");
                childViewHolder.tv_back_date.setText(vo.getBack_school_date().substring(0, 11));
                childViewHolder.tv_course.setText(vo.getVa_content());
                childViewHolder.tv_name.setText(vo.getTs_name());
            }
        } else if (holder instanceof GroupViewHolder) {
            GroupViewHolder groupViewHolder = (GroupViewHolder) holder;
            if (titleList.size() > 0) {
                String title = "";
                int resId = 0;
                if (beforeSize > 0 && afterSize > 0) {
                    if (position == 0) {
                        title = titleList.get(0);
                        resId = resIdList.get(0);
                    } else if (position == beforeSize + 1) {
                        title = titleList.get(1);
                        resId = resIdList.get(1);
                    }
                } else {
                    title = titleList.get(0);
                    resId = resIdList.get(0);
                }
                groupViewHolder.textView.setText(title);
                Drawable drawableLeft = ContextCompat.getDrawable(context, resId);
                Drawable drawableRight = ContextCompat.getDrawable(context, R.mipmap.ic_medicine_days);
                drawableLeft.setBounds(0, 0, drawableLeft.getIntrinsicWidth(), drawableLeft.getIntrinsicHeight());
                drawableLeft.setBounds(0, 0, drawableRight.getIntrinsicWidth(), drawableRight.getIntrinsicHeight());
                groupViewHolder.textView.setCompoundDrawables(drawableLeft, null, drawableRight, null);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (beforeSize>0&& afterSize>0){
            return vacationVoList.size()+2;
        }else {
            if (afterSize<0&&beforeSize<0){
                return 0;
            }
            else {
                return vacationVoList.size()+1;
            }
        }
    }
    private class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public GroupViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.tv_title);
            view.setTag(this);
        }
    }

    private class ChildViewHolder extends RecyclerView.ViewHolder {
        TextView tv_back_date;
        TextView tv_name;
        TextView tv_days;
        TextView tv_course;

        public ChildViewHolder(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_back_date = (TextView) view.findViewById(R.id.tv_back_date);
            tv_days = (TextView) view.findViewById(R.id.tv_days);
            tv_course = (TextView) view.findViewById(R.id.tv_course);
            view.setTag(this);
        }
    }
}
